package wint.lang.logback;

import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

import java.io.File;
import java.util.Map;

/**
 * Created by songlihuang on 2020/6/26.
 */
public class LogbackPathUtil {

    private static final Map<String, String> VARS = MapUtil.newHashMap();

    static {
        VARS.put("$USER_HOME", SystemUtil.USER_HOME);
    }


    private static String replaceVars(String path) {
        if (StringUtil.isEmpty(path)) {
            return path;
        }
        for (Map.Entry<String, String> entry : VARS.entrySet()) {
            path = path.replace(entry.getKey(), entry.getValue());
        }
        return path;
    }

    public static String ensureDir(String file) {
        file = LogbackPathUtil.replaceVars(file);
        File targetFile = new File(file);
        File parentFile = targetFile.getParentFile();
        parentFile.mkdirs();
        return file;
    }

}
