package wint.mvc.template.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.core.config.Configuration;
import wint.core.config.Constants;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.ServiceContext;
import wint.lang.exceptions.ViewException;
import wint.lang.misc.profiler.Profiler;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.IoUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.template.Context;
import wint.mvc.template.TemplateRender;

/**
 * @author pister 2012-1-2 12:10:18
 */
public abstract class AbstractTemplateEngine implements TemplateEngine {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected String encoding;
	
	protected String templatePath;
	
	protected ServiceContext serviceContext;
	
	protected String noFilterNamesFile;
	
	protected Set<String> noFilterNames = CollectionUtil.newHashSet();

	protected Set<String> innerVariables = CollectionUtil.newHashSet();

    private String baseFile;
	
	public void init(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
		Configuration configuration = serviceContext.getConfiguration();
		if (StringUtil.isEmpty(templatePath)) {
			templatePath = configuration.getProperties().getString(Constants.PropertyKeys.TEMPLATE_PATH, Constants.Defaults.TEMPLATE_PATH);
    	}
		if (StringUtil.isEmpty(encoding)) {
			encoding = configuration.getProperties().getString(Constants.PropertyKeys.CHARSET_ENCODING, Constants.Defaults.CHARSET_ENCODING);
		}
		String pageName = configuration.getProperties().getString(Constants.PropertyKeys.PAGE_CONTENT_NAME, Constants.Defaults.PAGE_CONTENT_NAME);
		String widgetName = configuration.getProperties().getString(Constants.PropertyKeys.WIDGET_CONTAINER_NAME, Constants.Defaults.WIDGET_CONTAINER_NAME);
		innerVariables.add(pageName);
		innerVariables.add(widgetName);
		
		if (StringUtil.isEmpty(noFilterNamesFile)) {
			noFilterNamesFile = configuration.getProperties().getString(Constants.PropertyKeys.NO_FILTER_NAMES_FILE, Constants.Defaults.NO_FILTER_NAMES_FILE);
		}
		
		loadNoFilterNames();
	}
	
	private void loadNoFilterNames() {
		Resource resource = serviceContext.getResourceLoader().getResource(noFilterNamesFile);
		if (resource == null || !resource.exist()) {
			return;
		}
		BufferedReader reader = null;
		try {
			InputStream is = resource.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String name = line.trim();
				if (StringUtil.isEmpty(name)) {
					continue;
				}
				noFilterNames.add(name);
			}			
		} catch(Exception e) {
			log.error("read resource error", e);
		} finally {
			IoUtil.close(reader);
		}
	}
	
	protected String getAbsoluteTemplatePath() {
        if (baseFile != null) {
            return baseFile;
        }
		ResourceLoader resourceLoader = serviceContext.getResourceLoader();
		Resource resource = resourceLoader.getResource(templatePath);
		if (resource == null || !resource.exist()) {
			return templatePath;
		}
		try {
			return resource.getFile().getCanonicalPath();
		} catch (IOException e) {
			throw new ViewException(e);
		}
	}
	
	public String render(TemplateRender templateRender, Context context) {
		try {
			Profiler.enter("render template " + templateRender.getPath());
			return renderTemplate(templateRender, context);
		} finally {
			Profiler.release();
		}
	}
	
	protected abstract String renderTemplate(TemplateRender templateRender, Context context);

	public String getEncoding() {
		return encoding;
	}

	public final void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public final void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getNoFilterNamesFile() {
		return noFilterNamesFile;
	}

	public void setNoFilterNamesFile(String noFilterNamesFile) {
		this.noFilterNamesFile = noFilterNamesFile;
	}

    @Override
    public void setBasePath(String baseFile) {
        this.baseFile = baseFile;
    }
}
