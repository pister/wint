package wint.lang.utils;

import junit.framework.TestCase;
import org.junit.Assert;

/**
 * User: huangsongli
 * Date: 15/4/20
 * Time: 下午2:38
 */
public class NetworkUtilTest extends TestCase {

    public void testIsLanAddress() throws Exception {
        Assert.assertTrue(NetworkUtil.isLanAddress("10.0.0.0"));
        Assert.assertTrue(NetworkUtil.isLanAddress("10.0.0.1"));
        Assert.assertTrue(NetworkUtil.isLanAddress("10.255.255.254"));
        Assert.assertTrue(NetworkUtil.isLanAddress("192.168.1.1"));
        Assert.assertTrue(NetworkUtil.isLanAddress("172.16.1.2"));
        Assert.assertTrue(NetworkUtil.isLanAddress("172.31.1.2"));
        Assert.assertFalse(NetworkUtil.isLanAddress("172.32.1.2"));
        Assert.assertFalse(NetworkUtil.isLanAddress("172.15.1.2"));
        Assert.assertFalse(NetworkUtil.isLanAddress("172.1.1.2"));
        Assert.assertFalse(NetworkUtil.isLanAddress("11.1.1.1"));
        Assert.assertFalse(NetworkUtil.isLanAddress("192.169.1.1"));
        Assert.assertFalse(NetworkUtil.isLanAddress("192.169.1.1"));
    }
}
