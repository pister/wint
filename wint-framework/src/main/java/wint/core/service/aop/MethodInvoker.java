package wint.core.service.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import wint.core.service.aop.ProxyInterceptorUtil.Invoker;

public class MethodInvoker implements Invoker {
	
	private Method method;

	public MethodInvoker(Method method) {
		super();
		this.method = method;
	}

	public String getName() {
		return method.getName();
	}

	public Object invoke(Object obj, Object[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return method.invoke(obj, args);
	}

}
