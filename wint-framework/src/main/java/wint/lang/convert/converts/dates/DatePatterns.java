package wint.lang.convert.converts.dates;

import wint.lang.utils.CollectionUtil;

import java.util.List;


/**
 * Created by songlihuang on 2022/11/18.
 */
public final class DatePatterns {

    private static List<DatePattern> dateTimePatterns = CollectionUtil.newArrayList();

    private static List<DatePattern> datePatterns = CollectionUtil.newArrayList();

    private static List<DatePattern> timePatterns = CollectionUtil.newArrayList();

    private static List<DatePattern> yearMonthPatterns = CollectionUtil.newArrayList();

    private static List<DatePattern> allPatterns = CollectionUtil.newArrayList();

    static {
        dateTimePatterns.add(new DatePattern("\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd HH:mm:ss"));
        dateTimePatterns.add(new DatePattern("\\d{4}\\-\\d{1,2}\\-\\d{1,2}T\\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd'T'HH:mm:ss"));
        dateTimePatterns.add(new DatePattern("\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}", "yyyyMMddHHmmss"));
        dateTimePatterns.add(new DatePattern("\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{2}:\\d{2}", "yyyy-MM-dd HH:mm"));
        dateTimePatterns.add(new DatePattern("\\d{1,2}\\-\\d{1,2} \\d{2}:\\d{2}", "MM-dd HH:mm"));
        dateTimePatterns.add(new DatePattern("\\d{4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}", "yyyy/MM/dd HH:mm:ss"));

        datePatterns.add(new DatePattern("\\d{4}\\-\\d{1,2}\\-\\d{1,2}", "yyyy-MM-dd"));
        datePatterns.add(new DatePattern("\\d{4}\\/\\d{1,2}\\/\\d{1,2}", "yyyy/MM/dd"));
        datePatterns.add(new DatePattern("\\d{2}\\/\\d{1,2}\\/\\d{1,2}", "yy/MM/dd"));
        datePatterns.add(new DatePattern("\\d{4}年\\d{1,2}月\\d{1,2}日", "yyyy年MM月dd日"));
        datePatterns.add(new DatePattern("\\d{2}\\-\\d{1,2}\\-\\d{1,2}", "yy-MM-dd"));
        datePatterns.add(new DatePattern("\\d{4}\\d{2}\\d{2}", "yyyyMMdd"));

        yearMonthPatterns.add(new DatePattern("\\d{4}\\-\\d{1,2}", "yyyy-MM"));
        yearMonthPatterns.add(new DatePattern("\\d{4}\\d{2}", "yyyyMM"));
        yearMonthPatterns.add(new DatePattern("\\d{2}\\-\\d{1,2}", "yy-MM"));
        yearMonthPatterns.add(new DatePattern("\\d{4}\\/\\d{1,2}", "yyyy/MM"));
        yearMonthPatterns.add(new DatePattern("\\d{2}\\/\\d{1,2}", "yy/MM"));
        yearMonthPatterns.add(new DatePattern("\\d{4}年\\d{1,2}月", "yyyy年MM月"));

        timePatterns.add(new DatePattern("\\d{1,2}:\\d{1,2}:\\d{1,2}", "HH:mm:ss"));
        timePatterns.add(new DatePattern("\\d{1,2}:\\d{1,2}", "HH:mm"));


        allPatterns.addAll(dateTimePatterns);
        allPatterns.addAll(datePatterns);
        allPatterns.addAll(yearMonthPatterns);
        allPatterns.addAll(timePatterns);

    }

    public static List<DatePattern> getAllPatterns() {
        return allPatterns;
    }

    public static List<DatePattern> getDateTimePatterns() {
        return dateTimePatterns;
    }

    public static List<DatePattern> getDatePatterns() {
        return datePatterns;
    }

    public static List<DatePattern> getTimePatterns() {
        return timePatterns;
    }

    public static List<DatePattern> getYearMonthPatterns() {
        return yearMonthPatterns;
    }
}
