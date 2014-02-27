package wint.sessionx.provider.sessionid;

import java.util.UUID;

/**
 * User: longyi
 * Date: 14-2-27
 * Time: 下午1:11
 */
public class UuidSessionIdGenerator implements SessionIdGenerator {
    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
