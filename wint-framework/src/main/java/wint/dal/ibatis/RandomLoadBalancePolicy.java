package wint.dal.ibatis;

import java.util.Random;

/**
 * User: pister
 * Date: 13-7-6
 * Time: 下午10:02
 */
public class RandomLoadBalancePolicy implements LoadBalancePolicy {

    private int size;

    private Random rand = new Random(System.currentTimeMillis());

    public RandomLoadBalancePolicy(int size) {
        this.size = size;
    }

    public int getNext() {
        return rand.nextInt(size);
    }
}
