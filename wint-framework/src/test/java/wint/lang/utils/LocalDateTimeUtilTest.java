package wint.lang.utils;

import junit.framework.TestCase;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTimeUtilTest extends TestCase {

    public void testTestFormat() {
        {
            String s = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
            System.out.println(s);
        }
        {
            String s = LocalDateTimeUtil.format(LocalDate.now(), "yyyy-MM-dd");
            System.out.println(s);
        }
    }
}