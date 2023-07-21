package wint.help.sql;

import wint.help.sql.TypeUtil.MethodPair;
import wint.lang.WintException;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Date;

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
                    pstmt.setNull(i++, Types.VARCHAR);
                    continue;
                } 

                if (arg.getClass().equals(Date.class)) {
                    Date d = (Date)arg;
                    arg = new java.sql.Timestamp(d.getTime());
                }


                MethodPair methodPair = TypeUtil.getMethodPair(arg.getClass());
                if (methodPair == null) {
                    pstmt.setObject(i++, arg);
                } else {
                    Method setter = methodPair.getSetter();
                    setter.invoke(pstmt, i++, arg);
                }

            } catch (Exception e) {
                throw new WintException(e);
            }
        }
    }
    
}
