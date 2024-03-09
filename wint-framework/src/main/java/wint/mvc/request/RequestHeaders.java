package wint.mvc.request;

import java.time.LocalDate;
import java.util.List;

/**
 * @author songlihuang
 * @date 2024/3/9 14:42
 */
public interface RequestHeaders {

    /**
     * 获取第一个header数据
     * @param name
     * @return
     */
    String getString(String name);

    /**
     * 获取该name的所有数据
     * @param name
     * @return
     */
    List<String> getStringList(String name);

    LocalDate getDate(String name);

    Long getLong(String name);

    Integer getInteger(String name);

    List<String> getNames();


}
