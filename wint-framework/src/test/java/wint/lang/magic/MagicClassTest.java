package wint.lang.magic;

import junit.framework.TestCase;
import wint.demo.app.biz.domain.UserDO;

/**
 * Created by songlihuang on 2021/11/12.
 */
public class MagicClassTest extends TestCase {

    public void test1() {
        MagicClass magicClass = MagicClass.wrap(UserDO.class);
        System.out.println(magicClass.getProperties());
    }

}