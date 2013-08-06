package wint.core.service.util;

import java.util.Set;

import wint.core.service.Service;
import wint.lang.exceptions.ServiceInitialException;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

public class ServiceUtil {
	
	public static void ensureServiceName(String name) {
		if (StringUtil.isEmpty(name)) {
			return;
		}
		if (Character.isUpperCase(name.charAt(0))) {
			throw new IllegalArgumentException("the first letter of object name must be UpperCase, but input is: " + name);
		}
	}
	
	public static String getSerivceName(Class<?> serviceClass) {
		String name = getSerivceNameImpl(serviceClass);
		if (StringUtil.isEmpty(name)) {
			return name;
		}
		return StringUtil.lowercaseFirstLetter(name);
	}
	
	private static String getSerivceNameImpl(Class<?> serviceClass) {
		if (serviceClass == null) {
			return null;
		}
		if (serviceClass.isInterface()) {
			return ClassUtil.getShortClassName(serviceClass);
		}
		Set<String> ret = CollectionUtil.newHashSet();
		Set<Class<?>> interfaces = ClassUtil.getAllInterfaces(serviceClass);
		for (Class<?> clazz : interfaces) {
			if (clazz.equals(Service.class)) {
				continue;
			}
			if (clazz.getName().endsWith("Service") && Service.class.isAssignableFrom(clazz)) {
				ret.add(ClassUtil.getShortClassName(clazz));
			}
		}
		if (CollectionUtil.isEmpty(ret)) {
			throw new ServiceInitialException("the service name must end with \"Service\" for class:" + serviceClass);
		}
		if (ret.size() > 1) {
			throw new ServiceInitialException("the service class must implement ONE Service interface!:" + serviceClass);
		}
		return ret.iterator().next();
	}
	

}
