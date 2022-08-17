package wint.dal.ibatis.ext.handlers;

import com.ibatis.sqlmap.engine.type.BaseTypeHandler;
import com.ibatis.sqlmap.engine.type.SimpleDateFormatter;
import com.ibatis.sqlmap.engine.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Created by songlihuang on 2022/8/17.
 */
public class LocalDateTypeHandler extends BaseTypeHandler implements TypeHandler {

    private static final String DATE_FORMAT = "yyyy/MM/dd";

    public void setParameter(PreparedStatement ps, int i, Object parameter, String jdbcType)
            throws SQLException {
        ps.setObject(i, parameter);
    }

    public Object getResult(ResultSet rs, String columnName)
            throws SQLException {
        Object sqlDate = rs.getObject(columnName, LocalDate.class);
        if (rs.wasNull()) {
            return null;
        } else {
            return sqlDate;
        }
    }

    public Object getResult(ResultSet rs, int columnIndex)
            throws SQLException {
        Object sqlDate = rs.getObject(columnIndex, LocalDate.class);
        if (rs.wasNull()) {
            return null;
        } else {
            return sqlDate;
        }
    }

    public Object getResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Object sqlDate = cs.getObject(columnIndex, LocalDate.class);
        if (cs.wasNull()) {
            return null;
        } else {
            return sqlDate;
        }
    }

    public Object valueOf(String s) {
       // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return SimpleDateFormatter.format(DATE_FORMAT, s);
    }
}