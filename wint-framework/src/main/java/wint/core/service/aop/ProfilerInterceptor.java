package wint.core.service.aop;

import wint.core.service.aop.ProxyInterceptorUtil.Invoker;
import wint.lang.magic.Interceptor;
import wint.lang.misc.profiler.Profiler;

public class ProfilerInterceptor implements Interceptor {

	public Object invoke(Object target, Invoker invoker, Object[] arguments) throws Throwable {
		Object ret = null;
		try {
			Profiler.enter(invoker.getName() + " at " + target.getClass().getSimpleName());
			ret = invoker.invoke(target, arguments);
		} finally {
			Profiler.release();
		}
		return ret;
	}

}
