package wint.lang.convert.converts;

import java.sql.Timestamp;
import java.util.Date;


public class SmartTimestampConvert extends AbstractConvert<Timestamp> {

	private SmartDateConvert smartDateConvert = new SmartDateConvert();
	
	public Timestamp convertTo(Object input, Timestamp defaultValue) {
		Date date = smartDateConvert.convertTo(input, defaultValue);
		if (date == null) {
			return defaultValue;
		}
		return new Timestamp(date.getTime());
	}

	public Timestamp getDefaultValue() {
		return null;
	}

}
