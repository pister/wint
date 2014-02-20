package wint.lang.magic;

import wint.core.service.aop.ProxyInterceptorUtil.Invoker;

/**
 * 代理对象的拦截器
 * @author pister 2012-1-2 08:49:46
 */
public interface Interceptor {

	Object invoke(Object target, Invoker invoker, Object[] argments) throws Throwable;

}
