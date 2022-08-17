package wint.lang.utils;

import junit.framework.TestCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * Created by songlihuang on 2022/8/17.
 */
public class DateUtilTest extends TestCase {

    public void test1() {
        System.out.println(DateUtil.formatFullDate(LocalDateTime.now()));
        System.out.println(DateUtil.formatDate(LocalDate.now(), "yyyy-MM-dd"));
        System.out.println(DateUtil.formatDate(LocalTime.now(), "HH:mm:ss"));
        System.out.println(DateUtil.formatFullDate(new Date()));
    }

}