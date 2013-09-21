package wint.help.tools.ibatis.gen;

import wint.lang.utils.ClassUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

import java.io.IOException;
import java.io.Writer;

/**
 * User: longyi
 * Date: 13-9-21
 * Time: 上午7:36
 */
public class DaoGenUtil {

    public static String getDoAlias(Class<?> clazz) {
        String name = ClassUtil.getShortClassName(clazz);
        name = StringUtil.lowercaseFirstLetter(name);
        if (name.endsWith("DO") || name.endsWith("Do")) {
            return name.substring(0, name.length() - 2);
        } else {
            return name;
        }
    }

    public static void writeLine(Writer out) {
        try {
            out.write(SystemUtil.LINE_SEPARATOR);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
