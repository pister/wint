package wint.mvc.form.validator;

import wint.lang.utils.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateValidator extends AbstractValidator {

	private List<String> formats = Arrays.asList("yyyy-MM-dd","yyyy-MM-dd HH:mm:ss");

    private String minStr;

    private String maxStr;

	private Date min;

	private Date max;

    @Override
    public void init() {
        super.init();
        if (!StringUtil.isEmpty(minStr)) {
            this.min = acceptDate(minStr);
        }
        if (!StringUtil.isEmpty(maxStr)) {
            this.max = acceptDate(maxStr);
        }
    }

    private Date acceptDate(String value) {
        for (String format: formats) {
            Date date = toDate(format, value);
            if (date != null) {
                return date;
            }
        }
        return null;
    }


	@Override
	protected boolean validate(String fieldValue) {
		if (fieldValue == null || fieldValue.length() == 0) {
			return true;
		}
		Date d = acceptDate(fieldValue);
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
	
	private Date toDate(String format, String input) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(input);
		} catch (ParseException e) {
			return null;
		}
	}

    public void setFormats(String formats) {
        this.formats = StringUtil.splitTrim(formats, ",");
    }

    public void setMin(String min) {
        this.minStr = min;
	}

	public void setMax(String max) {
        this.maxStr = max;
	}

}
