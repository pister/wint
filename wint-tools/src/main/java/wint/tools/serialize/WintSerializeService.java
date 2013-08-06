package wint.tools.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import wint.tools.util.CollectionUtil;

/**
 * 2012-8-23 下午1:19:39
 */
public class WintSerializeService extends AbstractSerializeService implements SerializeService {

	private SerializeService objectSerializeService;
	
	private Map<Integer, SimpleTypeSerialize> typeValue2Type = CollectionUtil.newHashMap();
	
	private Map<Class<?>, SimpleTypeSerialize> type2TypeValue = CollectionUtil.newHashMap();
	
	private static final int NULL_TYPE = 1;
	private static final int OBJECT_TYPE = 2;
	
	public WintSerializeService() {
		this(new JavaSerializeService());
	}

    public WintSerializeService(SerializeService objectSerializeService) {
        this.objectSerializeService = objectSerializeService;
        init();
    }

	private void init() {
		List<SimpleTypeSerialize> simpleTypeSerializes = CollectionUtil.newArrayList();
		// 1 for null
		// 2 for object
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(3, Byte.class, "Byte", Integer.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(4, Byte.TYPE, "Byte", Integer.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(5, Character.class, "Char", Integer.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(6, Character.TYPE, "Char", Integer.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(7, Short.class, "Short", Integer.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(8, Short.TYPE, "Short", Integer.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(9, Integer.class, "Int", Integer.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(10, Integer.TYPE, "Int", Integer.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(11, Long.class, "Long", Long.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(12, Long.TYPE, "Long", Long.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(13, Float.class, "Float", Float.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(14, Float.TYPE, "Float", Float.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(15, Double.class, "Double", Double.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(16, Double.TYPE, "Double", Double.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(17, Boolean.class, "Boolean", Boolean.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(18, Boolean.TYPE, "Boolean", Boolean.TYPE));
		simpleTypeSerializes.add( new NamedSimpleTypeSerialize(19, String.class, "UTF", String.class));
		
		for (SimpleTypeSerialize simpleTypeSerialize : simpleTypeSerializes) {
			type2TypeValue.put(simpleTypeSerialize.getType(), simpleTypeSerialize);
			typeValue2Type.put(simpleTypeSerialize.getTypeValue(), simpleTypeSerialize);
		}
		
	}
	
	public void serialize(Object o, OutputStream os) {
		try {
			DataOutputStream dos = new DataOutputStream(os);
			if (o == null) {
				dos.writeByte(NULL_TYPE);
				return;
			}
			Class<?> clazz = o.getClass();
			SimpleTypeSerialize typeSerialize = type2TypeValue.get(clazz);
			if (typeSerialize == null) {
				// 普通java对象
				dos.writeByte(OBJECT_TYPE);
				objectSerializeService.serialize(o, dos);
				dos.flush();
				return;
			}
			
			// 简单对象
			dos.writeByte(typeSerialize.getTypeValue());
			typeSerialize.write(dos, o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object deserialize(InputStream is) {
		try {
			DataInputStream dis = new DataInputStream(is);
			byte type = dis.readByte();
			if (type == NULL_TYPE) {
				return null;
			}
			SimpleTypeSerialize simpleTypeSerialize = getSimpleTypeSerialize((int)type);
			if (simpleTypeSerialize == null) {
				return objectSerializeService.deserialize(dis);
			}
			return simpleTypeSerialize.read(dis);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected SimpleTypeSerialize getSimpleTypeSerialize(int type) {
		return typeValue2Type.get(type);
	}
	
	static abstract class SimpleTypeSerialize {
		public abstract Object read(DataInputStream dis) throws IOException;
		public abstract void write(DataOutputStream dos, Object object) throws IOException;
		public abstract Class<?> getType();
		public abstract int getTypeValue();
	}

	static class NamedSimpleTypeSerialize extends SimpleTypeSerialize {

		private Class<?> clazz;
		
		private Class<?> typeInMethod;
		
		private String name;
		
		private Method readMethod;
		
		private Method writeMethod;
		
		private int typeValue;
		
		public NamedSimpleTypeSerialize(int typeValue, Class<?> clazz, String name, Class<?> typeInMethod) {
			super();
			this.clazz = clazz;
			this.name = name;
			this.typeInMethod = typeInMethod;
			this.typeValue = typeValue;
			
			initMethods();
		}
		
		private void initMethods() {
			String readMethodName = "read" + name;
			String writeMethodName = "write" + name;
			try {
				readMethod = DataInputStream.class.getMethod(readMethodName);
				writeMethod = DataOutputStream.class.getMethod(writeMethodName, new Class<?>[]{ typeInMethod });
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public Object read(DataInputStream dis) throws IOException {
			try {
				return readMethod.invoke(dis);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				Throwable targetException = e.getTargetException();
				if (targetException instanceof IOException) {
					throw (IOException)targetException;
				}
				throw new RuntimeException(targetException);
			}
		}

		public void write(DataOutputStream dos, Object object) throws IOException {
			try {
				writeMethod.invoke(dos, object);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				Throwable targetException = e.getTargetException();
				if (targetException instanceof IOException) {
					throw (IOException)targetException;
				}
				throw new RuntimeException(targetException);
			}
		}

		public Class<?> getType() {
			return clazz;
		}

		public int getTypeValue() {
			return typeValue;
		}
		
	}
	
}
