package wint.sessionx.provider.cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.lang.WintException;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.UrlUtil;
import wint.sessionx.constants.SpecSessionKeys;
import wint.sessionx.cookie.WintCookie;
import wint.sessionx.serialize.SerializeService;
import wint.sessionx.store.SessionData;
import wint.sessionx.util.BlowFishUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
            return BlowFishUtil.decryptBlowfish(value, config.getEncryptKey());
        } else {
            return value;
        }
    }

    private String encodeCookie(String value) {
        if (config.isEncrypt()) {
            return BlowFishUtil.encryptBlowfish(value, config.getEncryptKey());
        } else {
            return value;
        }
    }

    private String encodeName(String name) {
        return UrlUtil.encode(name, CookieContants.URL_ENCODE_CHARSET);
    }

    private String decodeName(String name) {
        return UrlUtil.decode(name, CookieContants.URL_ENCODE_CHARSET);
    }

    public Map<String, SessionData> parseData(String value) {
        String rawData = decodeCookie(value);
        if (rawData == null) {
            log.warn("cookie decrypt failed.");
            return null;
        }
        if (!rawData.startsWith(CookieContants.WINT_COOKIE_MAGIC_TOKEN)) {
            return null;
        }
        Map<String, SessionData> targetData = MapUtil.newHashMap();
        rawData = StringUtil.getFirstAfter(rawData, CookieContants.WINT_COOKIE_MAGIC_TOKEN);
        List<String> datas = StringUtil.splitTrim(rawData, config.getDataSeparate());
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
        return targetData;
    }


    public WintCookie buildCookie(int index, CookieSessionStore cookieSessionStore) {
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
        String encodeCookieValue = encodeCookie(CookieContants.WINT_COOKIE_MAGIC_TOKEN + cookieValue);
        if (encodeCookieValue == null) {
            return null;
        }
        if (encodeCookieValue.getBytes().length > config.getCookieDataMaxSize()) {
            // cookie data的内容太大了
            throw new WintException("too long session data size!");
        }

        WintCookie ret = new WintCookie(config.getPrefixName() + index, encodeCookieValue);

        String domain = config.getDomain();
        if (!StringUtil.isEmpty(domain)) {
            ret.setDomain(domain);
        }
        ret.setHttpOnly(true);
        ret.setMaxAge(config.getExpire());
        ret.setPath(config.getPath());

        return ret;
    }
}
