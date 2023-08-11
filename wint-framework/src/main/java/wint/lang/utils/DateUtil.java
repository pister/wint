package wint.lang.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentMap;


public class DateUtil {

	public static final long MI_SEC_IN_DAY = 24L * 3600 * 1000;
	
	public static final String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss";

	private static final ConcurrentMap<String, DateTimeFormatter> dateFormatterCache = MapUtil.newConcurrentHashMap();
	
	public static Date currentDate() {
		return new Date();
	}

	public static int diffDays(Date date1, Date date2) {
		if (date2 == null || date1 == null) {
			return 0;
		}
		return (int)((date1.getTime() - date2.getTime()) / MI_SEC_IN_DAY);
	}
	
	public static String formatDate(Object date, String fmt) {
		if (date == null) {
			return null;
		}
		if (date instanceof TemporalAccessor) {
			DateTimeFormatter dateTimeFormatter = dateFormatterCache.computeIfAbsent(fmt, k -> DateTimeFormatter.ofPattern(fmt));
			return dateTimeFormatter.format((TemporalAccessor)date);
		}
		if (!(date instanceof Date)) {
			return date.toString();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(date);
	}
	
	public static Date parseDate(String date) {
		return parseDate(date, DEFAULT_DATE_FMT);
	}
	
	public static Date parseDate(String date, String fmt) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String formatNow(String fmt) {
		return formatDate(LocalDateTime.now(), fmt);
	}
	
	public static String formatFullDate(Object date) {
		return formatDate(date,  "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String formatDateYMD(Object date) {
		return formatDate(date,  "yyyy-MM-dd");
	}
	
	public static Date addMinute(Date date, int minute) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}
	public static Date addSecond(Date date, int second) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}
	
	public static Date addHour(Date date, int hour) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}
	
	public static Date addDay(Date date, int days) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, days);
		return cal.getTime();
	}
	
	public static Date addMonth(Date date, int months) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	public static Date addYear(Date date, int years) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}
	
	public static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static boolean isSameDay(Date date1, Date date2) {
		return isSameDay(toCalendar(date1), toCalendar(date2));
	}

	public static Date getDateYMD(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * @deprecated use getDateYMD instead
	 * @param date
	 * @return
	 */
	public static Date getDayYMD(Date date) {
		return getDateYMD(date);
	}

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
	
	public static int getHour(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getMinute(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}
	
	public static int getSecond(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.SECOND);
	}
	
	public static boolean isDateAfter(Date d1, Date d2) {
		return d1.getTime() > d2.getTime();
	}

	public static boolean isDateBefore(Date d1, Date d2) {
		return d1.getTime() < d2.getTime();
	}
	
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		int year1 = cal1.get(Calendar.YEAR);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		
		int year2 = cal2.get(Calendar.YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);
		
		if (year1 != year2) {
			return false;
		}
		
		if (day1 != day2) {
			return false;
		}
		return true;
	}
}
