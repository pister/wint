package wint.lang.convert.converts.array;

import wint.lang.convert.converts.Convert;
import wint.lang.convert.converts.IntConvert;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午10:22
 */
public class IntWArrayConvert extends AbstractArrayConvert {

    private IntConvert intConvert = new IntConvert();

    private Class arrayClass = (new Integer[0]).getClass();

    @Override
    protected Integer[] createArray(int len) {
        return new Integer[len];
    }

    @Override
    protected Convert<Integer> getConvert() {
        return intConvert;
    }

    @Override
    public Class getArrayClass() {
        return arrayClass;
    }
}
