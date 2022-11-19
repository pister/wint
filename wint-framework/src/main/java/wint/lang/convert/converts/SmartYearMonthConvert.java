package wint.lang.convert.converts;

import wint.lang.convert.converts.dates.DatePattern;
import wint.lang.convert.converts.dates.DatePatterns;
import wint.lang.exceptions.DateParseException;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.LocalDateTimeUtil;

import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class SmartYearMonthConvert extends AbstractConvert<YearMonth> {

	private static List<DatePatternConverter> datePatternConverts = CollectionUtil.newArrayList();

	public SmartYearMonthConvert() {
	}
	
	static {
		for (DatePattern datePattern : DatePatterns.getYearMonthPatterns()) {
			datePatternConverts.add(new DatePatternConverter(datePattern.getPattern(), datePattern.getFormat()));
		}
    }
	
	public YearMonth convertTo(Object input, YearMonth defaultValue) {
		if (input instanceof YearMonth) {
			return (YearMonth)input;
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

	public YearMonth getDefaultValue() {
		return null;
	}
	
	static class DatePatternConverter {
		
		private Pattern datePattern;
		
		private DateTimeFormatter dateFormat;
		
		public DatePatternConverter(Pattern pattern, String dateFormat) {
			super();
			this.datePattern = pattern;
			this.dateFormat = LocalDateTimeUtil.getDateTimeFormatter(dateFormat);
		}

		public boolean matches(String stringDate) {
			return datePattern.matcher(stringDate).matches();
		}
		
		public YearMonth convert(String stringDate) {
			try {
				return YearMonth.parse(stringDate, dateFormat);
			} catch (DateTimeParseException e) {
				throw new DateParseException("parse date error, input " + stringDate + ", date format:" + dateFormat, e);
			}
		}
		
		
	}

}
