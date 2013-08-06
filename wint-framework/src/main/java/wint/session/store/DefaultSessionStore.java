package wint.session.store;

import java.util.List;
import java.util.Map;
import java.util.Set;

import wint.lang.WintException;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.session.config.SessionConfig;
import wint.session.config.SessionDataConfig;
import wint.session.config.SessionDataServiceConfig;
import wint.session.data.SessionData;
import wint.session.data.SessionDataService;
import wint.session.servlet.WintSessionHttpServletRequest;
import wint.session.servlet.WintSessionHttpServletResponse;
import wint.session.util.WintSessionContants;

public class DefaultSessionStore implements SessionStore {

	private WintSessionHttpServletRequest wintSessionHttpServletRequest;

	private WintSessionHttpServletResponse wintSessionHttpServletResponse;
	
	private Map<String, SessionDataService> namedSessionDataServices;
	
	private List<SessionDataService> sessionDataServices;
	
	private SessionConfig sessionConfig;
	
	private int maxInactiveInterval = 0;
	
	private boolean newSession;
	
	private long createTime;
	
	private long lastAccessedTime;
	
	private boolean inited = false;
	
	private SessionDataService defaultSessionDataService;
	
	private String sessionId;
	
	public DefaultSessionStore(WintSessionHttpServletRequest wintSessionHttpServletRequest, WintSessionHttpServletResponse wintSessionHttpServletResponse) {
		super();
		this.wintSessionHttpServletRequest = wintSessionHttpServletRequest;
		this.wintSessionHttpServletResponse = wintSessionHttpServletResponse;
	}

	protected SessionDataService getSessionDataService(String name) {
		SessionDataService ret = namedSessionDataServices.get(name);
		if (ret != null) {
			return ret;
		}
		return getDefaultSessionDataService();
	}
	
	private SessionDataService getDefaultSessionDataService() {
		if (defaultSessionDataService != null) {
			return defaultSessionDataService;
		}
		// TODO 按照什么策略找到一个存放内部数据的Service
		defaultSessionDataService = sessionDataServices.get(0);
		return defaultSessionDataService;
	}
	
	private void initSessionCreateTime() {
		if (!newSession) {
			SessionData sessionData = getDefaultSessionDataService().get(WintSessionContants.InnerKeys.CREATE_TIME_KEY);
			if (sessionData == null) {
				newSessionTime();
				return;
			}
			Long createTimeFromData = (Long)sessionData.getData();
			if (createTimeFromData == null) {
				newSessionTime();
				return;
			}
			this.createTime = createTimeFromData;
		} else {
			newSessionTime();
		}
	}
	
	private void newSessionTime() {
		this.newSession = true;
		this.createTime = System.currentTimeMillis();
	}
	
	public void init(SessionConfig sessionConfig) {
		if (inited) {
			return;
		}
		this.sessionConfig = sessionConfig;
		try {
			List<SessionDataServiceConfig> sessionDataServiceConfigs = sessionConfig.getSessionDataServiceConfigs();
			if (CollectionUtil.isEmpty(sessionDataServiceConfigs)) {
				// TODO 没有配置SessionDataServiceConfig的情况
				throw new WintException("SessionDataServiceConfig can not empty!");
			}
			namedSessionDataServices = MapUtil.newHashMap();
			sessionDataServices = CollectionUtil.newArrayList(sessionDataServiceConfigs.size());
			
			for (SessionDataServiceConfig sessionDataServiceConfig : sessionDataServiceConfigs) {
				Class<? extends SessionDataService> sessionDataServiceClass = sessionDataServiceConfig.getSessionDataServiceClass();
				Set<SessionDataConfig> sessionDataConfigs = sessionDataServiceConfig.getSessionDataConfigs();
				SessionDataService sessionDataService = sessionDataServiceClass.newInstance();

				sessionDataService.setSerializeService(sessionDataServiceConfig.getSerializeService());
				sessionId = sessionDataService.init(wintSessionHttpServletRequest, wintSessionHttpServletResponse, sessionDataConfigs, sessionDataServiceConfig.getSessionStoreConfig());
				
				sessionDataServices.add(sessionDataService);
				for (SessionDataConfig sessionDataConfig : sessionDataConfigs) {
					namedSessionDataServices.put(sessionDataConfig.getName(), sessionDataService);
				}
			}
			
			initSessionCreateTime();
			updateLastAccessTime();
			inited = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void commit() {
		if (!inited) {
			return;
		}
		
		// 1、提交session数据
		for (SessionDataService sessionDataService : sessionDataServices) {
			sessionDataService.commit(wintSessionHttpServletRequest, wintSessionHttpServletResponse, getExpire());
		}
	}
	
	private void updateLastAccessTime() {
		lastAccessedTime = System.currentTimeMillis();
	}
	
	public Object getAttribute(String name) {
		SessionData sessionData = getSessionDataService(name).get(name);
		if (sessionData == null) {
			return null;
		}
		Object ret = sessionData.getData();
		return ret;
	}

	public void setAttribute(String name, Object value) {
		SessionData newSessionData = new SessionData(name, value);
		newSessionData.setExpire(getExpire());
		getSessionDataService(name).set(name, newSessionData);
	}

	public void removeAttribute(String name) {
		getSessionDataService(name).remove(name);
	}

	public void invalidate() {
		for (SessionDataService sessionDataService : sessionDataServices) {
			sessionDataService.invalidate();
		}
	}

	public Set<String> getAttributeNames() {
		Set<String> ret = CollectionUtil.newHashSet();
		for (SessionDataService sessionDataService : sessionDataServices) {
			ret.addAll(sessionDataService.getNames());
		}
		return ret;
	}

	public boolean isNew() {
		return newSession;
	}

	protected int getExpire() {
		if (maxInactiveInterval < 0) {
			return YEAR_IN_SECONDS;
		}
		if (maxInactiveInterval > 0) {
			return maxInactiveInterval;
		}
		return sessionConfig.getExpire();
	}

	static final int YEAR_IN_SECONDS = 365 * 24 * 3600;
	
	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public String getSessionId() {
		return sessionId;
	}

}
