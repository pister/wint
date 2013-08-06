package wint.mvc.view;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import wint.core.service.ServiceContext;
import wint.lang.exceptions.ViewException;
import wint.lang.utils.FileUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.template.Context;
import wint.mvc.template.TemplateRender;
import wint.mvc.template.engine.AbstractTemplateEngine;
import wint.mvc.template.engine.TemplateEngine;

public class DefaultViewRenderEngine extends AbstractTemplateEngine implements TemplateEngine {

	@Override
	public void init(ServiceContext serviceContext) {
		super.init(serviceContext);
	}

	public String getName() {
		return StringUtil.EMPTY;
	}

	protected String renderTemplate(TemplateRender templateRender, Context context) {
		File targetFile = new File(baseFile, templateRender.getPath());
		if (!targetFile.exists()) {
			return StringUtil.EMPTY;
		}
		if (!targetFile.isFile()) {
			return StringUtil.EMPTY;
		}
		try {
			return FileUtil.readAsString(targetFile, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new ViewException(e);
		} catch (IOException e) {
			throw new ViewException(e);
		}
	}

}
