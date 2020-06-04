package wint.help.json.wrapper;

import java.util.Date;
import java.util.List;

/**
 * Created by songlihuang on 2020/6/4.
 */
public interface JsonList extends List<Object> {

    int getIntValue(int index);

    Integer getInteger(int index);

    long getLongValue(int index);

    Long getLong(int index);

    String getString(int index);

    boolean getBooleanValue(int index);

    Boolean getBoolean(int index);

    Date getDate(int index);

    JsonObject getObject(int index);

    JsonList getList(int index);

    int size();
}
