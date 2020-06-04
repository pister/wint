package wint.help.json;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by songlihuang on 2020/6/4.
 */
public class JsonUtilTest extends TestCase {

    public static class Foo {
        private int age;

        private String name;

        private List<String> addresses;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<String> addresses) {
            this.addresses = addresses;
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    ", addresses=" + addresses +
                    '}';
        }
    }

    public void testToJsonString() throws Exception {
        Foo foo = new Foo();
        foo.setName("Jack");
        foo.setAge(12);
        foo.setAddresses(Arrays.asList("aa", "bb"));
        String s = JsonUtil.toJsonString(foo);
        System.out.println(s);
    }

    public void testToJsonStringWithNull() throws Exception {
        Foo foo = new Foo();
        foo.setAge(12);
        foo.setAddresses(Arrays.asList("aa", "bb"));
        String s = JsonUtil.toJsonStringWithNull(foo);
        System.out.println(s);
    }

    public void testFromJsonString() throws Exception {
        String s = "{\"age\":12,\"name\":\"Jack\",\"addresses\":[\"aa\",\"bb\"]}";
        Foo foo = JsonUtil.fromJsonString(s, Foo.class);
        System.out.println(foo);
    }

    public void testFromJsonStringAsMap() throws Exception {
        List<Foo> fooList = new ArrayList<Foo>();
        {
            Foo foo = new Foo();
            foo.setAge(1);
            foo.setName("name1");
            foo.setAddresses(Arrays.asList("a1", "b1"));
            fooList.add(foo);
        }
        {
            Foo foo = new Foo();
            foo.setAge(2);
            foo.setName("name2");
            foo.setAddresses(Arrays.asList("a2", "b2"));
            fooList.add(foo);
        }
        String s = JsonUtil.toJsonString(fooList);
        List<Object> list = JsonUtil.fromJsonStringAsList(s);
        System.out.println(list);
    }

}