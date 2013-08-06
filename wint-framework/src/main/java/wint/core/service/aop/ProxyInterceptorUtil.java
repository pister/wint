package wint.core.service.aop;

import java.util.Iterator;
import java.util.List;

import wint.lang.magic.Interceptor;

public class ProxyInterceptorUtil {
	
	public static Object invokeByInterceptors(List<? extends Interceptor> interceptors, Object obj, Invoker invoker, Object[] args) throws Throwable {
		Iterator<? extends Interceptor> it = interceptors.iterator();
		if (!it.hasNext()) {
			return invoker.invoke(obj, args);
		}
		return invokeByInterceptors(obj, invoker, args, it);
	}
	
	private static Object invokeByInterceptors(Object obj, Invoker invoker, Object[] args, Iterator<? extends Interceptor> it) throws Throwable {
		Object ret = it.next().invoke(obj, invoker, args);
		if (!it.hasNext()) {
			return ret;
		}
		return invokeByInterceptors(obj, invoker, args, it);
	}
	
	public static interface Invoker {
		
		String getName();
		
		Object invoke(Object obj, Object[] args) throws Throwable;
		
	}
	

}
