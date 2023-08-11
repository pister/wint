package wint.lang.utils;

import wint.lang.convert.converts.SmartLocalDateConvert;
import wint.lang.convert.converts.SmartLocalDateTimeConvert;
import wint.lang.convert.converts.SmartLocalTimeConvert;
import wint.lang.convert.converts.dates.DatePattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by songlihuang on 2022/11/19.
 */
public class LocalDateTimeUtil {

    private static final ConcurrentMap<String, DateTimeFormatter> patternCache = MapUtil.newConcurrentHashMap();

    private static final SmartLocalDateTimeConvert smartLocalDateTimeConvert = new SmartLocalDateTimeConvert();
    private static final SmartLocalDateConvert smartLocalDateConvert = new SmartLocalDateConvert();

    private static final SmartLocalTimeConvert smartLocalTimeConvert = new SmartLocalTimeConvert();

    private static final Map<Class<? extends Temporal>, String> defaultFormats = new HashMap<>();

    static {
        defaultFormats.put(LocalDateTime.class, "yyyy-MM-dd HH:mm:ss");
        defaultFormats.put(LocalDate.class, "yyyy-MM-dd");
        defaultFormats.put(LocalTime.class, "HH:mm:ss");
    }


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

    public static String format(Temporal input, String patternFormat) {
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(patternFormat);
        return dateTimeFormatter.format(input);
    }

    public static String formatDefault(Temporal input) {
        if (input == null) {
            return null;
        }
        String fmt = defaultFormats.get(input.getClass());
        if (fmt == null) {
            // use object's default toString()
            return input.toString();
        }
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(fmt);
        return dateTimeFormatter.format(input);
    }

    public static LocalDateTime parseDateTime(String input) {
        return smartLocalDateTimeConvert.convertTo(input, null);
    }

    public static LocalDate parseDate(String input) {
        return smartLocalDateConvert.convertTo(input, null);
    }

    public static LocalTime parseTime(String input) {
        return smartLocalTimeConvert.convertTo(input, null);
    }

    public static LocalDateTime getDateYMD(LocalDateTime startTime) {
        LocalDate startDate = startTime.toLocalDate();
        return LocalDateTime.of(startDate, LocalTime.of(0, 0));
    }


}
