package wint.mvc.form.runtime;

import java.util.Map;

import wint.help.mvc.security.csrf.CsrfTokenUtil;
import wint.mvc.form.Field;
import wint.mvc.form.Form;

/**
 * @author pister
 * 2012-2-12 03:48:38
 */
public class DefaultRunTimeForm implements RunTimeForm {

	private Form form;
	
	public DefaultRunTimeForm(Form form) {
		super();
		this.form = form;
	}

	public Form getForm() {
		return form;
	}

	public Field get(String name) {
		return form.getFields().get(name);
	}

	public Map<String, Field> getFields() {
		return form.getFields();
	}

	public String getToken(String name) {
		return token();
	}

	public String token() {
		return CsrfTokenUtil.token();
	}

	@Override
	public String toString() {
		return "DefaultRunTimeForm [" + form.getName() + "]";
	}

}
