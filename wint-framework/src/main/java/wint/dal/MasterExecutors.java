package wint.dal;

/**
 * Created by songlihuang on 2021/4/27.
 */
public final class MasterExecutors {

    /**
     * 强制走master数据库
     * @param executeHandle
     * @param <T>
     * @return
     */
    public static <T> T executeInMaster(ExecuteHandle<T> executeHandle) {
        try {
            MasterForcer.beginForce();
            return executeHandle.execute();
        } finally {
            MasterForcer.endForce();
        }
    }

}
