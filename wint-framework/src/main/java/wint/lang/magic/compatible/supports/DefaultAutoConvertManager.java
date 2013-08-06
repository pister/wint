package wint.lang.magic.compatible.supports;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import wint.lang.convert.converts.BooleanConvert;
import wint.lang.convert.converts.Convert;
import wint.lang.convert.converts.DoubleConvert;
import wint.lang.convert.converts.FloatConvert;
import wint.lang.convert.converts.IntConvert;
import wint.lang.convert.converts.LongConvert;
import wint.lang.convert.converts.ShortConvert;
import wint.lang.convert.converts.SmartDateConvert;
import wint.lang.convert.converts.SmartTimestampConvert;
import wint.lang.convert.converts.StringConvert;
import wint.lang.exceptions.CanNotFindObjectException;
import wint.lang.magic.compatible.AutoConvertManager;
import wint.lang.utils.MapUtil;

public class DefaultAutoConvertManager extends AutoConvertManager {

	private Map<Class<?>, Convert<?>> converts = MapUtil.newHashMap();
	
	public DefaultAutoConvertManager() {
		registerDefaultConverts();
	}
	
	protected void registerDefaultConverts() {
		converts.put(boolean.class, new BooleanConvert());
		converts.put(Boolean.class, new BooleanConvert());
		converts.put(short.class, new ShortConvert());
		converts.put(Short.class, new ShortConvert());
		converts.put(int.class, new IntConvert());
		converts.put(Integer.class, new IntConvert());
		converts.put(long.class, new LongConvert());
		converts.put(Long.class, new LongConvert());
		converts.put(float.class, new FloatConvert());
		converts.put(Float.class, new FloatConvert());
		converts.put(double.class, new DoubleConvert());
		converts.put(Double.class, new DoubleConvert());
		converts.put(String.class, new StringConvert());
		converts.put(Date.class, new SmartDateConvert());
		converts.put(Timestamp.class, new SmartTimestampConvert());
	}
	
	@Override
	public Object convert(Object input, Class<?> targetType) {
		if (input != null && targetType.isInstance(input)) {
			return input;
		}
		Convert<?> convert = converts.get(targetType);
		if (convert == null) {
			throw new CanNotFindObjectException("can not find " + targetType + "\'s convert! input: " + input);
		}
		return convert.convertTo(input);
	}
	
	public <T> void registerConvert(Class<T> clazz, Convert<T> convert) {
		converts.put(clazz, convert);
	}

}
