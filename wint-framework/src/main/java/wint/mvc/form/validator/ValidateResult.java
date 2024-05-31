package wint.mvc.form.validator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import wint.help.biz.result.MessageRender;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;

public class ValidateResult {

	private Map<String, List<MessageRender>> fieldMessages = MapUtil.newHashMap();

	public boolean isSuccess() {
		if (MapUtil.isEmpty(fieldMessages)) {
			return true;
		}
		for (Map.Entry<String, List<MessageRender>> entry : fieldMessages.entrySet()) {
			List<MessageRender> messages = entry.getValue();
			if (!CollectionUtil.isEmpty(messages)) {
				return false;
			}
		}
		return true;
	}

	public Map<String, List<MessageRender>> getFieldMessages() {
		return Collections.unmodifiableMap(fieldMessages);
	}

	public void setFieldMessages(String fieldName, List<MessageRender> messages) {
		fieldMessages.put(fieldName, messages);
	}
	
	public List<MessageRender> getFieldMessages(String fieldName) {
		return fieldMessages.get(fieldName);
	}
	
	public MessageRender getFieldMessage(String fieldName) {
		List<MessageRender> messages = getFieldMessages(fieldName);
		if (CollectionUtil.isEmpty(messages)) {
			return null;
		}
		return messages.get(0);
	}

}
