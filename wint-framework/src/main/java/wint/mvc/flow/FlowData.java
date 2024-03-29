package wint.mvc.flow;

import wint.core.service.ServiceContext;
import wint.mvc.form.Form;
import wint.mvc.parameters.Arguments;
import wint.mvc.parameters.Parameters;
import wint.mvc.request.RequestBody;
import wint.mvc.request.RequestHeaders;
import wint.mvc.template.Context;
import wint.mvc.url.UrlBroker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Locale;

/**
 * 请求数据流对象
 * @author pister 2011-12-29 04:08:04
 */
public interface FlowData {

    /**
     * 获取flowData属性
     *
     * @param name
     * @return
     */
    Object getAttribute(String name);

    /**
     * 设置flowData属性
     *
     * @param name
     * @param value
     */
    void setAttribute(String name, Object value);

    /**
     * 获取http的queryString和表单的参数
     * 注意，尽量避免和 getRequestBody()同时时候
     * @return
     */
    Parameters getParameters();

    /**
     * 获取http请求的body数据，支持requestBody的http方法有POST/PUT/PATCH。
     *
     * 注意，尽量避免和 getParameters()同时时候
     *
     * @return
     */
    RequestBody getRequestBody();

    /**
     * 获取请求头
     * @return
     */
    RequestHeaders getRequestHeaders();

    /**
     * 获取action目标
     *
     * @return
     */
    String getTarget();

    /**
     * 在执行action前调用：则设置目标action；
     * 在执行了action内或是后调用：如果是doAction，相当于内部重定向，会执行目标和渲染模板，否则只设置渲染目标。
     *
     * @param target
     */
    void setTarget(String target);

    /**
     * 内部重定向
     *
     * @param target
     */
    void forwardTo(String target);

    /**
     * 外部重定向到一个location status code: 302
     *
     * @param location
     */
    void redirectTo(String location);

    /**
     * 获取请求body的数据流
     * 1.6.8版本 开始废弃，使用getRequestBody()代替
     * @return
     * @throws IOException
     * @deprecated use getRequestBody() instead
     */
    InputStream getInputStream() throws IOException;

    /**
     * 外部重定向到一个url module
     *
     * @param urlModule
     * @param target
     * @return
     */
    UrlBroker redirectTo(String urlModule, String target);

    /**
     * 一旦获取输出的Writer，wint会在内部调用setViewType("nop")
     *
     * @return
     */
    Writer getWriter() throws IOException;

    /**
     * 一旦获取输出的OutputStream，wint会在内部调用setViewType("nop")
     *
     * @return
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * 设置http的输出状态值
     *
     * @param code
     */
    void setStatusCode(int code);

    /**
     * 设置错误状态和错误信息
     * @param code
     * @param message
     * @deprecated use setErrorCode() instead
     */
    void setStatusCode(int code, String message);

    /**
     * 设置响应错误状态码和错误信息
     * @param code
     * @param message
     */
    void setErrorCode(int code, String message);

    /**
     * @deprecated use getResponseContentType instead of
     * @return response的content-type
     */
    String getContentType();

    /**
     * @return response的content-type
     */
    String getResponseContentType();

    /**
     * 设置http的输出contentType，默认为text/html
     *
     * @param contentType
     * @deprecated use setResponseContentType instead
     */
    void setContentType(String contentType);


    /**
     * 设置http的输出contentType，默认为text/html
     *
     * @param contentType
     */
    void setResponseContentType(String contentType);

    /**
     * 获取本地对象
     * @return
     */
    Locale getLocale();

    /**
     * @deprecated 为了版本将不再提供
     * @param locale
     */
    void setLocale(Locale locale);

    /**
     * 根据名称获取表单，参见form.xml的配置
     *
     * @param name
     * @return
     */
    Form getForm(String name);

    /**
     * 获取执行的方法参数，目的是提供一种另外的途径可以不用在方法参数中获取 <br />
     * http://127.0.0.1/hello/123-456-abc.htm
     * 返回 ["123", "456", "abc"] 类似的参数
     * @return
     */
    Arguments getArguments();


    /**
     * 获取视图类型
     * @return
     */
    String getViewType();

    /**
     * 设置视图类型，参考 ViewTypes
     * @param viewType
     */
    void setViewType(String viewType);

    Context getInnerContext();

    Context getContext();

    ServiceContext getServiceContext();

    /**
     * 获取session对象
     * @return
     */
    Session getSession();

    /**
     * 创建一个url对象
     * @param urlModuleName
     * @param target
     * @return
     */
    UrlBroker forkUrl(String urlModuleName, String target);

    /**
     * 获取layout
     * @return
     */
    String getLayout();

    /**
     * 设置layout
     * @param layout
     */
    void setLayout(String layout);

    /**
     * 获取ip地址
     * @return
     */
    String getRemoteAddr();

    /**
     * 获取url后缀
     * @return
     */
    String getSuffix();

    /**
     * 获取请求方法： post, get, etc.
     * @return
     */
    String getRequestMethod();
}
