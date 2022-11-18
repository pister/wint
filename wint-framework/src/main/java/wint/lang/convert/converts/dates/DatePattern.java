package wint.lang.convert.converts.dates;

import java.util.regex.Pattern;

/**
 * Created by songlihuang on 2022/11/18.
 */
public class DatePattern {

    private Pattern pattern;

    private String format;

    public DatePattern(String pattern, String format) {
        super();
        this.pattern = Pattern.compile(pattern);
        this.format = format;
    }

    public boolean matches(String stringDate) {
        return pattern.matcher(stringDate).matches();
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getFormat() {
        return format;
    }
}
