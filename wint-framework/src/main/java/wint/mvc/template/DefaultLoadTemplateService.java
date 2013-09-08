package wint.mvc.template;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import wint.core.config.Configuration;
import wint.core.config.Constants;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.AbstractService;
import wint.core.service.env.Environment;
import wint.core.service.initial.ConfigurationAwire;
import wint.core.service.thread.ThreadPoolService;
import wint.lang.exceptions.FlowDataException;
import wint.lang.exceptions.ResourceException;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicMap;
import wint.lang.utils.FileUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;
import wint.mvc.template.engine.TemplateEngine;
import wint.mvc.view.ViewRenderService;

public class DefaultLoadTemplateService extends AbstractService implements LoadTemplateService, ConfigurationAwire {
	
	private ViewRenderService viewRenderService;
	
	private Configuration configuration;
	
	private String templatePath;
	
	private String defaultLayoutFile;
	
	private String moduleActionName;
	
	private String templateLayoutName;
	
	private String templatePageName;

	private File templateBaseFile;
	
	private Environment environment;
	
	private ResourceLoader resourceLoader;
	
	private ExecutorService executorService;
	
	private ConcurrentMap<String, FutureTask<TemplateEntry>> cachedTemplateEntries = MapUtil.newConcurrentHashMap();
	
	@Override
	public void init() {
		super.init();
		this.resourceLoader = this.serviceContext.getResourceLoader();
		this.viewRenderService = (ViewRenderService)serviceContext.getService(ViewRenderService.class);
		MagicMap properties = configuration.getProperties();
		
		if (StringUtil.isEmpty(templatePath)) {
			templatePath = properties.getString(Constants.PropertyKeys.TEMPLATE_PATH, Constants.Defaults.TEMPLATE_PATH);
		}
		templateLayoutName = properties.getString(Constants.PropertyKeys.TEMPLATE_LAYOUT, Constants.Defaults.TEMPLATE_LAYOUT);
		templatePageName = properties.getString(Constants.PropertyKeys.TEMPLATE_PAGE, Constants.Defaults.TEMPLATE_PAGE);
		moduleActionName = properties.getString(Constants.PropertyKeys.APP_PACKAGE_WEB_ACTION, Constants.Defaults.APP_PACKAGE_WEB_ACTION);
		defaultLayoutFile = properties.getString(Constants.PropertyKeys.DEFAULT_LAYOUT_FILE, Constants.Defaults.DEFAULT_LAYOUT_FILE);
		environment = configuration.getEnvironment();
		
		ThreadPoolService threadPoolService = serviceContext.getService(ThreadPoolService.class);
		executorService = threadPoolService.getThreadPool();
		
		if (log.isInfoEnabled()) {
			log.info("template path: " + templatePath);
		}
		getBaseFile();
	}
	
	private void getBaseFile() {
		Resource resource = resourceLoader.getResource(templatePath);
		if (resource == null) {
			if (log.isWarnEnabled()) {
				log.warn("template path not exist: " + templatePath);
			}
			return;
		}
		
		templateBaseFile = resource.getFile();
		if (templateBaseFile == null) {
			if (log.isWarnEnabled()) {
				log.warn("template path not exist: " + templatePath);
			}
			return;
		}
		if (!templateBaseFile.exists()) {
			if (log.isWarnEnabled()) {
				log.warn("template path not exist: " + templatePath);
			}
			return;
		}
	}

	public TemplateRender loadTemplate(String templateName, Context context, String type) {
		if (moduleActionName.equals(type)) {
			type = templatePageName;
		}
		TemplateEntry templateEntry = loadTemplateEntry(templateName, type);
		return createTemplateRender(templateEntry, context);
	}
	
	protected TemplateEntry loadTemplateEntry(final String templateName, final String type) {
		if (environment == Environment.DEV){
			return loadTemplateEntryImpl(templateName, type);
		} else {
			String key = makeCacheKey(templateName, type);;
			FutureTask<TemplateEntry> newFutureTask = new FutureTask<TemplateEntry>(new Callable<TemplateEntry>() {
				public TemplateEntry call() throws Exception {
					return loadTemplateEntryImpl(templateName, type);
				}
			});
			FutureTask<TemplateEntry> existFutureTask = cachedTemplateEntries.putIfAbsent(key, newFutureTask);
			if (existFutureTask != null) {
				try {
					return existFutureTask.get();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new FlowDataException(e);
				} catch (ExecutionException e) {
					throw new FlowDataException(e);
				}
			} else {
				executorService.submit(newFutureTask);
				try {
					return newFutureTask.get();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new FlowDataException(e);
				} catch (ExecutionException e) {
					throw new FlowDataException(e);
				}
			}
		}
	}
	protected TemplateEntry loadTemplateEntryImpl(String templateName, String type) {
		if (templateLayoutName.equals(type)) {
			return loadTemplateSearchParent(templateName, type);
		} else {
			return loadTemplateImpl(templateName, type);
		}
	}
	
