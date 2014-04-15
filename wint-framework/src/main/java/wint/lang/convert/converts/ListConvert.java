package wint.lang.convert.converts;

import wint.lang.utils.CollectionUtil;

import java.util.Collection;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午10:07
 */
public class ListConvert extends CollectionConvert {
    @Override
    protected Collection newInstance(Collection initValues) {
        return CollectionUtil.newArrayList(initValues);
    }

    public Collection getDefaultValue() {
        return CollectionUtil.newArrayList(0);
    }
}
