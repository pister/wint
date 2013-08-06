package wint.lang.template;

import java.util.Map;

import wint.lang.io.FastStringReader;
import wint.lang.io.FastStringWriter;

public abstract class AbstractSimpleTemplateEngine implements SimpleTemplateEngine {

	public String merge(String templateString, Map<String, Object> context) {
		FastStringReader is = new FastStringReader(templateString);
		FastStringWriter out = new FastStringWriter();
		merge(is, out, context);
		return out.toString();
	}

}
