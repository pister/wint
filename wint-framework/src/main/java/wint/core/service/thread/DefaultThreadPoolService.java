package wint.core.service.thread;

import wint.core.service.AbstractService;

import java.util.concurrent.ExecutorService;

public class DefaultThreadPoolService extends AbstractService implements ThreadPoolService {

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


}
