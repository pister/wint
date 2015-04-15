package wint.help.redis;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * User: huangsongli
 * Date: 15/4/15
 * Time: 下午5:07
 */
public class RedisClientFactoryBean extends RedisClientFactory implements FactoryBean, InitializingBean {

    private RedisClient redisClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.redisClient = getRedisClient();
    }

    @Override
    public Object getObject() throws Exception {
        return redisClient;
    }

    @Override
    public Class getObjectType() {
        return redisClient.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
