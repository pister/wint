package wint.session.store;

import java.util.Set;

import wint.session.config.SessionConfig;

public interface SessionStore {
	
	Object getAttribute(String name);
	
	void setAttribute(String name, Object value);
	
	void removeAttribute(String name);
	
	void invalidate();
	
	Set<String> getAttributeNames();
	
	void setMaxInactiveInterval(int interval);

	int getMaxInactiveInterval();
	
	boolean isNew();
	
	long getCreateTime();

	void setCreateTime(long createTime);
	
	long getLastAccessedTime();

	void setLastAccessedTime(long lastAccessedTime);
	
	String getSessionId();

	void init(SessionConfig sessionConfig);
	
	void commit();

}
