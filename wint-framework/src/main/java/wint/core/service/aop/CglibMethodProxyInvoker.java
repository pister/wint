package wint.core.service.aop;

import net.sf.cglib.proxy.MethodProxy;
import wint.core.service.aop.ProxyInterceptorUtil.Invoker;

public class CglibMethodProxyInvoker implements Invoker {

	private MethodProxy methodProxy;
	
	private String name;
	
	public CglibMethodProxyInvoker(MethodProxy methodProxy, String name) {
		super();
		this.methodProxy = methodProxy;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object invoke(Object obj, Object[] args) throws Throwable {
		return methodProxy.invoke(obj, args);
	}

}
