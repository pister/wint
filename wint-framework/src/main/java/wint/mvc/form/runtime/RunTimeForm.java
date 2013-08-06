package wint.mvc.form.runtime;

import java.util.Map;

import wint.mvc.form.Field;

public interface RunTimeForm {
	
	Field get(String name);
	
	Map<String, Field> getFields();

	/**
	 * @param field
	 * @return
	 * @deprecated user method token() istead
	 */
	String getToken(String field);
	
	/**
	 * 生成防止csrf的token
	 * @return
	 */
	String token();
	
}
