package wint.mvc.template.remote;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Created by songlihuang on 2018/6/4.
 */
public class ExpiredFile {

    private File file;

    private LocalDateTime expire;

    public ExpiredFile(File file, LocalDateTime expire) {
        this.file = file;
        this.expire = expire;
    }

    public File getFile() {
        return file;
    }

    public boolean isExpired() {
        if (expire == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(expire);
    }
}
