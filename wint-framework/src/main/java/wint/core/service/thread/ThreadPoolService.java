package wint.core.service.thread;

import java.util.concurrent.ExecutorService;

import wint.core.service.Service;

public interface ThreadPoolService extends Service {

	ExecutorService getThreadPool();
	
}
