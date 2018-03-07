package wint.mvc.restful.method.impl;

import wint.lang.io.FastByteArrayOutputStream;
import wint.lang.utils.IoUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.BodySupportMethodFlowData;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class BodySupportMethodFlowDataSupport extends ResultfulMethodFlowDataSupport implements BodySupportMethodFlowData {

    public BodySupportMethodFlowDataSupport(FlowData flowData) {
        super(flowData);
    }

    @Override
    public InputStream getRequestBody() throws IOException {
        HttpServletRequest request = getServletRequest();
        if (request == null) {
            return null;
        }
        return request.getInputStream();
    }

    @Override
    public byte[] getRequestBodyAsData() {
        try {
            InputStream inputStream = getRequestBody();
            if (inputStream == null) {
                return null;
            }
            FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
            IoUtil.copyAndClose(inputStream, bos);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getRequestBodyAsString(String charset) throws UnsupportedEncodingException {
        byte[] body = getRequestBodyAsData();
        if (body == null) {
            return null;
        }
        return new String(body, charset);
    }

    private static final String DEFAULT_CHARSET = "utf-8";

    private String parseCharsetFromRequest(String defaultCharset) {
        String contentType = getHeader("Content-Type");
        if (StringUtil.isEmpty(contentType)) {
            return defaultCharset;
        }
        List<String> parts = StringUtil.splitTrim(contentType, ";");
        for (String part : parts) {
            int pos = part.indexOf('=');
            if (pos < 0) {
                continue;
            }
            String left = StringUtil.trimToEmpty(part.substring(0, pos));
            String right = StringUtil.trimToEmpty(part.substring(pos+ 1));
            if ("charset".equalsIgnoreCase(left)) {
                return right;
            }
        }
        return defaultCharset;
    }

    @Override
    public String getRequestBodyAsString() {
        try {
            return getRequestBodyAsString(parseCharsetFromRequest(DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
