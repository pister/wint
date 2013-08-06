package wint.lang.template;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public interface SimpleTemplateEngine {
	
	void merge(Reader is, Writer out, Map<String, Object> context);
	
	String merge(String templateString, Map<String, Object> context);

}
