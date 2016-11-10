package wint.sessionx.filter.filters;

import wint.lang.magic.MagicMap;
import wint.lang.utils.ClassUtil;
import wint.sessionx.filter.Filter;

/**
 * User: longyi
 * Date: 14-2-27
 * Time: 下午1:23
 */
public abstract class AbstractFilter implements Filter {

    public String getName() {
        return ClassUtil.getShortClassName(this.getClass());
    }

    public void init(MagicMap initParamters) {
    }
}
