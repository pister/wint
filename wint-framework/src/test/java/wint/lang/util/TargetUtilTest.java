package wint.lang.util;

import junit.framework.TestCase;
import org.junit.Assert;
import wint.lang.utils.TargetUtil;

public class TargetUtilTest extends TestCase {
	
	/**
	 * 	 null		=> /
	 *   ""			=> /
	 *   //			=> /
	 *   \			=> /
	 *   /.ext		=> /
	 *   xxx		=> /xxx
	 *   xxx/		=> /xxx   
	 *   /xxx/		=> /xxx
	 *   \xxx		=> /xxx
	 *   /xxx/yyy 		=> /xxx/yyy
	 *   /xxx//yyy 		=> /xxx/yyy
	 *   /xxx/yyy_zzz	=> /xxx/yyyZzz
	 *   /xxx/yyy-zzz	=> /xxx/yyyZzz
	 *   /xxx_aaa/yyy_zzz => /xxxAaa/yyyZzz
	 *   /xxx/yyy.ext	=>	/xxx/yyy
	 *   /xxx-aaa/yyy.ext => /xxxAxx/yyy
	 * 
	 * @param target
	 * @return
	 */
	public void testNormalizeTarget() {
		Assert.assertEquals("/", TargetUtil.normalizeTarget(null));
		Assert.assertEquals("/", TargetUtil.normalizeTarget(""));
		Assert.assertEquals("/", TargetUtil.normalizeTarget("//"));
		Assert.assertEquals("/", TargetUtil.normalizeTarget("\\"));
		Assert.assertEquals("/", TargetUtil.normalizeTarget("/.ext"));
		Assert.assertEquals("/xxx", TargetUtil.normalizeTarget("xxx"));
		Assert.assertEquals("/xxx", TargetUtil.normalizeTarget("xxx/"));
		Assert.assertEquals("/xxx", TargetUtil.normalizeTarget("/xxx/"));
		Assert.assertEquals("/xxx", TargetUtil.normalizeTarget("\\xxx"));
		Assert.assertEquals("/xxx/yyy", TargetUtil.normalizeTarget("/xxx/yyy"));
		Assert.assertEquals("/xxx/yyy", TargetUtil.normalizeTarget("/xxx//yyy"));
		Assert.assertEquals("/xxx/yyyZzz", TargetUtil.normalizeTarget("/xxx/yyy_zzz"));
		Assert.assertEquals("/xxx/yyyZzz", TargetUtil.normalizeTarget("/xxx/yyy-zzz"));
		Assert.assertEquals("/xxxAaa/yyyZzz", TargetUtil.normalizeTarget("/xxx_aaa/yyy_zzz"));
		Assert.assertEquals("/xxxAaa/yyyZzz", TargetUtil.normalizeTarget("/xxx_aaa/yyy_zzz"));
		Assert.assertEquals("/xxx/yyy", TargetUtil.normalizeTarget("/xxx/yyy.ext"));
		Assert.assertEquals("/xxxAaa/yyy", TargetUtil.normalizeTarget("/xxx-aaa/yyy.ext"));
	}

}
