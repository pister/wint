package wint.lang.convert.converts.array;

import wint.lang.convert.converts.Convert;
import wint.lang.convert.converts.IntConvert;
import wint.lang.convert.converts.array.AbstractArrayConvert;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午10:31
 */
public class IntArrayConvert extends AbstractArrayConvert {

    private IntConvert intConvert = new IntConvert();

    private Class arrayClass = (new int[0]).getClass();

    @Override
    protected Object createArray(int len) {
        return new int[len];
    }

    @Override
    protected Convert getConvert() {
        return intConvert;
    }

    @Override
    public Class getArrayClass() {
        return arrayClass;
    }

}
