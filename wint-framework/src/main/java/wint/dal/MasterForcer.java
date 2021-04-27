package wint.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: pister
 * Date: 13-7-7
 * Time: 上午11:46
 */
public class MasterForcer {

    private static final Logger log = LoggerFactory.getLogger(MasterForcer.class);

    static final ThreadLocal<Boolean> forceMasterTL = new ThreadLocal<Boolean>();

    public static void beginForce() {
        if (log.isDebugEnabled()) {
              log.debug("begin force master .");
        }
        forceMasterTL.set(true);
    }

    public static void endForce() {
        if (log.isDebugEnabled()) {
            log.debug("end force master .");
        }
        forceMasterTL.remove();
    }

    public static boolean isForceMaster() {
        Boolean value = forceMasterTL.get();
        if (value == null) {
            return false;
        }
        return value;
    }

}
