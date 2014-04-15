package wint.lang.convert.converts;

import wint.lang.utils.CollectionUtil;

import java.util.Collection;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午10:09
 */
public class SetConvert extends CollectionConvert {
    @Override
    protected Collection newInstance(Collection initValues) {
        return CollectionUtil.newHashSet(initValues);
    }

    public Collection getDefaultValue() {
        return CollectionUtil.newHashSet();
    }
}
