package wint.session.data.cookie;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.core.config.Constants;
import wint.lang.WintException;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.UrlUtil;
import wint.session.config.SessionDataConfig;
import wint.session.config.SessionStoreConfig;
import wint.session.cookie.WintCookie;
import wint.session.data.SessionData;
import wint.session.data.SessionDataService;
import wint.session.serialize.SerializeService;
import wint.session.servlet.WintSessionHttpServletRequest;
import wint.session.servlet.WintSessionHttpServletResponse;
import wint.session.util.BlowFishUtil;
import wint.session.util.SessionIdGenerator;
import wint.session.util.WintSessionContants;

// TODO 增加domain区分
/**
 * @author pister
 * 2012-7-22 下午06:26:19
 */
public class CookieStoreSessionDataService implements SessionDataService {

	static final String URL_ENCODE_CHARSET = Constants.Defaults.CHARSET_ENCODING;
	
	private static final Logger log = LoggerFactory.getLogger(CookieStoreSessionDataService.class);
	
	private Map<String, SessionData> orgData = MapUtil.newHashMap();
	
	private Set<String> toBeDeleteNames = CollectionUtil.newHashSet();
	
	private Map<String, SessionData> updatedData = MapUtil.newHashMap();
	
	private CookieSessionStoreConfig cookieSessionStoreConfig;
	
	private SerializeService serializeService;
	
	private String sessionIdAttributeName = "_wint_sid";
	
	private String sessionId;
	
	boolean needUpdateCookie() {
		return (!toBeDeleteNames.isEmpty()) || (!updatedData.isEmpty());
	}
	
	public void setSerializeService(SerializeService serializeService) {
		this.serializeService = serializeService;
	}

	public void set(String name, SessionData data) {
		updatedData.put(name, data);
		toBeDeleteNames.remove(name);
	}

	public SessionData get(String name) {
		if (toBeDeleteNames.contains(name)) {
			return null;
		}
		SessionData ret = updatedData.get(name);
		if (ret != null) {
			return ret;
		}
		return orgData.get(name);
	}

	public void remove(String name) {
		updatedData.remove(name);
		toBeDeleteNames.add(name);
	}

	public Set<String> getNames() {
		Set<String> ret = CollectionUtil.newHashSet();
		ret.addAll(orgData.keySet());
		ret.addAll(updatedData.keySet());
		ret.removeAll(toBeDeleteNames);
		return ret;
	}

	public void invalidate() {
		Set<String> names = getNames();
		for (String name : names) {
			remove(name);
		}
	}

	public CookieSessionStoreConfig getCookieStoreSessionConfig() {
		return cookieSessionStoreConfig;
	}

	public void setCookieStoreSessionConfig(CookieSessionStoreConfig cookieStoreSessionConfig) {
		this.cookieSessionStoreConfig = cookieStoreSessionConfig;
	}
	
	private String encodeName(String name) {
		return UrlUtil.encode(name, URL_ENCODE_CHARSET);
	}
	
	private String decodeName(String name) {
		return UrlUtil.decode(name, URL_ENCODE_CHARSET);
	}
	
	private String decodeCookie(String value) {
		if (cookieSessionStoreConfig.isEncrypt()) {
			return BlowFishUtil.decryptBlowfish(value, cookieSessionStoreConfig.getEncryptKey());
		} else {
			return value;
		}
	}

	private String encodeCookie(String value) {
		if (cookieSessionStoreConfig.isEncrypt()) {
			return BlowFishUtil.encryptBlowfish(value, cookieSessionStoreConfig.getEncryptKey());
		} else {
			return value;
		}
	}
	
	private boolean parseData(String value, Map<String, SessionData> targetData) {
		String rawData = decodeCookie(value);
		if (rawData == null) {
			log.warn("cookie decrypt failed.");
			return false;
		}
		if (!rawData.startsWith(WintSessionContants.WINT_COOKIE_MAGIC_TOKEN)) {
			return false;
		}
		rawData = StringUtil.getFirstAfter(rawData, WintSessionContants.WINT_COOKIE_MAGIC_TOKEN);
		List<String> datas = StringUtil.splitTrim(rawData, cookieSessionStoreConfig.getDataSeparate());
		for (String data : datas) {
			if (StringUtil.isEmpty(data)) {
				continue;
			}
			String name = StringUtil.getFirstBefore(data, "=");
			name = decodeName(name);
			String v = StringUtil.getFirstAfter(data, "=");
			Object obj = this.serializeService.unserialize(v);
			SessionData sessionData = new SessionData();
			sessionData.setData(obj);
			sessionData.setName(name);
			targetData.put(name, sessionData);
		}
		return true;
	}
	
