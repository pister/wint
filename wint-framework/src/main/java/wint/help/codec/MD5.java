package wint.help.codec;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class MD5 {

    private static Charset charset = Charset.forName("utf-8");

    public static String encrypt(String s) {
        return encrypt(s.getBytes(charset));
    }

    public static String encrypt(byte[] source) {
        String s;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return s;
    }

    static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
}
