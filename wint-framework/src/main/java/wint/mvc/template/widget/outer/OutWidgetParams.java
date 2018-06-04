package wint.mvc.template.widget.outer;

/**
 * Created by songlihuang on 2018/6/4.
 */
public class OutWidgetParams {

    private String localTempBasePath;

    private int expireInSeconds;

    private String templateBasePath;

    private boolean remoteFailUseCache;

    public String getLocalTempBasePath() {
        return localTempBasePath;
    }

    public void setLocalTempBasePath(String localTempBasePath) {
        this.localTempBasePath = localTempBasePath;
    }

    public int getExpireInSeconds() {
        return expireInSeconds;
    }

    public void setExpireInSeconds(int expireInSeconds) {
        this.expireInSeconds = expireInSeconds;
    }

    public String getTemplateBasePath() {
        return templateBasePath;
    }

    public void setTemplateBasePath(String templateBasePath) {
        this.templateBasePath = templateBasePath;
    }

    public boolean isRemoteFailUseCache() {
        return remoteFailUseCache;
    }

    public void setRemoteFailUseCache(boolean remoteFailUseCache) {
        this.remoteFailUseCache = remoteFailUseCache;
    }
}
