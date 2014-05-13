package wint.help.sql;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;

import wint.help.sql.TypeUtil.MethodPair;
import wint.lang.WintException;

/**
 * @author pister
 * 2011-1-24
 */
public class Binder {

    /**
     * @param pstmt
     * @param args
     */
    public static void bindParameters(PreparedStatement pstmt, Object... args) {
        if (args == null || args.length == 0) {
            return;
        }
        int i = 1;
        for (Object arg : args) {
            try {
                if (arg == null) {
                    pstmt.setString(i++, null);
                    continue;
                } 
                
                MethodPair methodPair = TypeUtil.getMethodPair(arg.getClass());
                if (methodPair == null) {
                    throw new WintException("未找到方法:" + arg.getClass());
                }
                Method setter = methodPair.getSetter();
                setter.invoke(pstmt, i++, arg);
            } catch (Exception e) {
                throw new WintException(e);
            }
        }
    }
    
}
