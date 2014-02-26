package wint.session.config;

import java.util.List;

import wint.lang.magic.MagicMap;


public interface SessionConfig {

	public int getExpire();

	public List<SessionDataServiceConfig> getSessionDataServiceConfigs();

	public MagicMap getParameters();

}
