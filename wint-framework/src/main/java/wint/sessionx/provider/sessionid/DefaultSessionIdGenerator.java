package wint.sessionx.provider.sessionid;

import java.security.SecureRandom;

/**
 * User: longyi
 * Date: 14-2-27
 * Time: 下午1:11
 */
public class DefaultSessionIdGenerator implements SessionIdGenerator {

    private final SecureRandom numberGenerator = new SecureRandom();

    private static final int LENGTH = 32;

    public DefaultSessionIdGenerator() {
        byte[] seed = numberGenerator.generateSeed(32);
        numberGenerator.setSeed(seed);
    }

    static private String toHexString(byte[] ba) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : ba) {
            String s = Integer.toHexString(b & 0xff);
            if (s.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public String generateSessionId() {
        byte[] total = new byte[LENGTH];
        numberGenerator.nextBytes(total);
        return toHexString(total);
    }


}
