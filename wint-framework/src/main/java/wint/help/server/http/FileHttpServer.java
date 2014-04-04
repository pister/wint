package wint.help.server.http;

import wint.lang.magic.MagicMethod;
import wint.lang.magic.reflect.ReflectMagicClass;
import wint.lang.magic.reflect.ReflectMagicObject;
import wint.lang.utils.ClassUtil;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 一个简单的文件服务器，可以用于开发或是单元测试
 * 不直接引用类的目的是不想直接依赖非标准的接口
 *
 * @author pister
 *         2012-4-9 下午10:32:05
 */
public class FileHttpServer {

    private ReflectMagicObject httpServer;

    private int backlog = 5;

    private String contentType = "image/jpeg";

    public FileHttpServer(int port, String path, final File filePath) throws IOException {
        ReflectMagicClass httpServerClass = new ReflectMagicClass(ClassUtil.forName("com.sun.net.httpserver.HttpServer"));
        MagicMethod createMethod = httpServerClass.getMethod("create", new Class<?>[]{InetSocketAddress.class, Integer.TYPE});
        httpServer = new ReflectMagicObject(createMethod.invoke(httpServerClass.getTargetClass(), new Object[]{new InetSocketAddress(port), backlog}));

        addFileContext(path, filePath);
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void start() {
        httpServer.invoke("start");
    }

    public void stop() {
        httpServer.invoke("start", new Class<?>[]{Integer.TYPE}, new Object[]{0});
    }

    private void addFileContext(String path, final File filePath) {
        Object httpHandler = HttpHandlerUtil.createHttpHandler(filePath, contentType);
        httpServer.invoke("createContext", new Class<?>[]{String.class, ClassUtil.forName("com.sun.net.httpserver.HttpHandler")}, new Object[]{path, httpHandler});

    }

}
