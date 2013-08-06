package wint.core.service.aop;

import wint.lang.magic.Interceptor;
import wint.lang.magic.MagicList;

public class ProxyInterceptors {

	static MagicList<? extends Interceptor> interceptors = MagicList.wrap(new ProfilerInterceptor());
	
	public static MagicList<? extends Interceptor> getInterceptors() {
		return interceptors;
	}

	
}
