package wint.session.data.cookie;

import wint.session.config.SessionConfigKeys;
import wint.session.config.SessionConfig;
import wint.session.config.SessionStoreConfig;
import wint.session.util.WintSessionContants;

public class CookieSessionStoreConfig implements SessionStoreConfig {
	
	private String prefixName = WintSessionContants.WINT_COOKIE_SESSION_DATA_NAME_PREFIX;
	
	private String domain;
	
	private String dataSeparate = ";";
	
	private String encryptKey = "testkey";

	private int cookieDataMaxSize = WintSessionContants.WINT_COOKIE_DATA_MAX_SIZE;
	
	private boolean encrypt = true;

	public void init(SessionConfig sessionConfig) {
		this.encryptKey = sessionConfig.getParameters().getString(SessionConfigKeys.COOKIE_ENCRYPTKEY, "testkey");
		this.encrypt = sessionConfig.getParameters().getBoolean(SessionConfigKeys.COOKIE_ENCRYPT_USE, true);
		this.prefixName = sessionConfig.getParameters().getString(SessionConfigKeys.COOKIE_PREFIXNAME, WintSessionContants.WINT_COOKIE_SESSION_DATA_NAME_PREFIX);
		this.domain = sessionConfig.getParameters().getString(SessionConfigKeys.COOKIE_DOMAIN, WintSessionContants.WINT_COOKIE_SESSION_DOMAIN);
		this.dataSeparate = sessionConfig.getParameters().getString(SessionConfigKeys.COOKIE_DATASEPARATE, ";");
		this.cookieDataMaxSize = sessionConfig.getParameters().getInt(SessionConfigKeys.COOKIE_COOKIEDATAMAXSIZE, WintSessionContants.WINT_COOKIE_DATA_MAX_SIZE);
		
	}

	public String getPrefixName() {
		return prefixName;
	}

	public String getEncryptKey() {
		return encryptKey;
	}

	public String getDataSeparate() {
		return dataSeparate;
	}

	public int getCookieDataMaxSize() {
		return cookieDataMaxSize;
	}

	public boolean isEncrypt() {
		return encrypt;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}
