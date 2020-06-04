package wint.help.json.wrapper;

import java.util.Date;

/**
 * Created by songlihuang on 2020/6/4.
 */
public interface JsonObject {

    int getIntValue(String name);

    Integer getInteger(String name);

    long getLongValue(String name);

    Long getLong(String name);

    String getString(String name);

    boolean getBooleanValue(String name);

    Boolean getBoolean(String name);

    Date getDate(String name);

    JsonObject getObject(String name);

    JsonList getList(String name);

}
