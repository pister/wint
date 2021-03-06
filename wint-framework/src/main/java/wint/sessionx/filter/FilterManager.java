package wint.sessionx.filter;

import java.util.Map;

/**
 * User: longyi
 * Date: 14-2-26
 * Time: 下午1:20
 */
public interface FilterManager {

    void addFilter(Filter filter);

    FilterContext performFilters(Map<String, Object> initAttributes);
}
