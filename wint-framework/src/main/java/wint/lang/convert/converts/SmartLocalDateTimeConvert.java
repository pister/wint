package wint.lang.convert.converts;

import wint.lang.convert.converts.dates.DatePattern;
import wint.lang.convert.converts.dates.DatePatterns;
import wint.lang.exceptions.DateParseException;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class SmartLocalDateTimeConvert extends AbstractConvert<LocalDateTime> {

	private static List<DatePatternConverter> datePatternConverts = CollectionUtil.newArrayList();

	public SmartLocalDateTimeConvert() {
	}
	
	static {
		for (DatePattern datePattern : DatePatterns.getDateTimePatterns()) {
			datePatternConverts.add(new DatePatternConverter(datePattern.getPattern(), datePattern.getFormat()));
		}
    }
	
	public LocalDateTime convertTo(Object input, LocalDateTime defaultValue) {
		if (input instanceof LocalDateTime) {
			return (LocalDateTime)input;
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

	public LocalDateTime getDefaultValue() {
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
		
		public LocalDateTime convert(String stringDate) {
			try {
				return LocalDateTime.parse(stringDate, dateFormat);
			} catch (DateTimeParseException e) {
				throw new DateParseException("parse date error, input " + stringDate + ", date format:" + dateFormat, e);
			}
		}
		
		
	}

}
