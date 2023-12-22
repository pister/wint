package wint.help.json.wrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

/**
 * Created by songlihuang on 2020/6/4.
 */
public interface JsonObject extends Map<String, Object> {

    int getIntValue(String name);

    Integer getInteger(String name);

    long getLongValue(String name);

    Long getLong(String name);

    String getString(String name);

    boolean getBooleanValue(String name);

    Boolean getBoolean(String name);

    Date getDate(String name);

    LocalDateTime getLocalDateTime(String name);

    LocalDate getLocalDate(String name);

    LocalTime getLocalTime(String name);

    JsonObject getObject(String name);

    JsonList getList(String name);

}
