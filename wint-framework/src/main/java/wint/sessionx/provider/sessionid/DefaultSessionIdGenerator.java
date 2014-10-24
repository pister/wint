package wint.sessionx.provider.sessionid;

import wint.help.codec.MD5;

import java.util.UUID;

/**
 * User: longyi
 * Date: 14-2-27
 * Time: 下午1:11
 */
public class DefaultSessionIdGenerator implements SessionIdGenerator {

    public String generateSessionId() {
        String s1 = UUID.randomUUID().toString();
        String s2 = UUID.randomUUID().toString();
        return MD5.encrypt(s1) + MD5.encrypt(s2);
    }


}
