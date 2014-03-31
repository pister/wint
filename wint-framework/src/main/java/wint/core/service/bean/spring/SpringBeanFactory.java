package wint.core.service.bean.spring;

import wint.core.service.bean.BeanFactory;
import wint.core.service.supports.AbstractBeanFactory;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;

import java.util.Set;

/**
 * @author pister 2012-3-4 11:30:36
 */
public class SpringBeanFactory extends AbstractBeanFactory implements BeanFactory {

	private static final Class<?>[] CONTAINS_BEAN_ARG_TYPES = { String.class };

	private static final Class<?>[] GET_BEAN_ARG_TYPES = { String.class };

	private MagicObject applicationContext;

	public SpringBeanFactory(MagicObject applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	public Object getObject(String name) {
		Boolean contains = (Boolean) applicationContext.invoke("containsBean", CONTAINS_BEAN_ARG_TYPES, new String[] { name });
		if (contains == null || !contains.booleanValue()) {
			return null;
		}
		return applicationContext.invoke("getBean", GET_BEAN_ARG_TYPES, new String[] { name });
	}

}