	private String parseDataCookies(List<Cookie> dataCookies) {
		Map<String, SessionData> data = MapUtil.newHashMap();
		for (Cookie cookie : dataCookies) {
			String value = cookie.getValue();
			parseData(value, data);
		}
		this.orgData = data;
		SessionData sessionIdData = data.remove(sessionIdAttributeName);
		if (sessionIdData != null) {
			return (String)sessionIdData.getData();
		} else {
			return SessionIdGenerator.generateSessionId();
		}
	}
	
	private List<Cookie> filterDataCookies(Cookie[] cookies) {
		if (cookies == null) {
			return CollectionUtil.newArrayList();
		}
		String prefixName = cookieSessionStoreConfig.getPrefixName();
		List<Cookie> dataCookies = CollectionUtil.newArrayList();
		for (Cookie cookie : cookies) {
			String name = cookie.getName();
			if (StringUtil.isEmpty(name)) {
				continue;
			}
			if (!name.startsWith(prefixName)) {
				continue;
			}
			dataCookies.add(cookie);
		}
		return dataCookies;
	}
	
	
	private WintCookie buildCookie(int expire) {
		Map<String, SessionData> needOutputData = MapUtil.newHashMap(orgData);
		needOutputData.putAll(updatedData);
		
		Set<String> needDeleteNames = CollectionUtil.newHashSet(toBeDeleteNames);
		for (String needDeleteName : needDeleteNames) {
			needOutputData.remove(needDeleteName);
		}
		
		List<String> cookieStrings = CollectionUtil.newArrayList();
		
		// append session id
		needOutputData.put(sessionIdAttributeName, new SessionData(sessionIdAttributeName, sessionId));
		
		// need update
		for (Map.Entry<String, SessionData> entry : needOutputData.entrySet()) {
			StringBuilder sb = new StringBuilder();
			sb.append(encodeName(entry.getKey()));
			sb.append("=");
			String data = serializeService.serialize(entry.getValue().getData()).toString();
			sb.append(data);
			cookieStrings.add(sb.toString());
		}
		
		String cookieValue = CollectionUtil.join(cookieStrings, cookieSessionStoreConfig.getDataSeparate());
		String encodeCookieValue = encodeCookie(WintSessionContants.WINT_COOKIE_MAGIC_TOKEN + cookieValue);
		if (encodeCookieValue == null) {
			return null;
		}
		if (encodeCookieValue.getBytes().length > cookieSessionStoreConfig.getCookieDataMaxSize()) {
			// cookie data的内容太大了
			throw new WintException("too long session data size!");
		}
		
		WintCookie ret = new WintCookie(cookieSessionStoreConfig.getPrefixName() + "0", encodeCookieValue);
		
		String domain = cookieSessionStoreConfig.getDomain();
		if (!StringUtil.isEmpty(domain)) {
			ret.setDomain(domain);
		}		
		ret.setHttpOnly(true);
		ret.setMaxAge(expire);
		ret.setPath("/");
		
		return ret;
	}
	
	public String init(WintSessionHttpServletRequest wintSessionHttpServletRequest, WintSessionHttpServletResponse wintSessionHttpServletResponse, Set<SessionDataConfig> sessionDataConfigs, SessionStoreConfig sessionStoreConfig) {		
		if (sessionStoreConfig instanceof CookieSessionStoreConfig) {
			cookieSessionStoreConfig = (CookieSessionStoreConfig)sessionStoreConfig;
		}
		Cookie[] cookies = wintSessionHttpServletRequest.getCookies();
		List<Cookie> dataCookies = filterDataCookies(cookies);
		sessionId = parseDataCookies(dataCookies);
		return sessionId;
	}

	public void commit(WintSessionHttpServletRequest wintSessionHttpServletRequest, WintSessionHttpServletResponse wintSessionHttpServletResponse, int expire) {
	/*	if (!needUpdateCookie()) {
			// TODO 这么做虽然省去了重新创建cookie和传输cookie的开销，但是不会更新cookie的过期时间
			return;
		}*/
		WintCookie wintCookie = buildCookie(expire);
		if (wintCookie != null) {
			wintSessionHttpServletResponse.addWintCookie(wintCookie);
		}
	}

	public String getSessionId() {
		return sessionId;
	}

}
