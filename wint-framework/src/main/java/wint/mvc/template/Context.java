package wint.mvc.template;

import java.util.Map;

public interface Context {

	void put(String name, Object value);
	
	void putAll(Map<String, Object> map);
	
	void putContext(Context context);
	
	Object get(String name);
	
	Map<String, Object> getAll();
	
	Context copyMe();
	
}
