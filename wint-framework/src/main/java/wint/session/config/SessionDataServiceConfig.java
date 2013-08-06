package wint.session.config;

import java.util.Set;

import wint.lang.utils.CollectionUtil;
import wint.session.data.SessionDataService;
import wint.session.serialize.SerializeService;

public class SessionDataServiceConfig {
	
	private Class<? extends SessionDataService> sessionDataServiceClass;
	
	private SessionStoreConfig sessionStoreConfig;
	
	private SerializeService serializeService;
	
	private Set<SessionDataConfig> sessionDataConfigs = CollectionUtil.newHashSet();
	
	public Class<? extends SessionDataService> getSessionDataServiceClass() {
		return sessionDataServiceClass;
	}

	public void setSessionDataServiceClass(Class<? extends SessionDataService> sessionDataServiceClass) {
		this.sessionDataServiceClass = sessionDataServiceClass;
	}

	public Set<SessionDataConfig> getSessionDataConfigs() {
		return sessionDataConfigs;
	}

	public void setSessionDataConfigs(Set<SessionDataConfig> sessionDataConfigs) {
		this.sessionDataConfigs = sessionDataConfigs;
	}

	public SerializeService getSerializeService() {
		return serializeService;
	}

	public void setSerializeService(SerializeService serializeService) {
		this.serializeService = serializeService;
	}

	public SessionStoreConfig getSessionStoreConfig() {
		return sessionStoreConfig;
	}

	public void setSessionStoreConfig(SessionStoreConfig sessionStoreConfig) {
		this.sessionStoreConfig = sessionStoreConfig;
	}

}
