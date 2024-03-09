package wint.mvc.request;

import wint.help.json.wrapper.JsonList;
import wint.help.json.wrapper.JsonObject;

import java.util.List;

/**
 * @author songlihuang
 * @date 2024/3/7 10:12
 */
public interface RequestBody {

    /**
     * 获取请求原始数据
     * @return
     */
    byte[] getData();

    /**
     * 获取请求的数据作为字符串
     * @return
     */
    String getString();

    /**
     * 把请求的数据解析成json结构数据
     * @return
     */
    JsonObject getJsonObject();

    /**
     * 把请求数据映射成指定类型的对象数据
     * @param theType
     * @return
     * @param <T>
     */
    <T> T getJsonObject(Class<T> theType);

    /**
     * 把请求的数据解析成json列表(数组)
     * @return
     */
    JsonList getJsonList();

    /**
     * 把请求数据映射成指定类型的对象列表(数组)
     * @param elementType
     * @return
     * @param <T>
     */
    <T> List<T> getJsonList(Class<T> elementType);
}
