package wint.dal.ibatis.ext.ex;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;

/**
 * Created by songlihuang on 2022/9/8.
 */
public class CommonAccessException extends DataAccessException {

    private static final long serialVersionUID = -1794346608555567091L;

    public CommonAccessException(String msg) {
        super(msg);
    }

    public CommonAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
