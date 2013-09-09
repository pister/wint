package wint.core.service.thread;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: longyi
 * Date: 13-9-9
 * Time: 下午2:04
 */
public class LocalThreadService extends AbstractExecutorService {

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void shutdown() {

    }

    public List<Runnable> shutdownNow() {
        return null;
    }

    public boolean isShutdown() {
        return false;
    }

    public boolean isTerminated() {
        return false;
    }

    public void execute(Runnable command) {
        command.run();
    }
}
