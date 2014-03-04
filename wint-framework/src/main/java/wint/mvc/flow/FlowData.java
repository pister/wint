package wint.mvc.flow;

import wint.core.service.ServiceContext;
import wint.mvc.form.Form;
import wint.mvc.form.fileupload.UploadFile;
import wint.mvc.parameters.Arguments;
import wint.mvc.parameters.Parameters;
import wint.mvc.template.Context;
import wint.mvc.url.UrlBroker;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

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
     * 获取http的queryString的参数
     *
     * @return
     */
    Parameters getParameters();

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
     * 外部重定向到一个url module
     *
     * @param urlModule
     * @param target
     * @return
     */
    UrlBroker redirectTo(String urlModule, String target);

    /**
     * 一旦获取输出的Writer，wint会在内部调用setViewType(NopViewRender.TYPE_NAME)
     *
     * @return
     */
    Writer getWriter();

    /**
     * 一旦获取输出的OuputStream，wint会在内部调用setViewType(NopViewRender.TYPE_NAME)
     *
     * @return
     */
    OutputStream getOutputStream();

    int getStatusCode();

    /**
     * 设置http的输出状态值
     *
     * @param code
     */
    void setStatusCode(int code);

    String getContentType();

    /**
     * 设置http的输出contentType，默认为text/html
     *
     * @param contentType
     */
    void setContentType(String contentType);

    /**
     * 获取本地对象
     * @return
     */
    Locale getLocale();

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
     * 获取上传文件
     * @return
     * @deprecated use getParameter().getUploadFile() instead
     */
    Map<String, UploadFile> getUploadFiles();

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

}
