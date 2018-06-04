package wint.mvc.template.remote;

import wint.lang.utils.StringUtil;

/**
 * Created by songlihuang on 2018/6/4.
 */
public class PathUtil {

    public static boolean isRemotePath(String path) {
        if (StringUtil.isEmpty(path)) {
            return false;
        }
        path = path.toLowerCase();
        if (path.startsWith("http:") || path.startsWith("https:") || path.startsWith("ftp:")) {
            return true;
        }
        return false;
    }
}
