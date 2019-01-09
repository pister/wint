package wint.sessionx.provider.cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.lang.WintException;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.UrlUtil;
import wint.sessionx.cookie.WintCookie;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.SessionData;
import wint.sessionx.util.BlowFishUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static wint.sessionx.constants.SpecSessionKeys.LAST_ACCESSED_TIME;
import static wint.sessionx.constants.SpecSessionKeys.MAX_INACTIVE_INTERVAL;

/**
 * Created by pister on 14-2-26.
 */
public class CookieCodec {

    private static final Logger log = LoggerFactory.getLogger(CookieCodec.class);

    private CookieSessionConfig config;

    private SerializeService serializeService;

    public CookieCodec(CookieSessionConfig config,  SerializeService serializeService) {
        this.config = config;
        this.serializeService = serializeService;
    }

    private String decodeCookie(String value) {
        if (config.isEncrypt()) {
            return BlowFishUtils.decryptBlowfish(value, config.getEncryptKey());
        } else {
            return value;
        }
    }

    private String encodeCookie(String value) {
        if (config.isEncrypt()) {
            return BlowFishUtils.encryptBlowfish(value, config.getEncryptKey());
        } else {
            return value;
        }
    }

    private String encodeName(String name) {
        return UrlUtil.encode(name, CookieConstants.URL_ENCODE_CHARSET);
    }

    private String decodeName(String name) {
        return UrlUtil.decode(name, CookieConstants.URL_ENCODE_CHARSET);
    }

    private boolean isSessionExpired(Map<String, SessionData> sessionDataMap) {
        SessionData inactiveSessionData = sessionDataMap.get(MAX_INACTIVE_INTERVAL);
        if (inactiveSessionData == null) {
            return true;
        }
        long expireInSeconds = ((Number)inactiveSessionData.getData()).longValue();
        if (expireInSeconds <= 0) {
            return false;
        }

        SessionData lastAccessSessionData = sessionDataMap.get(LAST_ACCESSED_TIME);
        if (lastAccessSessionData == null) {
            return true;
        }
        long lastAccessInMs = ((Number)lastAccessSessionData.getData()).longValue();
        if (lastAccessInMs + expireInSeconds * 1000 < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public Map<String, SessionData> parseData(String value) {
        String rawData = decodeCookie(value);
        if (rawData == null) {
            log.warn("cookie decrypt failed.");
            return null;
        }
        if (!rawData.startsWith(CookieConstants.WINT_COOKIE_MAGIC_TOKEN)) {
            return null;
        }
        Map<String, SessionData> targetData = MapUtil.newHashMap();
        rawData = StringUtil.getFirstAfter(rawData, CookieConstants.WINT_COOKIE_MAGIC_TOKEN);
        List<String> dataList = StringUtil.splitTrim(rawData, config.getDataSeparate());
        for (String data : dataList) {
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

        if (isSessionExpired(targetData)) {
            return null;
        }

        return targetData;
    }


    public WintCookie buildCookie(CookieSessionStore cookieSessionStore) {
        Map<String, SessionData> needOutputData = MapUtil.newHashMap(cookieSessionStore.getOrgData());
        needOutputData.putAll(cookieSessionStore.getUpdatedData());

        Set<String> needDeleteNames = CollectionUtil.newHashSet(cookieSessionStore.getToBeDeleteNames());
        for (String needDeleteName : needDeleteNames) {
            needOutputData.remove(needDeleteName);
        }

        List<String> cookieStrings = CollectionUtil.newArrayList();

        // need update
        for (Map.Entry<String, SessionData> entry : needOutputData.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(encodeName(entry.getKey()));
            sb.append("=");
            String data = serializeService.serialize(entry.getValue().getData()).toString();
            sb.append(data);
            cookieStrings.add(sb.toString());
        }

        String cookieValue = CollectionUtil.join(cookieStrings, config.getDataSeparate());
        String encodeCookieValue = encodeCookie(CookieConstants.WINT_COOKIE_MAGIC_TOKEN + cookieValue);
        if (encodeCookieValue == null) {
            return null;
        }
        if (encodeCookieValue.getBytes().length > config.getCookieDataMaxSize()) {
            // cookie data的内容太大了
            throw new WintException("too long session data size!");
        }

        WintCookie ret = new WintCookie(config.getPrefixName() + config.getCookieDataIndex(), encodeCookieValue);

        String domain = config.getDomain();
        if (!StringUtil.isEmpty(domain)) {
            ret.setDomain(domain);
        }
        ret.setHttpOnly(true);
        ret.setPath(config.getPath());

        return ret;
    }
}
