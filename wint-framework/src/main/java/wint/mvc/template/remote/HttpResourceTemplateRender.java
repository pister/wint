package wint.mvc.template.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.core.config.Constants;
import wint.lang.io.FastByteArrayOutputStream;
import wint.lang.utils.FileUtil;
import wint.lang.utils.IoUtil;
import wint.lang.utils.LocalDateTimeUtil;
import wint.mvc.template.Context;
import wint.mvc.template.TemplateRender;
import wint.mvc.template.engine.TemplateEngine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;

/**
 * Created by songlihuang on 2018/6/1.
 */
public class HttpResourceTemplateRender implements TemplateRender {

    private static final Logger log = LoggerFactory.getLogger(HttpResourceTemplateRender.class);

    private String path;

    private TemplateEngine viewRenderEngine;

    private Context context;

    private int expireInSeconds = 60;

    private String baseUrl;

    private boolean remoteFailUseCache;

    private File baseTempDir = new File(Constants.Defaults.WINT_OUTER_TEMPLATE_TEMP_PATH);

    public HttpResourceTemplateRender(String path, TemplateEngine viewRenderEngine, Context context, String baseUrl) {
        super();
        this.path = path;
        this.viewRenderEngine = viewRenderEngine;
        this.context = context;
        this.baseUrl = baseUrl;
    }

    public String render() {
        return viewRenderEngine.render(this, context);
    }

    @Override
    public String getPath() {
        try {
            // 1. check path exist, if not, download it than return
            ExpiredFile expiredFile = getExpiredFile();
            if (expiredFile == null) {
                syncTemplateFromRemote();
                return path;
            }
            // 2. if path exist, the the ${path}.last_time file, if the content is too old,
            if (!expiredFile.isExpired()) {
                return path;
            }
            // than try download again, and update content
            try {
                syncTemplateFromRemote();
            } catch (IOException e) {
                log.error("error", e);
                if (remoteFailUseCache) {
                    return path;
                } else {
                    throw new RuntimeException(e);
                }
            }
            return path;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected byte[] requestFromRemote(String templateUrl) throws IOException {
        URL url = new URL(templateUrl);
        URLConnection conn = url.openConnection();
        conn.setReadTimeout(3000);
        conn.setConnectTimeout(3000);
        InputStream is = conn.getInputStream();
        FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
        try {
            IoUtil.copy(is, bos);
        } finally {
            IoUtil.close(is);
        }
        return bos.toByteArray();
    }


    private void syncTemplateFromRemote() throws IOException {
        File targetFile = new File(baseTempDir, path);
        File targetParentFile = targetFile.getParentFile();
        if (!targetParentFile.exists()) {
            targetParentFile.mkdirs();
        }
        String url = baseUrl + "/" + path;
        byte[] data = requestFromRemote(url);

        FileUtil.writeContent(targetFile, data);
        File expireFile = new File(baseTempDir, expireFilePath(path));
        FileUtil.writeContent(expireFile, LocalDateTimeUtil.format(LocalDateTime.now().plusSeconds(expireInSeconds), DATE_FORMAT));
    }


    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String expireFilePath(String path) {
        return path + ".last_time";
    }


    private ExpiredFile getExpiredFile() throws IOException {
        // TODO add cache : key -> path, value -> ExpiredFile
        return getExpiredFileImpl();
    }

    private ExpiredFile getExpiredFileImpl() throws IOException {
        if (!baseTempDir.exists()) {
            return null;
        }
        File targetFile = new File(baseTempDir, path);
        if (!targetFile.exists()) {
            return null;
        }
        File expireFile = new File(baseTempDir, expireFilePath(path));
        if (!expireFile.exists()) {
            return null;
        }
        String stringDate = FileUtil.readAsString(expireFile);
        LocalDateTime expire = LocalDateTimeUtil.parseDateTime(stringDate, DATE_FORMAT);
        return new ExpiredFile(targetFile, expire);
    }

    public void setRemoteFailUseCache(boolean remoteFailUseCache) {
        this.remoteFailUseCache = remoteFailUseCache;
    }

    public void setTempDir(String tempDir) {
        this.baseTempDir = new File(tempDir);
    }

    public void setExpireInSeconds(int expireInSeconds) {
        this.expireInSeconds = expireInSeconds;
    }
}
