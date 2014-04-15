package wint.lang.convert.converts.array;

import wint.lang.convert.converts.Convert;
import wint.lang.convert.converts.StringConvert;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午10:38
 */
public class StringArrayConvert extends AbstractArrayConvert {

    private StringConvert stringConvert = new StringConvert();

    private Class arrayClass = (new String[0]).getClass();

    @Override
    protected Object createArray(int len) {
        return new String[len];
    }

    @Override
    protected Convert getConvert() {
        return stringConvert;
    }

    @Override
    public Class getArrayClass() {
        return arrayClass;
    }
}
