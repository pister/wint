package wint.lang.convert.converts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import wint.lang.exceptions.DateParseException;
import wint.lang.utils.CollectionUtil;

public class SmartDateConvert extends AbstractConvert<Date> {

	private static List<DatePatternConverter> datePatternConverts = CollectionUtil.newArrayList(); 
	
	public SmartDateConvert() {
	}
	
	static {
		datePatternConverts.add(new DatePatternConverter("\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd HH:mm:ss"));
		datePatternConverts.add(new DatePatternConverter("\\d{4}\\-\\d{1,2}\\-\\d{1,2}", "yyyy-MM-dd"));
        datePatternConverts.add(new DatePatternConverter("\\d{4}\\d{1,2}\\d{1,2}\\d{2}\\d{2}\\d{2}", "yyyyMMddHHmmss"));
        datePatternConverts.add(new DatePatternConverter("\\d{4}\\d{1,2}\\d{1,2}", "yyyyMMdd"));
        datePatternConverts.add(new DatePatternConverter("\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{2}:\\d{2}", "yyyy-MM-dd HH:mm"));
        datePatternConverts.add(new DatePatternConverter("\\d{1,2}\\-\\d{1,2} \\d{2}:\\d{2}", "MM-dd HH:mm"));
        datePatternConverts.add(new DatePatternConverter("\\d{1,2}:\\d{1,2}:\\d{1,2}", "HH:mm:ss"));
        datePatternConverts.add(new DatePatternConverter("\\d{1,2}:\\d{1,2}", "HH:mm"));
        datePatternConverts.add(new DatePatternConverter("\\d{2}\\-\\d{1,2}\\-\\d{1,2}", "yy-MM-dd"));
        datePatternConverts.add(new DatePatternConverter("\\d{4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}", "yyyy/MM/dd HH:mm:ss"));
        datePatternConverts.add(new DatePatternConverter("\\d{4}\\/\\d{1,2}\\/\\d{1,2}", "yyyy/MM/dd"));
        datePatternConverts.add(new DatePatternConverter("\\d{2}\\/\\d{1,2}\\/\\d{1,2}", "yy/MM/dd"));
    }
	
	public Date convertTo(Object input, Date defaultValue) {
		if (input instanceof Date) {
			return (Date)input;
		}
		if (input == null) {
			return defaultValue;
		}
		String stringDate = input.toString();
		for (DatePatternConverter datePatternConverter : datePatternConverts) {
			if (datePatternConverter.matches(stringDate)) {
				return datePatternConverter.convert(stringDate);
			}
		}
		return defaultValue;
	}

	public Date getDefaultValue() {
		return null;
	}
	
	static class DatePatternConverter {
		
		private Pattern datePattern;
		
		private String dateFormat;
		
		public DatePatternConverter(String datePattern, String dateFormat) {
			super();
			this.datePattern = Pattern.compile(datePattern);
			this.dateFormat = dateFormat;
		}

		public boolean matches(String stringDate) {
			return datePattern.matcher(stringDate).matches();
		}
		
		public Date convert(String stringDate) {
			DateFormat sdf = new SimpleDateFormat(dateFormat);
			try {
				return sdf.parse(stringDate);
			} catch (ParseException e) {
				throw new DateParseException("parse date error, input " + stringDate + ", date format:" + dateFormat, e);
			}
		}
		
		
	}

}
