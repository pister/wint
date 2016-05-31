package wint.sessionx.provider.batch;

/**
 * User: huangsongli
 * Date: 16/5/27
 * Time: 下午1:06
 */
public interface BatchGetReceiver {

    void onReceive(SessionAttributeReader sessionAttributeReader);

}
