package wint.lang.template;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.lang.exceptions.TemplateWintException;
import wint.lang.exceptions.ViewException;

public class SimpleVelocityEngine extends AbstractSimpleTemplateEngine {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private VelocityEngine velocityEngine;
	
	private String logTag = getClass().getSimpleName();
	
	public void init(String encoding) {
		try {
			ExtendedProperties configuration = new ExtendedProperties();
	    	configuration.setProperty(Velocity.INPUT_ENCODING, encoding);
	    	configuration.setProperty(Velocity.OUTPUT_ENCODING, encoding);
	    	
	        configuration.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, new LogChute() {
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
            velocityEngine.setExtendedProperties(configuration);
            velocityEngine.init();
		} catch (Exception e) {
			throw new ViewException(e);
		}
	}
	
	public void merge(Reader is, Writer out, Map<String, Object> context) {
		try {
			Context velocityContext = new VelocityContext(context);
			velocityEngine.evaluate(velocityContext, out, logTag, is);
		} catch (Exception e) {
			throw new TemplateWintException(e);
		}
	}

}
