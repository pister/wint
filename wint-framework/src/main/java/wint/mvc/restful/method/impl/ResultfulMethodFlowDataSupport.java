package wint.mvc.restful.method.impl;

import wint.core.service.ServiceContext;
import wint.lang.utils.MapUtil;
import wint.lang.utils.NumberUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.ServletFlowData;
import wint.mvc.flow.Session;
import wint.mvc.form.Form;
import wint.mvc.form.fileupload.UploadFile;
import wint.mvc.parameters.Arguments;
import wint.mvc.parameters.Parameters;
import wint.mvc.restful.method.ResultfulMethodFlowData;
import wint.mvc.template.Context;
import wint.mvc.url.UrlBroker;

import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.io.Writer;
import java.util.*;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class ResultfulMethodFlowDataSupport implements ResultfulMethodFlowData {

    protected FlowData flowData;

    public ResultfulMethodFlowDataSupport(FlowData flowData) {
        this.flowData = flowData;
    }

    @Override
    public Object getAttribute(String name) {
        return flowData.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        flowData.setAttribute(name, value);
    }

    @Override
    public Parameters getParameters() {
        return flowData.getParameters();
    }

    @Override
    public String getTarget() {
        return flowData.getTarget();
    }

    @Override
    public void setTarget(String target) {
        flowData.setTarget(target);
    }

    @Override
    public void forwardTo(String target) {
        flowData.forwardTo(target);
    }

    @Override
    public void redirectTo(String location) {
        flowData.redirectTo(location);
    }

    @Override
    public UrlBroker redirectTo(String urlModule, String target) {
        return flowData.redirectTo(urlModule, target);
    }

    @Override
    public Writer getWriter() {
        return flowData.getWriter();
    }

    @Override
    public OutputStream getOutputStream() {
        return flowData.getOutputStream();
    }

    @Override
    public int getStatusCode() {
        return flowData.getStatusCode();
    }

    @Override
    public void setStatusCode(int code) {
        flowData.setStatusCode(code);
    }

    @Override
    public void setStatusCode(int code, String message) {
        flowData.setStatusCode(code, message);
    }

    @Override
    public void sendError(int code, String message) {
        flowData.sendError(code, message);
    }

    @Override
    public String getContentType() {
        return flowData.getContentType();
    }

    @Override
    public void setContentType(String contentType) {
        flowData.setContentType(contentType);
    }

    @Override
    public Locale getLocale() {
        return flowData.getLocale();
    }

    @Override
    public void setLocale(Locale locale) {
        flowData.setLocale(locale);
    }

    @Override
    public Form getForm(String name) {
        return flowData.getForm(name);
    }

    @Override
    public Arguments getArguments() {
        return flowData.getArguments();
    }

    @Override
    public String getViewType() {
        return flowData.getViewType();
    }

    @Override
    public void setViewType(String viewType) {
        flowData.setViewType(viewType);
    }

    @Override
    public Context getInnerContext() {
        return flowData.getInnerContext();
    }

    @Override
    public Context getContext() {
        return flowData.getContext();
    }

    @Override
    public ServiceContext getServiceContext() {
        return flowData.getServiceContext();
    }

    @Override
    public Session getSession() {
        return flowData.getSession();
    }

    @Override
    public UrlBroker forkUrl(String urlModuleName, String target) {
        return flowData.forkUrl(urlModuleName, target);
    }

    @Override
    public Map<String, UploadFile> getUploadFiles() {
        return flowData.getUploadFiles();
    }

    @Override
    public String getLayout() {
        return flowData.getLayout();
    }

    @Override
    public void setLayout(String layout) {
        flowData.setLayout(layout);
    }

    @Override
    public String getRemoteAddr() {
        return flowData.getRemoteAddr();
    }

    @Override
    public String getSuffix() {
        return flowData.getSuffix();
    }

    @Override
    public String getRequestMethod() {
        return flowData.getRequestMethod();
    }

    @Override
    public String getHeader(String name) {
        HttpServletRequest request = getServletRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader(name);
    }

    @Override
    public Date getHeaderDate(String name) {
        HttpServletRequest request = getServletRequest();
        if (request == null) {
            return null;
        }
        long v = request.getDateHeader(name);
        if (v < 0) {
            return null;
        }
        return new Date(v);
    }

    @Override
    public Long getHeaderLong(String name) {
        String value = getHeader(name);
        if (value == null) {
            return null;
        }
        if (NumberUtil.isNumeric(value)) {
            return Long.parseLong(value);
        } else {
            return null;
        }
    }

    protected HttpServletRequest getServletRequest() {
        if (flowData instanceof ServletFlowData) {
            ServletFlowData servletFlowData = (ServletFlowData) flowData;
            return servletFlowData.getRequest();
        } else {
            return null;
        }
    }

    @Override
    public List<String> getHeaders(String name) {
        HttpServletRequest request = getServletRequest();
        if (request == null) {
            return null;
        }
        Enumeration<String> values = request.getHeaders(name);
        if (values == null) {
            return null;
        }
        return Collections.list(values);
    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        HttpServletRequest request = getServletRequest();
        if (request == null) {
            return null;
        }
        Map<String, List<String>> headers = MapUtil.newHashMap();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            if (values == null) {
                continue;
            }
            headers.put(name, Collections.list(values));
        }
        return headers;
    }
}
