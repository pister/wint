package wint.mvc.view.types;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import wint.core.config.Constants;
import wint.lang.magic.MagicMap;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.Field;
import wint.mvc.form.runtime.FormFactory;
import wint.mvc.form.runtime.RunTimeForm;
import wint.mvc.template.Context;
import wint.mvc.view.types.json.FastJsonRender;
import wint.mvc.view.types.json.GsonJsonRender;
import wint.mvc.view.types.json.JsonRender;

public class JsonViewRender extends AbstractViewRender {

	private JsonRender jsonRender;

    private String successField;

    private String messagesField;

    @Override
    public void init() {
        super.init();
        String jsonRoot = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_JSON_ROOT, Constants.Defaults.WINT_JSON_ROOT);
        successField = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_JSON_VIEW_FORM_VALIDATE_SUCCESS_FIELD, Constants.Defaults.WINT_JSON_VIEW_FORM_VALIDATE_SUCCESS_FIELD);
        messagesField = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_JSON_VIEW_FORM_VALIDATE_MESSAGES_FIELD, Constants.Defaults.WINT_JSON_VIEW_FORM_VALIDATE_MESSAGES_FIELD);
        jsonRender = new GsonJsonRender(jsonRoot);
    }

    @Override
	public String getViewType() {
		return ViewTypes.JSON_VIEW_TYPE;
	}

    private Map<String, String> forResultMessage(RunTimeForm runTimeForm) {
        Map<String, Field> fieldMap = runTimeForm.getFields();
        Map<String, String> resultMessage = MapUtil.newHashMap();
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();
            String message = field.getMessage();
            if (StringUtil.isEmpty(message)) {
                continue;
            }
            resultMessage.put(entry.getKey(), message);
        }
        return resultMessage;
    }

    private void handleFormResult(Context context, InnerFlowData flowData) {
        FormFactory formFactory = (FormFactory)flowData.getInnerContext().get(Constants.Form.TEMPLATE_FORM_FACTORY_NAME);
        Map<String, RunTimeForm> forms = formFactory.getForms();
        if (MapUtil.isEmpty(forms)) {
            return;
        }

         for (Map.Entry<String, RunTimeForm> entry : forms.entrySet()) {
             Map<String, String> messages = forResultMessage(entry.getValue());
           //  messages.put(entry.getKey(), forResultMessage(entry.getValue()));
             if (MapUtil.isEmpty(messages)) {
                 continue;
             }

             context.put(messagesField, messages);
             context.put(successField, false);
             // 只处理第一个
             break;
         }
    }


	@Override
	public void render(Context context, InnerFlowData flowData, String target, String moduleType) {
		try {
            handleFormResult(context, flowData);
			Writer out = flowData.getWriter();
			jsonRender.render(context, out);
			out.close();
		} catch (IOException e) {
			log.error("json render error", e);
		}
	}

}
