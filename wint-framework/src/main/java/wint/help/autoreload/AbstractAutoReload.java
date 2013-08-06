package wint.help.autoreload;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.core.io.resource.autoload.LastModifiedFile;
import wint.lang.utils.CollectionUtil;


/**
 * 自动加载的一个抽象实现
 * @author pister
 * 2012-5-16 下午11:00:48
 */
public abstract class AbstractAutoReload {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected abstract Set<LastModifiedFile> getCheckFiles();
	
	protected abstract void onReload();
	
	private boolean started = false;
	
	protected int getTimeDelay() {
		return 1000;
	}
	
	private void execute() {
		while (true) {
			if (Thread.interrupted()) {
				break;
			}
			try {
				Thread.sleep(getTimeDelay());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			Set<LastModifiedFile> lastModifiedFiles = getCheckFiles();
			if (lastModifiedFiles == null) {
				continue;
			}
			// 复制一份数据，这么做的目的是如果 getCheckFiles()的实现返回的是线程不安全的数据，
			// 并且在另外一个线程正在做修改，那就悲剧了
			Set<LastModifiedFile> needCheckModifiedFiles = CollectionUtil.newHashSet(lastModifiedFiles);
			if (needReload(needCheckModifiedFiles)) {
				try {
					onReload();
				} catch (Exception e) {
					log.warn("reload failed", e);
				}
				
			}
		}
	}
	
	private boolean needReload(Set<LastModifiedFile> needCheckModifiedFiles) {
		for (LastModifiedFile needCheckModifiedFile : needCheckModifiedFiles) {
			if (needCheckModifiedFile.hasModified()) {
				return true;
			}
		}
		return false;
	}
	
	
	public synchronized void startAutoReload() {
		if (started) {
			return;
		}
		CheckThread checkThread = new CheckThread(this);
		checkThread.start();
		started = true;
	}
	
	static class CheckThread extends Thread {

		private AbstractAutoReload autoReload;
		
		public CheckThread(AbstractAutoReload autoReload) {
			super();
			this.autoReload = autoReload;
		}

		@Override
		public void run() {
			autoReload.execute();
		}
		
	}
	

}