	protected String makeCacheKey(String templateName, String type) {
		return type + "." + templateName;
	}
	
	//environment

	private TemplateEntry loadTemplateImpl(String templateName, String type) {
		if (StringUtil.isEmpty(templateName)) {
			return new TemplateEntry(templateName, StringUtil.EMPTY);
		}
		File typedTemplate = new File(type);
		if (templateBaseFile == null || !templateBaseFile.exists()) {
			return null;
		}
		String templateExtension = getTemplateForExtension(templateName, type);
		if (templateExtension == null) {
			return null;
		}
		File targetFile = new File(typedTemplate, templateName + "." + templateExtension);
		return new TemplateEntry(targetFile.getPath(), templateExtension);
	}
	
	private TemplateEntry loadTemplateSearchParent(String templateName, String type) {
		TemplateEntry templateEntry = loadTemplateImpl(templateName, type);
		if (templateEntry != null) {
			return templateEntry;
		}
		
		templateEntry = loadTemplateImpl(getDefaultLayoutFileName(templateName), type);
		if (templateEntry != null) {
			return templateEntry;
		}
		
		File typedFile = new File(templateBaseFile, type);
		if (!typedFile.exists()) {
			return null;
		}
		File templateNameFile = new File(typedFile, templateName);
		File templateDir = templateNameFile.getParentFile();
		if (templateDir.equals(templateBaseFile)) {
			return null;
		}
		File parentFile = templateDir.getParentFile();
		if (parentFile.equals(templateBaseFile)) {
			return null;
		}
		String parentTarget = getParentTarget(templateName);
		if (StringUtil.isEmpty(parentTarget)) {
			return null;
		}
		return loadTemplateSearchParent(parentTarget, type);
	}
	
	private String getParentTarget(String target) {
		target = target.replace('\\', '/');
		int pos = target.lastIndexOf('/');
		if (pos < 0) {
			return null;
		}
		String dir = target.substring(0, pos);
		pos = dir.lastIndexOf('/');
		if (pos < 0) {
			return null;
		}
		return dir.substring(0, pos + 1) + defaultLayoutFile;
	}
	
	private String getDefaultLayoutFileName(String templateName) {
		templateName = templateName.replace('\\', '/');
		int pos = templateName.lastIndexOf('/');
		if (pos < 0) {
			return defaultLayoutFile;
		}
		return templateName.substring(0, pos + 1) + defaultLayoutFile;
	}
	
	protected TemplateRender createTemplateRender(TemplateEntry templateEntry, Context context) {
		if (templateEntry == null) {
			return null;
		}
		TemplateEngine viewRenderEngine = viewRenderService.getTemplateEngine(templateEntry.getTemplateExtension());
		return new DefaultTemplateRender(templateEntry.getPath(), viewRenderEngine, context);
	}
	
	private String getTemplateForExtension(String templateName, String type) {
		File typedFile = new File(templateBaseFile, type);
		
		File templateNameFile = new File(typedFile, templateName);
		File templateDir = templateNameFile.getParentFile();
		if (!templateDir.exists()) {
			return null;
		}
		final String targetTemplateName = templateNameFile.getName();
		File[] acceptableFiles = templateDir.listFiles(new FileFilter() {
			
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return false;
				}
				String name = pathname.getName();
				if (StringUtil.isEmpty(name)) {
					return false;
				}
				int pos = name.lastIndexOf('.');
				if (pos < 0) {
					return name.endsWith(targetTemplateName);
				}
				String nameWithoutExt = name.substring(0, pos);
				return nameWithoutExt.equals(targetTemplateName);
			}
		});
		
		if (acceptableFiles == null || acceptableFiles.length == 0) {
			return null;
		}
		if (acceptableFiles.length > 1) {
			throw new ResourceException("two many template files found for template: " 
					+ targetTemplateName +" type: "+ type +" they are: "
					+ SystemUtil.LINE_SEPARATOR
					+ MagicList.wrap(acceptableFiles).join(SystemUtil.LINE_SEPARATOR));
		}
		return FileUtil.getFileExtension(acceptableFiles[0].getName());
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

}
