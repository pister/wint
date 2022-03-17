package wint.lang.utils;

import junit.framework.TestCase;

/**
 * Created by songlihuang on 2021/11/16.
 */
public class RandomStringUtilTest extends TestCase {
    public void testRandom() throws Exception {

        for (int i = 0; i < 20; i++) {
            System.out.println(RandomStringUtil.randomAlphanumeric(10));
        }

    }


    class Node {
        Node next;
        int data;
    }


    class List {
        Node first;
        Node last;

        public void add(int v) {
            if (first == null) {
                first = new Node();
                first.data = v;
                last = first;
            } else {
                Node newNode = new Node();
                newNode.data = v;
                last.next = newNode;
                last = newNode;
            }
        }

        void reverse() {
            Node prev = null;
            Node current = first;
            last = first;
            while (current != null) {
                Node next = current.next;
                current.next = prev;
                prev = current;
                if (next == null) {
                    break;
                }
                current = next;

            }
            first = current;
        }

    }


    public void test1() {
        List a = new List();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        a.add(5);
        a.add(6);
        a.add(7);

        a.reverse();

        Node n = a.first;
        while (n != null) {
            System.out.println(n.data);
            n = n.next;
        }

    }
}