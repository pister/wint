package wint.lang.magic.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;

import wint.core.service.aop.MethodInvoker;
import wint.core.service.aop.ProxyInterceptorUtil;
import wint.lang.magic.Interceptor;
import wint.lang.magic.MagicObject;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.CollectionUtil;

/**
 * @author pister 2011-12-22 10:05:44
 */
public class ReflectMagicObject extends MagicObject {

	public ReflectMagicObject(Object targetObject) {
		super(targetObject, new ReflectMagicClass(targetObject.getClass()));
	}
	
	@Override
	public MagicObject asProxyObject(final List<? extends Interceptor> interceptors) {
		if (CollectionUtil.isEmpty(interceptors)) {
			return this;
		}
		Set<Class<?>> interfaces = ClassUtil.getAllInterfaces(magicClass.getTargetClass());
		if (CollectionUtil.isEmpty(interfaces)) {
			return this;
		}
		
		return new ReflectMagicObject(Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), interfaces.toArray(new Class<?>[0]), new InvocationHandler() {

			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return ProxyInterceptorUtil.invokeByInterceptors(interceptors, targetObject, new MethodInvoker(method), args);
			}
		}));
	}

}
