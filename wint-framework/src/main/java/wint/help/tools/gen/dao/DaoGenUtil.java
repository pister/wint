package wint.help.tools.gen.dao;

import wint.lang.magic.MagicClass;
import wint.lang.magic.Property;
import wint.lang.template.SimpleVelocityEngine;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
