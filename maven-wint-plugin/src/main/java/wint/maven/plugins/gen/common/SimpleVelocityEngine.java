package wint.maven.plugins.gen.common;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.maven.plugin.logging.Log;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

public class SimpleVelocityEngine {
	
	private final Log log;
	
	private VelocityEngine velocityEngine;
	
	private String logTag = getClass().getSimpleName();
	
	static final String encoding = "utf-8";
	
	public SimpleVelocityEngine(Log log) {
		super();
		this.log = log;
		init(encoding);
	}

	private void init(String encoding) {
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
	                    	log.debug(message, t);
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
	                        return log.isDebugEnabled();
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
			throw new RuntimeException(e);
		}
	}
	
	public String merge(String templateString, Map<String, Object> context) {
		StringReader is = new StringReader(templateString);
		StringWriter out = new StringWriter();
		merge(is, out, context);
		return out.toString();
	}
	
	public void merge(Reader is, Writer out, Map<String, Object> context) {
		try {
			Context velocityContext = new VelocityContext(context);
			velocityEngine.evaluate(velocityContext, out, logTag, is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
