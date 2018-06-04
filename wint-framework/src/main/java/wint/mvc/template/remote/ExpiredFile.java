package wint.mvc.template.remote;

import wint.lang.utils.DateUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by songlihuang on 2018/6/4.
 */
public class ExpiredFile {

    private File file;

    private Date expire;

    public ExpiredFile(File file, Date expire) {
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
        return DateUtil.isDateAfter(new Date(), expire);
    }
}
