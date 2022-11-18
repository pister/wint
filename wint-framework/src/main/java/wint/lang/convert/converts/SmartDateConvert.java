package wint.lang.convert.converts;

import wint.lang.convert.converts.dates.DatePattern;
import wint.lang.convert.converts.dates.DatePatterns;
import wint.lang.exceptions.DateParseException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmartDateConvert extends AbstractConvert<Date> {

	public SmartDateConvert() {
	}
	
	public Date convertTo(Object input, Date defaultValue) {
		if (input instanceof Date) {
			return (Date)input;
		}
		if (input == null) {
			return defaultValue;
		}
		String stringDate = input.toString();
		for (DatePattern datePattern : DatePatterns.getDateTimePatterns()) {
			if (datePattern.matches(stringDate)) {
				DateFormat sdf = new SimpleDateFormat(datePattern.getFormat());
				try {
					return sdf.parse(stringDate);
				} catch (ParseException e) {
					throw new DateParseException("parse date error, input " + stringDate + ", date format:" + datePattern.getFormat(), e);
				}
			}
		}
		return defaultValue;
	}

	public Date getDefaultValue() {
		return null;
	}

}
