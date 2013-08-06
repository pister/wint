package wint.mvc.view.types.json;

import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import wint.lang.WintException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.template.Context;

public class DefaultJsonRender implements JsonRender {

	static final Class<?>[] JSON_OBJECT_CONSTRUCTOR_MAP_TYPES = new Class<?>[] { Map.class };
	
	static final Class<?>[] JSON_OBJECT_CONSTRUCTOR_STRING_TYPES = new Class<?>[] { String.class };
	
	static final Class<?>[] JSON_OBJECT_CONSTRUCTOR_OBJECT_TYPES = new Class<?>[] { Object.class };
	
	static final Class<?>[] JSON_OBJECT_WRITE_TYPES = new Class<?>[] { Writer.class };
	
	static final Class<?>[] JSON_OBJECT_PUT_TYPES = new Class<?>[] { String.class, Object.class };
	

	public void render(Context context, Writer writer) {
		try {
			MagicClass jsonObjectClass = MagicClass.forName("org.json.JSONObject");
			MagicObject jsonObject = jsonObjectClass.newInstance();
			for (Map.Entry<String, Object> entry : context.getAll().entrySet()) {
				String name = entry.getKey();
				Object obj = entry.getValue();
				if (obj == null) {
					continue;
				}
				Object json =  tryToJSONObject(obj);
				if (json == null) {
					continue;
				}
				jsonObject.invoke("put", JSON_OBJECT_PUT_TYPES, new Object[] {name, json});
			}
			jsonObject.invoke("write", JSON_OBJECT_WRITE_TYPES, new Object[] { writer });
		} catch (Exception e) {
			throw new WintException(e);
		}
	}
	
	private boolean isJsonLike(Class<?> clazz) {
		return clazz.getName().startsWith("org.json");
	}
	
	private Map<String, Object> getObjectPropertiesMap(Object obj) {
		if (obj == null) {
			return null;
		}
		MagicObject o = MagicObject.wrap(obj);
		Map<String, Property> properties = o.getMagicClass().getProperties();
		if (MapUtil.isEmpty(properties)) {
			return MapUtil.newHashMap();
		}
		Map<String, Object> ret = MapUtil.newHashMap();
		for (Map.Entry<String, Property> entry : properties.entrySet()) {
			Property property = entry.getValue();
			if (!property.isReadable()) {
				continue;
			}
			if (!property.isWritable()) {
				continue;
			}
			String name = entry.getKey();
			Object propertyValue = property.getValue(obj);
			if (propertyValue == null) {
				continue;
			}
			Object jsonPropertyValue = tryToJSONObject(propertyValue);
			if (jsonPropertyValue == null) {
				continue;
			}
			ret.put(name, jsonPropertyValue);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	protected Object tryToJSONObject(Object obj) {
		if (obj == null) {
			return null;
		}
		Class<?> clazz = obj.getClass();
		if (ClassUtil.isSimpleType(clazz)) {
			return obj;
		}
		if (obj instanceof String) {
			return obj;
		}
		if (clazz.isArray()) {
			return obj;
		}
		if (obj instanceof Collection) {
			return obj;
		}
		if (obj instanceof Map) {
			return obj;
		}
		if (obj instanceof Date) {
			return obj;
		}
		if (isJsonLike(clazz)) {
			return obj;
		}
		Map<String, Object> propertiesMap = getObjectPropertiesMap(obj);
		return propertiesMap;
	}

}
