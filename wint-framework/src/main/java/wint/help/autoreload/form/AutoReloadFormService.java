package wint.help.autoreload.form;

import java.util.Map;
import java.util.Set;

import wint.core.config.Constants;
import wint.core.io.resource.Resource;
import wint.core.io.resource.autoload.LastModifiedFile;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.ServiceContext;
import wint.core.service.util.ServiceUtil;
import wint.help.autoreload.AbstractAutoReload;
import wint.lang.exceptions.CanNotFindFormException;
import wint.lang.template.SimpleTemplateEngine;
import wint.lang.template.SimpleVelocityEngine;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.DefaultForm;
import wint.mvc.form.Form;
import wint.mvc.form.FormService;
import wint.mvc.form.config.FormConfig;
import wint.mvc.form.config.FormConfigLoader;
import wint.mvc.form.config.ParseResult;
import wint.mvc.form.config.XmlFormConfigLoader;

/**
 * 自动加载form服务
 * @author pister
 * 2012-5-16 下午11:01:05
 */
public class AutoReloadFormService  {

	private FormService targetFormService;
	
	private ServiceContext serviceContext;
	
	public AutoReloadFormService(ServiceContext serviceContext) {
		super();
		this.serviceContext = serviceContext;
		init();
	}

	public Form getForm(String name, InnerFlowData flowData) {
		return targetFormService.getForm(name, flowData);
	}

	public void init() {
		AutoReloadForm autoReloadForm = new AutoReloadForm();
		autoReloadForm.startAutoReload();
	}
	
	class AutoReloadForm extends AbstractAutoReload {

		private Set<LastModifiedFile> configResources = CollectionUtil.newHashSet();
		
		private SimpleTemplateEngine simpleTemplateEngine;
		
		private Map<String, FormConfig> formConfigs;
		
		public AutoReloadForm() {
			ResourceLoader resourceLoader = serviceContext.getResourceLoader();
			Resource defaultFormConfigResource = resourceLoader.getResource(Constants.Form.FORM_CONFIG_FILE);
			configResources.add(new LastModifiedFile(defaultFormConfigResource));
			
			SimpleVelocityEngine simpleTemplateEngine = new SimpleVelocityEngine();
			String encoding = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.CHARSET_ENCODING, Constants.Defaults.CHARSET_ENCODING);
			simpleTemplateEngine.init(encoding);
			
			this.simpleTemplateEngine = simpleTemplateEngine;
			
			onReload();
			
			for (String name :getFormConfigResources()) {
				configResources.add(new LastModifiedFile(resourceLoader.getResource(name)));
			}
			
		}
		
		private Set<String> getFormConfigResources() {
			ResourceLoader resourceLoader = serviceContext.getResourceLoader();
			Resource defaultFormResource = resourceLoader.getResource(Constants.Form.FORM_CONFIG_FILE);
			if (defaultFormResource != null && defaultFormResource.exist()) {
				FormConfigLoader formConfigLoader = new XmlFormConfigLoader(serviceContext.getResourceLoader(), simpleTemplateEngine);
				return formConfigLoader.parse(Constants.Form.FORM_CONFIG_FILE).getResourceNames();
			} else {
				return CollectionUtil.newHashSet();
			}
		}
		
		@Override
		protected Set<LastModifiedFile> getCheckFiles() {
			return configResources;
		}

		@Override
		protected synchronized void onReload() {
			FormService newFormService = new FormService() {

				public void destroy() {
				}

				public String getName() {
					return ServiceUtil.getSerivceName(getClass());
				}

				public ServiceContext getServiceContext() {
					return serviceContext;
				}

				public Form getForm(String name, InnerFlowData flowData) {
					FormConfig formConfig = formConfigs.get(name);
					if (formConfig == null) {
						throw new CanNotFindFormException("can not find form for name: " + name);
					}
					return new DefaultForm(formConfig, flowData);
				}

				public void init() {
				}
			};
			
			ResourceLoader resourceLoader = serviceContext.getResourceLoader();
			Resource defaultFormResource = resourceLoader.getResource(Constants.Form.FORM_CONFIG_FILE);
			if (defaultFormResource != null && defaultFormResource.exist()) {
				SimpleVelocityEngine simpleVelocityEngine = new SimpleVelocityEngine();
				String encoding = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.CHARSET_ENCODING, Constants.Defaults.CHARSET_ENCODING);
				simpleVelocityEngine.init(encoding);
				simpleTemplateEngine = simpleVelocityEngine;
				FormConfigLoader formConfigLoader = new XmlFormConfigLoader(serviceContext.getResourceLoader(), simpleVelocityEngine);
				ParseResult parseResult = formConfigLoader.parse(Constants.Form.FORM_CONFIG_FILE);
				formConfigs = parseResult.getFormConfigs();
				
				Set<LastModifiedFile> newConfigResources = CollectionUtil.newHashSet();
				for (String name : parseResult.getResourceNames()) {
					newConfigResources.add(new LastModifiedFile(resourceLoader.getResource(name)));
				}
				configResources = newConfigResources;
			} else {
				formConfigs =  MapUtil.newHashMap();
			}
			
			targetFormService = newFormService;
			log.info("form config reload success.");
		}
		
	}
	

}
