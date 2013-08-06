package wint.help.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author pister
 * 2012-9-11 下午3:12:15
 */
public interface RowProcessor<T> {

    T processRow(ResultSet rs) throws SQLException;
    
}
