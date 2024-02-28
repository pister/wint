package wint.sessionx.filter;

import wint.core.config.property.MagicPropertiesMap;
import wint.core.config.property.PropertiesMap;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:17
 */
public interface Filter {

    String getName();

    void init(PropertiesMap initParameters);

    void doFilter(FilterContext filterContext);

}
