package wint.sessionx.provider.sessionid;

import java.security.SecureRandom;

/**
 * User: longyi
 * Date: 14-2-27
 * Time: 下午1:11
 */
public class DefaultSessionIdGenerator implements SessionIdGenerator {

    private final SecureRandom numberGenerator = new SecureRandom();

    public DefaultSessionIdGenerator() {
        byte[] seed = numberGenerator.generateSeed(32);
        numberGenerator.setSeed(seed);
    }

    public static void main(String[] args) {
        DefaultSessionIdGenerator sessionIdGenerator = new DefaultSessionIdGenerator();
        for (int i = 0; i < 10; ++i) {
            System.out.println(sessionIdGenerator.generateSessionId());
        }
    }

    static private String toHexString(byte[] ba) {
        StringBuilder sbuf = new StringBuilder();
        for (byte b : ba) {
            String s = Integer.toHexString((int) (b & 0xff));
            if (s.length() == 1) {
                sbuf.append('0');
            }
            sbuf.append(s);
        }
        return sbuf.toString();
    }

    public String generateSessionId() {
        byte[] total = new byte[32];
        numberGenerator.nextBytes(total);
        return toHexString(total);
    }


}
