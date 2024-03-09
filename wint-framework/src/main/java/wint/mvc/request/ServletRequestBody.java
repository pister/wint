package wint.mvc.request;

import wint.core.config.Constants;
import wint.help.json.JsonUtil;
import wint.help.json.wrapper.JsonList;
import wint.help.json.wrapper.JsonObject;
import wint.lang.utils.IoUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author songlihuang
 * @date 2024/3/7 10:13
 */
public class ServletRequestBody implements RequestBody {

    private byte[] requestData;

    private String charset;

    private HttpServletRequest httpServletRequest;

    private boolean hasInit = false;

    public ServletRequestBody(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    private void ensureInit() {
        if (hasInit) {
            return;
        }
        charset = parseCharset(httpServletRequest.getContentType());
        try {
            InputStream inputStream = httpServletRequest.getInputStream();
            requestData = IoUtil.copyAllToBytes(inputStream);
            IoUtil.close(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hasInit = true;
    }

    private static String parseCharset(String contentTypeHeader) {
        String defaultCharset = Constants.Defaults.CHARSET_ENCODING;
        String charsetPrefix = "charset=";
        if (contentTypeHeader == null || !contentTypeHeader.contains(charsetPrefix)) {
            return defaultCharset;
        }
        int start = contentTypeHeader.indexOf(charsetPrefix) + charsetPrefix.length();
        String charset = contentTypeHeader.substring(start).trim();
        if (charset.isEmpty()) {
            return defaultCharset;
        }
        return charset;
    }

    @Override
    public byte[] getData() {
        ensureInit();
        return requestData;
    }

    @Override
    public String getString() {
        try {
            return new String(getData(), charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonObject getJsonObject() {
        String bodyString = getString();
        return JsonUtil.fromJsonStringObject(bodyString);
    }

    @Override
    public <T> T getJsonObject(Class<T> theType) {
        String bodyString = getString();
        return JsonUtil.fromJsonString(bodyString, theType);
    }

    @Override
    public JsonList getJsonList() {
        String bodyString = getString();
        return JsonUtil.fromJsonStringList(bodyString);
    }

    @Override
    public <T> List<T> getJsonList(Class<T> elementType) {
        String bodyString = getString();
        return JsonUtil.fromJsonStringList(bodyString, elementType);
    }
}
