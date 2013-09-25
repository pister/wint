package wint.mvc.view.types;

import java.io.IOException;
import java.io.Writer;

import wint.core.config.Constants;
import wint.lang.magic.MagicMap;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;
import wint.mvc.view.types.json.FastJsonRender;
import wint.mvc.view.types.json.JsonRender;

public class JsonViewRender extends AbstractViewRender {

	private JsonRender jsonRender = new FastJsonRender();

    private String contentType;

    @Override
    public void init() {
        super.init();
        MagicMap properties = serviceContext.getConfiguration().getProperties();
        String charset = properties.getString(Constants.PropertyKeys.CHARSET_ENCODING, Constants.Defaults.CHARSET_ENCODING);
        contentType = "application/json; charset=" + charset;
    }

    @Override
	public String getViewType() {
		return ViewTypes.JSON_VIEW_TYPE;
	}

	@Override
	public void render(Context context, InnerFlowData flowData, String target, String moduleType) {
		try {
            flowData.setContentType(contentType);
			Writer out = flowData.getWriter();
			jsonRender.render(context, out);
			out.close();
		} catch (IOException e) {
			log.error("json render error", e);
		}
	}

}
