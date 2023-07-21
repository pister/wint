package wint.help.sql;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.lang.utils.MapUtil;

/**
 * @author pister 2012-3-8 下午09:02:50
 */
public class TypeUtil {
	
	private static final Logger log = LoggerFactory.getLogger(TypeUtil.class);
	
	private static final Map<Integer, Class<?>> sqlType2JavaTypes = MapUtil.newHashMap();
	
	private static final Map<Class<?>, MethodEntity> javaType2MethodName = MapUtil.newHashMap();
	
	private static final Map<Class<?>, MethodPair> javaType2Method = MapUtil.newHashMap();
	
	private static final Class<?> BYTE_ARRAY_CLASS = (new byte[]{}).getClass();
	
	private static final Class<ResultSet> resultSetClass =  ResultSet.class;
	
	private static final Class<PreparedStatement> preparedStatementClass =  PreparedStatement.class;
	
	private static final Class<?>[] SINGLE_STRING_ARRAY_CLASS = new Class[] {String.class};
	
	
	static {
		initTypeMapping();
		initMethodNameMapping();
		initMethods();
	}

	private static void initTypeMapping() {
		sqlType2JavaTypes.put(Types.BOOLEAN, Boolean.TYPE);
		sqlType2JavaTypes.put(Types.BIT, Byte.TYPE);
		sqlType2JavaTypes.put(Types.INTEGER, Integer.TYPE);
		sqlType2JavaTypes.put(Types.BIGINT, Long.TYPE);
		sqlType2JavaTypes.put(Types.CHAR, String.class);
		sqlType2JavaTypes.put(Types.DATE, Date.class);
		sqlType2JavaTypes.put(Types.DECIMAL, BigDecimal.class);
		sqlType2JavaTypes.put(Types.DOUBLE, Double.TYPE);
		sqlType2JavaTypes.put(Types.FLOAT, Float.TYPE);
		sqlType2JavaTypes.put(Types.JAVA_OBJECT, Object.class);
		sqlType2JavaTypes.put(Types.LONGNVARCHAR, String.class);
		sqlType2JavaTypes.put(Types.LONGVARBINARY, BYTE_ARRAY_CLASS);
		sqlType2JavaTypes.put(Types.LONGVARCHAR, String.class);
		sqlType2JavaTypes.put(Types.NCHAR, String.class);
		sqlType2JavaTypes.put(Types.NCLOB, BYTE_ARRAY_CLASS);
		sqlType2JavaTypes.put(Types.NULL, Object.class);
		sqlType2JavaTypes.put(Types.NUMERIC, Double.TYPE);
		sqlType2JavaTypes.put(Types.NVARCHAR, String.class);
		sqlType2JavaTypes.put(Types.OTHER, Object.class);
		sqlType2JavaTypes.put(Types.REAL, Double.TYPE);
		sqlType2JavaTypes.put(Types.SMALLINT, Short.TYPE);
		sqlType2JavaTypes.put(Types.TIME, Time.class);
		sqlType2JavaTypes.put(Types.TIMESTAMP, Timestamp.class);
		sqlType2JavaTypes.put(Types.TINYINT, Byte.TYPE);
		sqlType2JavaTypes.put(Types.VARBINARY, BYTE_ARRAY_CLASS);
		sqlType2JavaTypes.put(Types.VARCHAR, String.class);
	}
	
	private static void initMethodNameMapping() {
	    javaType2MethodName.put(Byte.class, new MethodEntity("Byte", Byte.TYPE));
	    javaType2MethodName.put(Byte.TYPE,  new MethodEntity("Byte", Byte.TYPE));
	    javaType2MethodName.put(Short.class,  new MethodEntity("Short", Short.TYPE));
	    javaType2MethodName.put(Short.TYPE,  new MethodEntity("Short", Short.TYPE));
	    javaType2MethodName.put(Integer.class,  new MethodEntity("Int", Integer.TYPE));
        javaType2MethodName.put(Integer.TYPE,  new MethodEntity("Int", Integer.TYPE));
        javaType2MethodName.put(Boolean.class,  new MethodEntity("Boolean", Boolean.TYPE));
		javaType2MethodName.put(Boolean.TYPE,  new MethodEntity("Boolean", Boolean.TYPE));
		javaType2MethodName.put(Long.class,  new MethodEntity("Long", Long.TYPE));
		javaType2MethodName.put(Long.TYPE,  new MethodEntity("Long", Long.TYPE));
		javaType2MethodName.put(Float.class,  new MethodEntity("Float", Float.TYPE));
		javaType2MethodName.put(Float.TYPE,  new MethodEntity("Float", Float.TYPE));
		javaType2MethodName.put(Double.class,  new MethodEntity("Double", Double.TYPE));
		javaType2MethodName.put(Double.TYPE,  new MethodEntity("Double", Double.TYPE));
		javaType2MethodName.put(BYTE_ARRAY_CLASS,  new MethodEntity("Bytes", BYTE_ARRAY_CLASS));
		javaType2MethodName.put(Time.class,  new MethodEntity("Time", Time.class));
		javaType2MethodName.put(Date.class,  new MethodEntity("Date", Date.class));
		javaType2MethodName.put(Timestamp.class,  new MethodEntity("Timestamp", Timestamp.class));
		javaType2MethodName.put(String.class,  new MethodEntity("String", String.class));
		javaType2MethodName.put(BigDecimal.class,  new MethodEntity("BigDecimal", BigDecimal.class));
		javaType2MethodName.put(Object.class,  new MethodEntity("Object", Object.class));

	}
	
	static class MethodEntity {
	    public MethodEntity(String name, Class<?> clazz) {
            super();
            this.name = name;
            this.clazz = clazz;
        }
        String name;
	    Class<?> clazz;
	}
	
	
	private static void initMethods() {
		try {
			for (Map.Entry<Class<?>, MethodEntity> entry : javaType2MethodName.entrySet()) {
				Class<?> javaType = entry.getKey();
				MethodEntity methodEntity = entry.getValue();
				Method getter = resultSetClass.getMethod("get" + methodEntity.name, SINGLE_STRING_ARRAY_CLASS);
				Method setter = preparedStatementClass.getMethod("set" + methodEntity.name, new Class<?>[] {Integer.TYPE, methodEntity.clazz});
				javaType2Method.put(javaType, new MethodPair(setter, getter));
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static MethodPair getMethodPair(Class<?> clazz) {
		return javaType2Method.get(clazz);
	}
	
	public static Class<?> getJavaType(int sqlType) {
		return sqlType2JavaTypes.get(sqlType);
	}
	
	public static class MethodPair {
		
		public MethodPair(Method setter, Method getter) {
			this.setter = setter;
			this.getter = getter;
		}

		private Method setter;
		
		private Method getter;

		public Method getSetter() {
			return setter;
		}

		public void setSetter(Method setter) {
			this.setter = setter;
		}

		public Method getGetter() {
			return getter;
		}

		public void setGetter(Method getter) {
			this.getter = getter;
		}
		
		
	}

}
