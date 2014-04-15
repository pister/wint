package wint.lang.convert.converts.array;

import wint.lang.convert.converts.AbstractConvert;
import wint.lang.convert.converts.Convert;
import wint.lang.magic.MagicArrayObject;
import wint.lang.magic.MagicList;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午10:10
 */
public abstract class AbstractArrayConvert extends AbstractConvert<Object> {

    public Object convertTo(Object input, Object defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        MagicList magicList = MagicList.wrap(input);
        int len = magicList.size();
        Object ret = createArray(len);
        int i = 0;
        for (Object o : magicList) {
            Array.set(ret, i, getConvert().convertTo(o));
            i++;
        }
        return ret;
    }

    public Object getDefaultValue() {
        return null;
    }

    protected abstract Object createArray(int len);

    protected abstract Convert getConvert();

    public abstract Class getArrayClass();

}
