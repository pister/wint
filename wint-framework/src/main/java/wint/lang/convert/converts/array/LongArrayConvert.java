package wint.lang.convert.converts.array;

import wint.lang.convert.converts.Convert;
import wint.lang.convert.converts.LongConvert;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午10:35
 */
public class LongArrayConvert extends AbstractArrayConvert {

    private LongConvert longConvert = new LongConvert();

    private Class arrayClass = (new long[0]).getClass();

    @Override
    protected Object createArray(int len) {
        return new long[len];
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
