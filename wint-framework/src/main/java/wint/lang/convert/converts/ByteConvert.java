package wint.lang.convert.converts;

/**
 * User: longyi
 * Date: 14-4-3
 * Time: 上午9:57
 */
public class ByteConvert extends NumberConvert<Byte> {

    private static final byte defaultValue = (byte)0;

    public Byte convertTo(Object input, Byte defaultValue) {
        Number num = (Number)super.convertToNumber(input, defaultValue);
        return Byte.valueOf(num.byteValue());
    }

    public Byte getDefaultValue() {
        return defaultValue;
    }
}
