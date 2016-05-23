package wint.mvc.url.rewrite.domain;

/**
 * User: huangsongli
 * Date: 16/5/23
 * Time: 下午5:09
 */
public interface DomainParser {

    Object parse(String domain);

    String getParameterName();
}
