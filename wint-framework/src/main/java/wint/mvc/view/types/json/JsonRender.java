package wint.mvc.view.types.json;

import java.io.IOException;
import java.io.Writer;

import wint.mvc.template.Context;

public interface JsonRender {
	
	void render(Context context, Writer writer) throws IOException;

}
