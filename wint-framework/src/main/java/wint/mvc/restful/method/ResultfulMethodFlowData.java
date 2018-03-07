package wint.mvc.restful.method;

import wint.mvc.flow.FlowData;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by songlihuang on 2018/3/7.
 */
public interface ResultfulMethodFlowData extends FlowData {

    /**
     * 获取第一个header数据
     * @param name
     * @return
     */
    String getHeader(String name);

    Date getHeaderDate(String name);

    Long getHeaderLong(String name);

    /**
     * 获取该name的所有hander数据
     * @param name
     * @return
     */
    List<String> getHeaders(String name);

    /**
     * 获取所有header
     * @return
     */
    Map<String, List<String>> getAllHeaders();

}
