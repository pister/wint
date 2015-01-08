package wint.mvc.template.engine.velocity;

import java.io.IOException;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

import wint.core.config.Configuration;
import wint.core.config.Constants;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.ServiceContext;
import wint.core.service.env.Environment;
import wint.lang.exceptions.ViewException;
import wint.lang.io.FastStringWriter;
import wint.lang.utils.StringUtil;
import wint.mvc.template.Context;
import wint.mvc.template.TemplateRender;
import wint.mvc.template.engine.AbstractTemplateEngine;
import wint.mvc.template.engine.TemplateEngine;

/**
 * @author pister 2012-1-2 12:09:45
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine implements TemplateEngine {

	private static final String MODIFICATION_CHECK_INTERVAL = "file.resource.loader.modificationCheckInterval";
	
	private String macroLibrary = "macro.vm";
	
	private VelocityEngine velocityEngine;
	
	private EventCartridge eventCartridge = new EventCartridge();
	
	public void init(ServiceContext serviceContext) {
		super.init(serviceContext);
		try {
			
			Configuration wintConfiguration = serviceContext.getConfiguration();
			
			ReferenceInsertionEventHandler handler = new WintEventHandler(innerVariables, noFilterNames);
			eventCartridge.addEventHandler(handler);
			
			ExtendedProperties velocityConfiguration = new ExtendedProperties();
	    	velocityConfiguration.setProperty(Velocity.INPUT_ENCODING, encoding);
	    	velocityConfiguration.setProperty(Velocity.OUTPUT_ENCODING, encoding);
	    	
	    	if (wintConfiguration.getEnvironment().isSupportDev()) {
	    		velocityConfiguration.setProperty(Velocity.VM_LIBRARY_AUTORELOAD, true);
	    	} else {
	    		velocityConfiguration.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true);
                int checkInterval = wintConfiguration.getProperties().getInt(Constants.PropertyKeys.TEMPLATE_MODIFICATION_CHECK_INTERVAL, Constants.Defaults.TEMPLATE_MODIFICATION_CHECK_INTERVAL);
	    		velocityConfiguration.setProperty(MODIFICATION_CHECK_INTERVAL,  String.valueOf(checkInterval));
	    	}
	    	
	    	if (!StringUtil.isEmpty(templatePath)) {
	    		String baseTemplatePath = getAbsoluteTemplatePath();
	    		velocityConfiguration.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, baseTemplatePath);
	    	}
	    	
	    	if (!StringUtil.isEmpty(macroLibrary)) {
	    		ResourceLoader resourceLoader = serviceContext.getResourceLoader();
	    		Resource resource = resourceLoader.getResource(templatePath + "/" + macroLibrary);
	    		if (resource != null && resource.exist()) {
	    			velocityConfiguration.setProperty(Velocity.VM_LIBRARY, macroLibrary);
	    		} else {
	    			log.warn("Velocity Macro Library file " + macroLibrary + " does not exist.");
	    		}
	    	}
	    	
	        velocityConfiguration.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, new LogChute() {
	            public void init(RuntimeServices runtimeServices) {
	            }

	            public void log(int level, String message) {
	                log(level, message, null);
	            }

	            public void log(int level, String message, Throwable t) {
	                switch (level) {
	                    case TRACE_ID:
	                    	log.trace(message, t);
	                        break;
	                    case DEBUG_ID:
	                    	log.debug(message, t);
	                        break;
	                    case INFO_ID:
	                    	log.info(message, t);
	                        break;
	                    case WARN_ID:
	                    	log.warn(message, t);
	                        break;
	                    case ERROR_ID:
	                    	log.error(message, t);
	                        break;
	                    default:
	                    	break;
	                }
	            }

	            public boolean isLevelEnabled(int level) {
	                switch (level) {
	                    case TRACE_ID:
	                        return log.isTraceEnabled();
	                    case DEBUG_ID:
	                        return log.isDebugEnabled();
	                    case INFO_ID:
	                        return log.isInfoEnabled();
	                    case WARN_ID:
	                        return log.isWarnEnabled();
	                    case ERROR_ID:
	                        return log.isErrorEnabled();
	                    default:
	                        return false;
	                }
	            }
	        });

            velocityEngine = new VelocityEngine();
            velocityEngine.setExtendedProperties(velocityConfiguration);
            velocityEngine.init();
		} catch (Exception e) {
			throw new ViewException(e);
		}
	}

	protected String renderTemplate(TemplateRender templateRender, Context context) {
		try {
			Template template = velocityEngine.getTemplate(templateRender.getPath());
			VelocityContext velocityContext = new VelocityContext(context.getAll());
			eventCartridge.attachToContext(velocityContext);
			FastStringWriter writer = new FastStringWriter();
			template.merge(velocityContext, writer);
			return writer.toString();
		} catch (ResourceNotFoundException e) {
			throw new ViewException(e);
		} catch (ParseErrorException e) {
			throw new ViewException(e);
		} catch (IOException e) {
			throw new ViewException(e);
		} catch (Exception e) {
			throw new ViewException(e);
		}
	}
	
	public String getName() {
		return "vm";
	}

	public String getMacroLibrary() {
		return macroLibrary;
	}

	public void setMacroLibrary(String macroLibrary) {
		this.macroLibrary = macroLibrary;
	}

}
