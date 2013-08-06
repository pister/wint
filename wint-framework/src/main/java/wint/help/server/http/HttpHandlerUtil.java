package wint.help.server.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;

import wint.lang.magic.MagicObject;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.IoUtil;

/**
 * 创建一个com.sun.net.httpserver.HttpHandler的handle，不直接引用类的目的是不想直接依赖非标准的接口
 * @author pister
 * 2012-4-9 下午10:31:08
 */
public class HttpHandlerUtil {
	
	public static Object createHttpHandler(final File filePath, final String contentType) {
		Class<?>[] interfaces = new Class<?>[]{ClassUtil.forName("com.sun.net.httpserver.HttpHandler")};
		return Proxy.newProxyInstance(ClassUtil.getClassLoader(), interfaces, new InvocationHandler() {

			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (method.getName().equals("handle")) {
					MagicObject httpExchange = MagicObject.wrap(args[0]);
					try {
						URI uri = (URI)httpExchange.invoke("getRequestURI");
						String file = uri.getPath();
						File f = new File(filePath, file);
						if (!f.exists()) {
							httpExchange.invoke("sendResponseHeaders", new Class<?>[] {Integer.TYPE, Long.TYPE}, new Object[] {404, 0});
							return null;
						}
						httpExchange.invoke("sendResponseHeaders", new Class<?>[] {Integer.TYPE, Long.TYPE}, new Object[] {200, f.length()});
						FileInputStream fis = new FileInputStream(f);
						MagicObject.wrap(httpExchange.invoke("getResponseHeaders")).invoke("add", new Class<?>[] {String.class, String.class}, new Object[] {"content-type", contentType});
						IoUtil.copy(fis, (OutputStream)httpExchange.invoke("getResponseBody"));
					} catch(Throwable e) {
						throw e;
					} finally {
						httpExchange.invoke("close");
					}
				}
				return null;
			}
			
		});
	}

}
