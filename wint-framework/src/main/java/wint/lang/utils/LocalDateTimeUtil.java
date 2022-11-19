package wint.lang.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by songlihuang on 2022/11/19.
 */
public class LocalDateTimeUtil {

    private static ConcurrentMap<String, DateTimeFormatter> patternCache = MapUtil.newConcurrentHashMap();

    public static DateTimeFormatter getDateTimeFormatter(String patternFormat) {
        return patternCache.computeIfAbsent(patternFormat, k -> DateTimeFormatter.ofPattern(patternFormat));
    }

    public static LocalDate parseDate(String input, String patternFormat) {
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(patternFormat);
        return LocalDate.parse(input, dateTimeFormatter);
    }

    public static LocalDateTime parseDateTime(String input, String patternFormat) {
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(patternFormat);
        return LocalDateTime.parse(input, dateTimeFormatter);
    }

    public static LocalTime parseTime(String input, String patternFormat) {
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(patternFormat);
        return LocalTime.parse(input, dateTimeFormatter);
    }

    public static YearMonth parseYearMonth(String input, String patternFormat) {
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(patternFormat);
        return YearMonth.parse(input, dateTimeFormatter);
    }

}
