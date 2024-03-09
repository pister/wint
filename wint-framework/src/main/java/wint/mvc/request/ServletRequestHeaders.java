package wint.mvc.request;

import wint.lang.utils.LocalDateTimeUtil;
import wint.lang.utils.NumberUtil;
import wint.lang.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author songlihuang
 * @date 2024/3/9 14:44
 */
public class ServletRequestHeaders implements RequestHeaders {

    private HttpServletRequest request;

    public ServletRequestHeaders(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getString(String name) {
        return request.getHeader(name);
    }

    @Override
    public List<String> getStringList(String name) {
        return Collections.list(request.getHeaders(name));
    }

    @Override
    public LocalDate getDate(String name) {
        String value = getString(name);
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return LocalDateTimeUtil.parseDate(value);
    }

    @Override
    public Long getLong(String name) {
        String value = getString(name);
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        if (NumberUtil.isNumeric(value)) {
            return Long.parseLong(value);
        } else {
            return null;
        }
    }

    @Override
    public Integer getInteger(String name) {
        String value = getString(name);
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        if (NumberUtil.isNumeric(value)) {
            return Integer.parseInt(value);
        } else {
            return null;
        }
    }

    @Override
    public List<String> getNames() {
        return Collections.list(request.getHeaderNames());
    }
}
