package wint.mvc.restful.method.impl;

import wint.core.service.ServiceContext;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.ServletFlowData;
import wint.mvc.flow.Session;
import wint.mvc.form.Form;
import wint.mvc.parameters.Arguments;
import wint.mvc.parameters.Parameters;
import wint.mvc.request.RequestBody;
import wint.mvc.request.RequestHeaders;
import wint.mvc.restful.method.ResultfulFlowData;
import wint.mvc.template.Context;
import wint.mvc.url.UrlBroker;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Locale;

/**
 * Created by songlihuang on 2018/3/7.
 */
@SuppressWarnings("ALL")
public class ResultfulFlowDataSupport implements ResultfulFlowData {

    protected FlowData flowData;

    public ResultfulFlowDataSupport(FlowData flowData) {
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
    public RequestBody getRequestBody() {
        return flowData.getRequestBody();
    }

    @Override
    public RequestHeaders getRequestHeaders() {
        return flowData.getRequestHeaders();
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
    public InputStream getInputStream() throws IOException {
        return flowData.getInputStream();
    }

    @Override
    public UrlBroker redirectTo(String urlModule, String target) {
        return flowData.redirectTo(urlModule, target);
    }

    @Override
    public Writer getWriter() throws IOException {
        return flowData.getWriter();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return flowData.getOutputStream();
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
    public void setErrorCode(int code, String message) {
        flowData.setErrorCode(code, message);
    }

    @Override
    public String getContentType() {
        return getResponseContentType();
    }

    @Override
    public String getResponseContentType() {
        return flowData.getResponseContentType();
    }

    @Override
    public void setContentType(String contentType) {
        setResponseContentType(contentType);
    }

    @Override
    public void setResponseContentType(String contentType) {
        flowData.setResponseContentType(contentType);
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

    protected HttpServletRequest getServletRequest() {
        if (flowData instanceof ServletFlowData) {
            ServletFlowData servletFlowData = (ServletFlowData) flowData;
            return servletFlowData.getRequest();
        } else {
            return null;
        }
    }

}
