package wint.lang.convert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import wint.lang.convert.converts.*;
import wint.lang.exceptions.ConvertException;
import wint.lang.utils.ClassUtil;

/**
 *
 * @author pister 2009-10-23
 */
public class ConvertUtil {
	
	private static Map<String, Convert<?>> types2Convert = new HashMap<String, Convert<?>>();
	
	private static final SmartDateConvert smartDateConvert = new SmartDateConvert();
	
	static {
		types2Convert.put("boolean", new BooleanConvert());
		types2Convert.put("Boolean", new BooleanConvert());
		types2Convert.put("byte", new ByteConvert());
		types2Convert.put("Byte", new ByteConvert());
		types2Convert.put("short", new ShortConvert());
		types2Convert.put("Short", new ShortConvert());
		types2Convert.put("int", new IntConvert());
		types2Convert.put("Integer", new IntConvert());
		types2Convert.put("long", new LongConvert());
		types2Convert.put("Long", new LongConvert());
		types2Convert.put("float", new FloatConvert());
		types2Convert.put("Float", new FloatConvert());
		types2Convert.put("double", new DoubleConvert());
		types2Convert.put("Double", new DoubleConvert());
		types2Convert.put("String", new StringConvert());
		types2Convert.put("string", new StringConvert());
		types2Convert.put("Date", new SmartDateConvert());
		types2Convert.put("date", new SmartDateConvert());
	}
	
	public static Convert<?> getSimpleConvert(String type) {
		return types2Convert.get(type);
	}
	
	@SuppressWarnings("unchecked")
	public static Object convertTo(Object input, String type, Object defaultValue) {
		if (input == null) {
			return defaultValue;
		}
		Convert<Object> convert = (Convert<Object>)types2Convert.get(type);
		if (convert == null) {
			return defaultValue;
		} else {
			return convert.convertTo(input, defaultValue);
		}
	}
	
	public static String toString(Object input) {
		if (input == null) {
			return null;
		}
		return input.toString();
	}
	
	/*public static String[] toStringArray(Object input) {
		if (input == null) {
			return null;
		}
		MagicObject magicObject = MagicObject.wrap(input);
		if (magicObject.isArray()) {
			
		}
	//	return input.toString();
	}*/
	
	@SuppressWarnings("unchecked")
	public static Object convertTo(Object input, Class<?> targetClass, Object defaultValue) {
		if (input == null) {
			return defaultValue;
		}
		if (input.getClass().isAssignableFrom(targetClass)) {
			return (Object)input;
		}
		
		Convert<Object> convert = (Convert<Object>)types2Convert.get(ClassUtil.getShortClassName(targetClass.getName()));
		if (convert == null) {
			// can not convert
			throw new ConvertException("can not convert " + input + " to " + targetClass);
		} else {
			return (Object)convert.convertTo(input, defaultValue);
		}
	}
	
	public static boolean toBoolean(String input, boolean defaultValue) {
		if (input == null) {
			return defaultValue;
		}
		try {
			return Boolean.valueOf(input).booleanValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static byte toByte(String input, byte defaultValue) {
		try {
			return Byte.parseByte(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static char toCharacter(String input, char defaultValue) {
		if (input == null) {
			return defaultValue;
		}
		if (input.length() == 1) {
			return input.charAt(0);
		} else {
			return defaultValue;
		}
	}
	
	public static short toShort(String input, short defaultValue) {
		try {
			return Short.parseShort(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static short toShort(String input, int radix, short defaultValue) {
		try {
			return Short.parseShort(input, radix);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static int toInt(String input, int defaultValue) {
		try {
			return Integer.parseInt(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static int toInt(String input, int radix, int defaultValue) {
		try {
			return Integer.parseInt(input, radix);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static long toLong(String input, int radix, long defaultValue) {
		try {
			return Long.parseLong(input, radix);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static long toLong(String input, long defaultValue) {
		try {
			return Long.parseLong(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static float toFloat(String input, float defaultValue) {
		try {
			return Float.parseFloat(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static double toDouble(String input, double defaultValue) {
		try {
			return Double.parseDouble(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Date toDate(String input, String pattern, Date defaultValue) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Date toDate(String input, Date defaultValue) {
		try {
			return smartDateConvert.convertTo(input, defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	
}
