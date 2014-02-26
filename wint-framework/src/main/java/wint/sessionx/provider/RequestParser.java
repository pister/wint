package wint.sessionx.provider;

import javax.servlet.http.Cookie;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午2:04
 */
public interface RequestParser {

    Object parseRequest(Cookie[] cookies);
}
