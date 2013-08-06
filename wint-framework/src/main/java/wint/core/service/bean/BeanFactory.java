package wint.core.service.bean;

import java.util.Set;

import wint.lang.magic.Property;

/**
 * @author pister 2012-3-4 03:48:02
 */
public interface BeanFactory {
	
	Object getObject(String name);
	
	Set<Property> injectProperties(Object target);

}
