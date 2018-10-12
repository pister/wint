package wint.help.redis;

import redis.clients.jedis.Protocol;
import wint.lang.utils.Tuple;

/**
 * Created by songlihuang on 2018/10/12.
 */
public class RedisUtil {

    public static final int DEFAULT_DATABASE = Protocol.DEFAULT_DATABASE;

    public static Tuple<String, Integer> parseForDatabase(String raw) {
        int sepPos = raw.lastIndexOf('/');
        int database = DEFAULT_DATABASE;
        if (sepPos > 0) {
            String realPart = raw.substring(0, sepPos);
            String databaseString = raw.substring(sepPos + 1);
            try {
                database = Integer.parseInt(databaseString);
            } catch (Exception e) {
                // ignore
            }
            return Tuple.create(realPart, database);
        } else {
            return Tuple.create(raw, database);
        }
    }

}
