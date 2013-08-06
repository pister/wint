package wint.core.service.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wint.core.service.AbstractService;

public class DefaultThreadPoolService extends AbstractService implements ThreadPoolService  {

	private ExecutorService executorService;
	
	@Override
	public void init() {
		super.init();
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1); 
	}

	public ExecutorService getThreadPool() {
		return executorService;
	}

}
