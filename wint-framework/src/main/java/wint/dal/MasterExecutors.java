package wint.dal;

/**
 * Created by songlihuang on 2021/4/27.
 */
public final class MasterExecutors {

    public static <T> T executeInMaster(ExecuteHandle<T> executeHandle) {
        try {
            MasterForcer.beginForce();
            return executeHandle.execute();
        } finally {
            MasterForcer.endForce();
        }
    }

}
