package wint.mvc.restful.method;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by songlihuang on 2018/3/7.
 */
public interface BodySupportMethodFlowData extends ResultfulMethodFlowData {

    InputStream getRequestBody() throws IOException;

    byte[] getRequestBodyAsData();

    /**
     * 把请求中数据转成字符串
     * @param charset 指定字符集
     * @return
     * @throws UnsupportedEncodingException
     */
    String getRequestBodyAsString(String charset) throws UnsupportedEncodingException;


    /**
     * 把请求中数据转成字符串, 字符串从request.getHeader("Content-Type")中解析，如果获取不到字符串，
     * 则采用utf-8字符集
     * @return
     */
    String getRequestBodyAsString();

}
