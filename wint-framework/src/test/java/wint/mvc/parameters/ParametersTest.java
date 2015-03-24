package wint.mvc.parameters;

import junit.framework.TestCase;
import wint.lang.utils.MapUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

/**
 * User: huangsongli
 * Date: 15/3/12
 * Time: 下午1:35
 */
public class ParametersTest extends TestCase {

    public static class Foo {
        private int age;
        private Integer level;
        private String name;
        private Integer noValue;
        private int noAge;

        private Integer[] ids;

        public Integer getNoValue() {
            return noValue;
        }

        public void setNoValue(Integer noValue) {
            this.noValue = noValue;
        }

        public int getNoAge() {
            return noAge;
        }

        public void setNoAge(int noAge) {
            this.noAge = noAge;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer[] getIds() {
            return ids;
        }

        public void setIds(Integer[] ids) {
            this.ids = ids;
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "age=" + age +
                    ", level=" + level +
                    ", name='" + name + '\'' +
                    ", noValue=" + noValue +
                    ", noAge=" + noAge +
                    ", ids=" + Arrays.toString(ids) +
                    '}';
        }
    }

    public void testApply() throws InterruptedException {
        Map<String, String[]> data = MapUtil.newHashMap();
        data.put("age", new String[] {"10"});
        data.put("level", new String[] {"12"});
        data.put("ids", new String[] {"12", "22", "33"});
        data.put("name", new String[] {"Pister"});
        MapParameters mapParameters = new MapParameters(data);
        Foo foo = new Foo();
        Thread.sleep(1000000000);
        mapParameters.apply(foo);
        System.out.println(foo);
    }

}
