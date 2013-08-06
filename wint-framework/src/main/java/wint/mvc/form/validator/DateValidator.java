package wint.mvc.form.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator extends AbstractValidator {

	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private String format = DEFAULT_FORMAT;
	
	private Date min;
	
	private Date max;
	
	@Override
	protected boolean validate(String fieldValue) {
		if (fieldValue == null || fieldValue.length() == 0) {
			return true;
		}
		Date d = toDate(fieldValue);
		if (d == null) {
			return false;
		}
		if (min != null && d.compareTo(min) < 0) {
			return false;
		}
		if (max != null && d.compareTo(max) > 0) {
			return false;
		}
 		return true;
	}
	
	private String toString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	private Date toDate(String input) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(input);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getMin() {
		return toString(min);
	}

	public void setMin(String min) {
		this.min = toDate(min);
	}

	public String getMax() {
		return toString(max);
	}

	public void setMax(String max) {
		this.max = toDate(max);
	}

}
