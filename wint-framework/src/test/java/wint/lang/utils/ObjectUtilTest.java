package wint.lang.utils;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by songlihuang on 2017/9/9.
 */
public class ObjectUtilTest extends TestCase {

    public static class  Foo {
        String f1;
        String f2;
        int v1;
        Date v2;
        Map<String, Object> v3;

        public String getF1() {
            return f1;
        }

        public void setF1(String f1) {
            this.f1 = f1;
        }

        public String getF2() {
            return f2;
        }

        public void setF2(String f2) {
            this.f2 = f2;
        }

        public int getV1() {
            return v1;
        }

        public void setV1(int v1) {
            this.v1 = v1;
        }

        public Date getV2() {
            return v2;
        }

        public void setV2(Date v2) {
            this.v2 = v2;
        }

        public Map<String, Object> getV3() {
            return v3;
        }

        public void setV3(Map<String, Object> v3) {
            this.v3 = v3;
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "f1='" + f1 + '\'' +
                    ", f2='" + f2 + '\'' +
                    ", v1=" + v1 +
                    ", v2=" + v2 +
                    ", v3=" + v3 +
                    '}';
        }
    }

    public void testWalkProperties() throws Exception {
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("a1", "bbbb");
        map.put("a2", new Date());
        map.put("a3", 1111);
        map.put("a4", new String[] {"111", "222"});
        map.put("a5", Arrays.asList("bb", "ccc"));
        Foo foo = new Foo();
        foo.f1 = "fff1";
        foo.f2 = "fff2";
        foo.setV2(new Date());
        foo.setV3(map);
        ObjectUtil.walkProperties(foo, new ObjectUtil.PropertyValueWalker() {
            @Override
            public Object filter(Object value) {
                if (value instanceof String) {
                    return "aa+" + value;
                }
                return value;
            }
        });
        System.out.println(foo);

    }

}