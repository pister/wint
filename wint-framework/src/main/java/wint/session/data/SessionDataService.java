package wint.session.data;

import java.util.Set;

import wint.session.config.SessionDataConfig;
import wint.session.config.SessionStoreConfig;
import wint.session.serialize.SerializeService;
import wint.session.servlet.WintSessionHttpServletRequest;
import wint.session.servlet.WintSessionHttpServletResponse;

public interface SessionDataService {
	
	void set(String name, SessionData data);
	
	SessionData get(String name);
	
	void remove(String name);
	
	Set<String> getNames();
	
	void invalidate();
	
	String getSessionId();
	
	String init(WintSessionHttpServletRequest wintSessionHttpServletRequest, WintSessionHttpServletResponse wintSessionHttpServletResponse, Set<SessionDataConfig> sessionDataConfigs, SessionStoreConfig sessionStoreConfig);
	
	void commit(WintSessionHttpServletRequest wintSessionHttpServletRequest, WintSessionHttpServletResponse wintSessionHttpServletResponse, int expire);
	
	void setSerializeService(SerializeService serializeService);
	
}
