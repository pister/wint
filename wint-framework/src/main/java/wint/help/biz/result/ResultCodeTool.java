package wint.help.biz.result;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import wint.lang.utils.MapUtil;

class ResultCodeTool {

	private static Map<Class<? extends ResultCode>, Map<String, ResultCode>> enumFields = MapUtil.newHashMap();
	
	public static Map<String, ResultCode> getEnumFields(Class<? extends ResultCode> enumClass) {
		synchronized (enumFields) {
			Map<String, ResultCode> fields = null;
			try {
				fields = fetchFields(enumClass);
				enumFields.put(enumClass, fields);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
			return fields;
		}
	}
	
	private static Map<String, ResultCode> fetchFields(Class<? extends ResultCode> enumClass) throws IllegalArgumentException, IllegalAccessException {
		Map<String, ResultCode> ret = new HashMap<String, ResultCode>();
		Field[] fs = enumClass.getFields();
		for (Field f : fs) {
			int mod = f.getModifiers();
			if (Modifier.isPublic(mod) && Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
				String fieldName = f.getName();
				ResultCode v = (ResultCode)f.get(enumClass);
				ret.put(fieldName, v);
			}
		}
		return ret;
	}
	
}
