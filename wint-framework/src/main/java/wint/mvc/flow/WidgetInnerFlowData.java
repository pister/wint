package wint.mvc.flow;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import wint.core.service.ServiceContext;
import wint.mvc.form.Form;
import wint.mvc.form.fileupload.UploadFile;
import wint.mvc.module.Module;
import wint.mvc.parameters.Arguments;
import wint.mvc.parameters.Parameters;
import wint.mvc.restful.request.RequestBody;
import wint.mvc.template.Context;
import wint.mvc.url.UrlBroker;

/**
 * @author pister
 * 2012-9-9 下午04:29:31
 */
@SuppressWarnings("deprecation")
public class WidgetInnerFlowData implements InnerFlowData {
	
	private InnerFlowData innerFlowData;
	
	private Module widgetModule;

	public WidgetInnerFlowData(InnerFlowData innerFlowData, Module widgetModule) {
		super();
		this.innerFlowData = innerFlowData;
		this.widgetModule = widgetModule;
	}

	public void commitData() {
		throw new UnsupportedOperationException();
	}

	public UrlBroker forkUrl(String urlModuleName, String target) {
		return innerFlowData.forkUrl(urlModuleName, target);
	}

	public void forwardTo(String target) {
		throw new UnsupportedOperationException();
	}

	public Arguments getArguments() {
		return innerFlowData.getArguments();
	}

	public Object getAttribute(String name) {
		return innerFlowData.getAttribute(name);
	}

	public String getContentType() {
		return getResponseContentType();
	}

	@Override
	public String getResponseContentType() {
		return innerFlowData.getResponseContentType();
	}

	public Form getForm(String name) {
		return innerFlowData.getForm(name);
	}

	public Context getInnerContext() {
		return innerFlowData.getInnerContext();
	}

	public String getLayout() {
		return innerFlowData.getLayout();
	}

	public Locale getLocale() {
		return innerFlowData.getLocale();
	}

	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException();
	}

	public Parameters getParameters() {
		return innerFlowData.getParameters();
	}

	@Override
	public RequestBody getRequestBody() {
		return innerFlowData.getRequestBody();
	}

	public ServiceContext getServiceContext() {
		return innerFlowData.getServiceContext();
	}

	public Session getSession() {
		return innerFlowData.getSession();
	}

	public int getStatusCode() {
		return innerFlowData.getStatusCode();
	}

	public String getTarget() {
		return innerFlowData.getTarget();
	}

	public String getViewType() {
		return innerFlowData.getViewType();
	}

	public Context getContext() {
		return innerFlowData.getContext();
	}

	public Writer getWriter() {
		throw new UnsupportedOperationException();
	}

	public boolean isForwardTo() {
		throw new UnsupportedOperationException();
	}

	public boolean isSendRedirected() {
		throw new UnsupportedOperationException();
	}

	public UrlBroker redirectTo(String urlModule, String target) {
		throw new UnsupportedOperationException();
	}

	public void redirectTo(String location) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	public void resetRedirected() {
		throw new UnsupportedOperationException();
	}

	public void setArguments(Arguments arguments) {
		innerFlowData.setArguments(arguments);
	}

	public void setAttribute(String name, Object value) {
		innerFlowData.setAttribute(name, value);
	}

	public void setContext(Context context) {
		innerFlowData.setContext(context);
	}

	public void setContentType(String contentType) {
		setResponseContentType(contentType);
	}

	@Override
	public void setResponseContentType(String contentType) {
		throw new UnsupportedOperationException();
	}

	public void setInnerContext(Context innerContext) {
		throw new UnsupportedOperationException();
	}

	public void setLayout(String layout) {
		throw new UnsupportedOperationException();
	}

	public void setLocale(Locale locale) {
		throw new UnsupportedOperationException();
	}

	public void setStatusCode(int code) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStatusCode(int code, String message) {
		throw new UnsupportedOperationException();
	}

	public void setTarget(String target) {
		throw new UnsupportedOperationException();
	}

	public void setViewType(String viewType) {
		throw new UnsupportedOperationException();
	}

	public Module getModule() {
		return widgetModule;
	}

	public void setModule(Module module) {
		this.widgetModule = module;
	}

    public String getRemoteAddr() {
        return innerFlowData.getRemoteAddr();
    }

    @Override
    public String getSuffix() {
        return innerFlowData.getSuffix();
    }

    @Override
    public void setParameters(Parameters parameters) {
        innerFlowData.setParameters(parameters);
    }

    @Override
    public String getRequestMethod() {
        return innerFlowData.getRequestMethod();
    }
}
