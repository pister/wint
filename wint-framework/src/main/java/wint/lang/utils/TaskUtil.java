package wint.lang.utils;

import wint.lang.exceptions.FlowDataException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * User: huangsongli
 * Date: 16/10/26
 * Time: 下午7:31
 */
public class TaskUtil {

    public static <T> T executeTask(ExecutorService executorService, FutureTask<T> newFutureTask, FutureTask<T> existFutureTask) {
        if (existFutureTask != null) {
            return execute(existFutureTask);
        } else {
            executorService.submit(newFutureTask);
            return execute(newFutureTask);
        }
    }

    private static <T> T execute(FutureTask<T> futureTask) {
        try {
            return futureTask.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FlowDataException(e);
        } catch (ExecutionException e) {
            throw new FlowDataException(e);
        }
    }

}
