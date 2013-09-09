package wint.core.service.thread;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import wint.core.service.AbstractService;

public class DefaultThreadPoolService extends AbstractService implements ThreadPoolService  {

	private ExecutorService executorService;
	
	@Override
	public void init() {
		super.init();
	//	executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	    executorService = new LocalThreadService();
    }

	public ExecutorService getThreadPool() {
		return executorService;
	}

    static protected class LocalThreadService extends AbstractExecutorService {
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

}
