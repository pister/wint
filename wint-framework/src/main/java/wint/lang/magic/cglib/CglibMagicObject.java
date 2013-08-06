package wint.lang.magic.cglib;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import wint.core.service.aop.CglibMethodProxyInvoker;
import wint.core.service.aop.ProxyInterceptorUtil;
import wint.lang.magic.Interceptor;
import wint.lang.magic.MagicObject;

public class CglibMagicObject extends MagicObject {

	public CglibMagicObject(Object targetObject) {
		super(targetObject);
	}
	
	@Override
	public MagicObject asProxyObject(final List<? extends Interceptor> interceptors) {
		Class<?> clazz = targetObject.getClass();
		if (Modifier.isFinal(clazz.getModifiers())) {
			return this;
		}
		Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new CglibMethodInterceptor(interceptors, targetObject));
        Object proxyObject = enhancer.create();
        return new CglibMagicObject(proxyObject);
	}
	
	public static class CglibMethodInterceptor implements MethodInterceptor  {
		
		private List<? extends Interceptor> interceptors;
		
		private Object targetObject;

		public CglibMethodInterceptor(List<? extends Interceptor> interceptors, Object targetObject) {
			super();
			this.interceptors = interceptors;
			this.targetObject = targetObject;
		}

		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			return ProxyInterceptorUtil.invokeByInterceptors(interceptors, targetObject, new CglibMethodProxyInvoker(proxy, method.getName()), args);
		}

	}
	
}
