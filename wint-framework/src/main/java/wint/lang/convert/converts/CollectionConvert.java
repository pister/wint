package wint.lang.convert.converts;

import wint.lang.magic.MagicList;

import java.util.Collection;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午9:58
 */
public abstract class CollectionConvert extends AbstractConvert<Collection> {

    public Collection convertTo(Object input, Collection defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        if (input instanceof Collection) {
            return newInstance((Collection) input);
        }
        return newInstance(MagicList.wrap(input));
    }

    protected abstract Collection newInstance(Collection initValues);

}
