package wint.mvc.form.config;

import java.lang.reflect.Field;
import java.util.Map;

import wint.core.config.Configuration;
import wint.core.config.Constants;
import wint.help.biz.result.MessageRender;
import wint.help.biz.result.ResultCode;
import wint.help.biz.result.StringMessage;
import wint.lang.exceptions.FormConfigException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicMap;
import wint.lang.template.SimpleTemplateEngine;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.form.validator.Validator;
import wint.mvc.form.validator.ValidatorUtil;
import wint.mvc.i18n.I18nResourceFinder;

public class ValidatorConfig {

    private String type;

    private MagicMap parameters;

    private String rawMessage;

    private MessageRender message;

    private Validator validator;

    private FieldConfig fieldConfig;

    private I18nResourceFinder i18nResourceFinder;

    public void init(SimpleTemplateEngine simpleTemplateEngine, I18nResourceFinder i18nResourceFinder) {
        this.i18nResourceFinder = i18nResourceFinder;
        evalValidatorObject();

        if (isI18nMessage()) {
            loadI18nResultCode();
        } else {
            evalStringMessage(simpleTemplateEngine);
        }
    }

    private boolean isI18nMessage() {
        if (StringUtil.isEmpty(rawMessage)) {
            return false;
        }
        if (rawMessage.startsWith("#!")) {
            return true;
        }
        return false;
    }

    private void evalValidatorObject() {
        validator = ValidatorUtil.lookFor(type, parameters);
    }

    private void loadI18nResultCode() {
        String resultCodeName = rawMessage.substring(2);
        String className = StringUtil.getFirstBefore(resultCodeName, ".");
        Class<?> clazz = i18nResourceFinder.findClassBySimpleName(className);
        if (clazz == null) {
            throw new FormConfigException("not found resultCode class: " + className);
        }
        String fieldName = StringUtil.getFirstAfter(resultCodeName, ".");
        MagicClass magicClass = MagicClass.wrap(clazz);
        Field field = magicClass.getField(fieldName);
        Object value = null;
        try {
            value = field.get(clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (!(value instanceof ResultCode)) {
            throw new FormConfigException("field:" + fieldName + " is not ResultCode of " + className);
        }
        message = (ResultCode)value;
    }

    private void evalStringMessage(SimpleTemplateEngine simpleTemplateEngine) {
        Map<String, Object> context = MapUtil.newHashMap();
        context.put(Constants.Form.VAR_NAME_FORM, fieldConfig.getFormConfig());
        context.put(Constants.Form.VAR_NAME_FIELD, fieldConfig);
        if (!MapUtil.isEmpty(parameters)) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
        }
        message = new StringMessage(simpleTemplateEngine.merge(rawMessage, context));
    }

    public Validator getValidator() {
        return validator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MagicMap getParameters() {
        return parameters;
    }

    public void setParameters(MagicMap parameters) {
        this.parameters = parameters;
    }

    public MessageRender getMessage() {
        return message;
    }

    public void setMessage(MessageRender message) {
        this.message = message;
    }

    public FieldConfig getFieldConfig() {
        return fieldConfig;
    }

    public void setFieldConfig(FieldConfig fieldConfig) {
        this.fieldConfig = fieldConfig;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

}
