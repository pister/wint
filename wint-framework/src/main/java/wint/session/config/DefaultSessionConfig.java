package wint.session.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.lang.WintException;
import wint.lang.magic.MagicMap;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.FileUtil;
import wint.lang.utils.StreamUtil;
import wint.session.data.cookie.CookieSessionStoreConfig;
import wint.session.data.cookie.CookieStoreSessionDataService;
import wint.session.serialize.StringSerializeService;
import wint.session.util.WintSessionContants;

public class DefaultSessionConfig implements SessionConfig {
	
	static final transient Logger log = LoggerFactory.getLogger(DefaultSessionConfig.class);
	
	static final String COOKIE_SESSION_ENCRYPT_KEY_FILENAME = "wint-session-key";

	static final String DEFAULT_COOKIE_SESSION_KEY = "default_key";
	
	/**
	 * 默认的session超时时间(秒)
	 */
	private int expire = WintSessionContants.WINT_COOKIE_SESSION_EXPIRE;
	
	private String sessionIdName = WintSessionContants.SESSION_ID_NAME;
	
	private List<SessionDataServiceConfig> sessionDataServiceConfigs = CollectionUtil.newArrayList();

	private CookieSessionStoreConfig cookieSessionStoreConfig = new CookieSessionStoreConfig();
	
	private MagicMap parameters = MagicMap.newMagicMap();
	
	private void initParametersFromServlet(MagicMap initParamters) {
		parameters.putAll(initParamters);
	}
	
	private void appendDefaultParameters(String sessionCookieKeyPath) {
		if (!parameters.containsKey(SessionConfigKeys.COOKIE_ENCRYPTKEY)) {
			parameters.put(SessionConfigKeys.COOKIE_ENCRYPTKEY, loadCookieSessionKey(sessionCookieKeyPath));
		}
	}
	
	public void init(MagicMap initParamters) {
		initParametersFromServlet(initParamters);
		
		String sessionCookieKeyPath = parameters.getString(SessionConfigKeys.COOKIE_ENCRYPTKEY_PATH, COOKIE_SESSION_ENCRYPT_KEY_FILENAME);

        expire = parameters.getInt(SessionConfigKeys.COOKIE_EXPIRE, WintSessionContants.WINT_COOKIE_SESSION_EXPIRE);

		appendDefaultParameters(sessionCookieKeyPath);
		
		cookieSessionStoreConfig.init(this);
		
		SessionDataServiceConfig cookieSessionDataServiceConfig = new SessionDataServiceConfig();
		
		cookieSessionDataServiceConfig.setSessionStoreConfig(cookieSessionStoreConfig);
		cookieSessionDataServiceConfig.setSessionDataServiceClass(CookieStoreSessionDataService.class);
		cookieSessionDataServiceConfig.setSerializeService(new StringSerializeService());
		
		sessionDataServiceConfigs.add(cookieSessionDataServiceConfig);
	}
	
	/**
	 * 1、用户指定路径
	 * 2、$user.home/wint-session-key
	 * 3、应用下 wint-session-key 文件
	 * 4、默认值
	 * 
	 * @param sessionCookieKeyPath
	 * @return
	 */
	private String loadCookieSessionKey(String sessionCookieKeyPath) {
		try {
			InputStream is = getSessionCookieKeyInputStream(sessionCookieKeyPath);
			if (is == null) {
				log.warn("no wint-session-key file found, use default cookie session key!!!");
				return DEFAULT_COOKIE_SESSION_KEY;
			} else {
				return StreamUtil.readAsString(is);
			}
		} catch (IOException e) {
			throw new WintException(e);
		}
	}
	
	private InputStream getSessionCookieKeyInputStream(String sessionCookieKeyPath) throws IOException {
		if (FileUtil.existFile(sessionCookieKeyPath)) {
			log.warn("session key load from: " + sessionCookieKeyPath);
			return new FileInputStream(sessionCookieKeyPath);
		}
		InputStream is = StreamUtil.getUserHomeResourceAsStream(sessionCookieKeyPath);
		if (is != null) {
			log.warn("session key load from: " + new File(new File(System.getProperty("user.home")), sessionCookieKeyPath));
			return is;
		}
		is = StreamUtil.getUserHomeResourceAsStream(COOKIE_SESSION_ENCRYPT_KEY_FILENAME);
		if (is != null) {
			log.warn("session key load from resource: " + new File(new File(System.getProperty("user.home")), COOKIE_SESSION_ENCRYPT_KEY_FILENAME));
			return is;
		}
		is =  WebResourceUtil.getWebResouceAsStream(COOKIE_SESSION_ENCRYPT_KEY_FILENAME);
		if (is != null) {
			log.warn("session key load from resource: " + COOKIE_SESSION_ENCRYPT_KEY_FILENAME);
			return is;
		}
		return is;
	}
	
	public int getExpire() {
		return expire;
	}

	public List<SessionDataServiceConfig> getSessionDataServiceConfigs() {
		return sessionDataServiceConfigs;
	}

	public String getSessionIdName() {
		return sessionIdName;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public void setSessionIdName(String sessionIdName) {
		this.sessionIdName = sessionIdName;
	}

	public void setSessionDataServiceConfigs(List<SessionDataServiceConfig> sessionDataServiceConfigs) {
		this.sessionDataServiceConfigs = sessionDataServiceConfigs;
	}

	public MagicMap getParameters() {
		return parameters;
	}


}
