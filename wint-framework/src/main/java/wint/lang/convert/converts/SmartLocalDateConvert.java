package wint.lang.convert.converts;

import wint.lang.convert.converts.dates.DatePattern;
import wint.lang.convert.converts.dates.DatePatterns;
import wint.lang.exceptions.DateParseException;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.LocalDateTimeUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class SmartLocalDateConvert extends AbstractConvert<LocalDate> {

	private static List<DatePatternConverter> datePatternConverts = CollectionUtil.newArrayList();

	public SmartLocalDateConvert() {
	}
	
	static {
		for (DatePattern datePattern : DatePatterns.getDatePatterns()) {
			datePatternConverts.add(new DatePatternConverter(datePattern.getPattern(), datePattern.getFormat()));
		}
    }
	
	public LocalDate convertTo(Object input, LocalDate defaultValue) {
		if (input instanceof LocalDate) {
			return (LocalDate)input;
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

	public LocalDate getDefaultValue() {
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
		
		public LocalDate convert(String stringDate) {
			try {
				return LocalDate.parse(stringDate, dateFormat);
			} catch (DateTimeParseException e) {
				throw new DateParseException("parse date error, input " + stringDate + ", date format:" + dateFormat, e);
			}
		}
		
		
	}

}
