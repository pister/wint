package wint.lang.convert.converts.array;

import wint.lang.convert.converts.Convert;
import wint.lang.convert.converts.LongConvert;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午10:36
 */
public class LongWArrayConvert extends AbstractArrayConvert {

    private LongConvert longConvert = new LongConvert();

    private Class arrayClass = (new Long[0]).getClass();

    @Override
    protected Object createArray(int len) {
        return new Long[len];
    }

    @Override
    protected Convert getConvert() {
        return longConvert;
    }

    @Override
    public Class getArrayClass() {
        return arrayClass;
    }
}
