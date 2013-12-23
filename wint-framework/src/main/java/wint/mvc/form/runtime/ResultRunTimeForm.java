package wint.mvc.form.runtime;

import java.util.Date;
import java.util.Map;

import wint.help.mvc.security.csrf.CsrfTokenUtil;
import wint.lang.convert.ConvertUtil;
import wint.lang.magic.MagicObject;
import wint.lang.utils.DateUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.form.DefaultField;
import wint.mvc.form.Field;
import wint.mvc.form.Form;

/**
 * @author pister 2012-2-26 09:59:26
 */
public class ResultRunTimeForm implements RunTimeForm {

	private RunTimeForm runTimeForm;
	
	public ResultRunTimeForm(Form form) {
		super();
		runTimeForm = new DefaultRunTimeForm(form);
	}
	
	public ResultRunTimeForm(Form form, Object object) {
		super();
		runTimeForm = new ResultRunTimeFormImpl(form, object);
	}

	public Field get(String name) {
		return runTimeForm.get(name);
	}

	public Map<String, Field> getFields() {
		return runTimeForm.getFields();
	}
	
	public String getToken(String name) {
		return token();
	}

	public String token() {
		return runTimeForm.token();
	}



	private class ResultRunTimeFormImpl implements RunTimeForm {

		private Map<String, Field> runtimeFields = MapUtil.newHashMap();
		
		public ResultRunTimeFormImpl(Form form, Object object) {
			super();
			initFields(form, object);
		}

        private String valueToString(Object propertyValue) {
            if (propertyValue instanceof Date) {
                return DateUtil.formatFullDate(propertyValue);
            }
            return ConvertUtil.toString(propertyValue);
        }

		
		private void initFields(Form form, Object object) {
			MagicObject magicObject = MagicObject.wrap(object);
			Map<String, Field> fields = form.getFields();
			for (Map.Entry<String, Field> entry : fields.entrySet()) {
				String name = entry.getKey();
				Field field = entry.getValue();
				Field runtimeField = new DefaultField(field.getFieldConfig(), form);
				
				Object propertyValue = magicObject.getPropertyValue(name);
				String stringValue = valueToString(propertyValue);
				runtimeField.setValue(stringValue);
				// TODO
				//runtimeField.setValues(values);
				runtimeFields.put(name, runtimeField);
			}
		}

		public Field get(String name) {
			return runtimeFields.get(name);
		}

		public Map<String, Field> getFields() {
			return runtimeFields;
		}

		public String getToken(String name) {
			return token();
		}

		public String token() {
			return CsrfTokenUtil.token();
		}

	}

}
