package wint.mvc.form.validator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;

public class ValidateResult {

	private Map<String, List<String>> fieldMessages = MapUtil.newHashMap();

	public boolean isSuccess() {
		if (MapUtil.isEmpty(fieldMessages)) {
			return true;
		}
		for (Map.Entry<String, List<String>> entry : fieldMessages.entrySet()) {
			List<String> messages = entry.getValue();
			if (!CollectionUtil.isEmpty(messages)) {
				return false;
			}
		}
		return true;
	}

	public Map<String, List<String>> getFieldMessages() {
		return Collections.unmodifiableMap(fieldMessages);
	}

	public void setFieldMessages(String fieldName, List<String> messages) {
		fieldMessages.put(fieldName, messages);
	}
	
	public List<String> getFieldMessages(String fieldName) {
		return fieldMessages.get(fieldName);
	}
	
	public String getFieldMessage(String fieldName) {
		List<String> messages = getFieldMessages(fieldName);
		if (CollectionUtil.isEmpty(messages)) {
			return null;
		}
		return messages.get(0);
	}

}
