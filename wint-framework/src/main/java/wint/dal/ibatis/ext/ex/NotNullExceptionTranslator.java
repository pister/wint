package wint.dal.ibatis.ext.ex;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.lang.Nullable;

import java.sql.SQLException;

/**
 * Created by songlihuang on 2022/9/8.
 */
public class NotNullExceptionTranslator implements SQLExceptionTranslator {

    private SQLExceptionTranslator targetTranslator;

    public NotNullExceptionTranslator(SQLExceptionTranslator targetTranslator) {
        this.targetTranslator = targetTranslator;
    }

    @Override
    public DataAccessException translate(String task, @Nullable String sql, SQLException ex) {
        DataAccessException e = targetTranslator.translate(task, sql, ex);
        if (e != null) {
            return e;
        }
        return new CommonAccessException("Error", ex);
    }
}
