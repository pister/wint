package wint.help.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Assert;
import wint.help.json.wrapper.JsonList;
import wint.help.json.wrapper.JsonObject;

import java.lang.reflect.Type;
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

        private Bar bar;

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

        public Bar getBar() {
            return bar;
        }

        public void setBar(Bar bar) {
            this.bar = bar;
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

    public static class Bar {
        private String x;
        private int y;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        private List<Integer> values;

        public void setValues(List<Integer> values) {
            this.values = values;
        }

        public List<Integer> getValues() {
            return values;
        }
    }

    public void testToJsonString() throws Exception {
        Foo foo = new Foo();
        foo.setName("Jack");
        foo.setAge(12);
        foo.setAddresses(Arrays.asList("aa", "bb"));
        Bar bar = new Bar();
        bar.setX("xxx");
        bar.setY(222);
        foo.setBar(bar);
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

    public void testFromJsonArray() throws Exception {
        String s = "[\"hello\", \"world\"]";
        List<String> arr = JsonUtil.fromJsonStringList(s, String.class);
        Assert.assertEquals(arr.size(), 2);
        assertEquals(arr.get(0), "hello");
        assertEquals(arr.get(1), "world");
    }

    public void testFromJsonArray1() throws Exception {
        String s = "[123, 456]";
        List<Integer> arr = JsonUtil.fromJsonStringList(s, Integer.class);
        Assert.assertEquals(arr.size(), 2);
        assertEquals(arr.get(0), Integer.valueOf(123));
        assertEquals(arr.get(1), Integer.valueOf(456));
    }

    public void testFromJsonArray2() throws Exception {
        String s = "[{\"x\":\"hello\",\"y\":123, \"values\":[1,2]},{\"x\":\"world\",\"y\":666,\"values\":[2,3]}]";
        List<Bar> arr = JsonUtil.fromJsonStringList(s, Bar.class);
        Assert.assertEquals(arr.size(), 2);
        {
            Bar bar = arr.get(0);
            Assert.assertEquals(bar.getX(), "hello");
            Assert.assertEquals(bar.getY(), 123);
            List<Integer> values = bar.getValues();
            System.out.println(values);
        }
        {
            Bar bar = arr.get(1);
            Assert.assertEquals(bar.getX(), "world");
            Assert.assertEquals(bar.getY(), 666);
            List<Integer> values = bar.getValues();

            System.out.println(values);

        }
    }

    public void test33() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> numbers = gson.fromJson("[123, 456]", type);
        System.out.println(numbers);
    }

    public void testFromJsonStringObject() throws Exception {
        String s = "{\"age\":12,\"name\":\"Jack\",\"addresses\":[\"aa\",\"bb\"],\"bar\":{\"x\":\"xxx\",\"y\":222}}";
        JsonObject object = JsonUtil.fromJsonStringObject(s);
        System.out.println(object);
        int age = object.getIntValue("age");
        Assert.assertEquals(12, age);
        String name = object.getString("name");
        Assert.assertEquals("Jack", name);
        JsonList jsonList = object.getList("addresses");
        Assert.assertEquals(2, jsonList.size());
        Assert.assertEquals("aa", jsonList.getString(0));
        Assert.assertEquals("bb", jsonList.getString(1));

        JsonObject bar = object.getObject("bar");
        Assert.assertNotNull(bar);
        Assert.assertEquals("xxx", bar.getString("x"));
        Assert.assertEquals(222, bar.getIntValue("y"));
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
        JsonList list = JsonUtil.fromJsonStringList(s);
        System.out.println(list);
    }

}