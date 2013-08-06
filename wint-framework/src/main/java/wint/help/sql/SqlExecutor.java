package wint.help.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wint.lang.WintException;

/**
 * @author pister
 * 2012-9-11 下午3:10:09
 */
public class SqlExecutor {
	
	 public static int executeUpdate(Connection conn, String sql, Object... args) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            Binder.bindParameters(pstmt, args);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
        	 throw new WintException(e);
        }finally {
            SqlUtil.close(pstmt);
            SqlUtil.close(conn);
        }
    }
	
    public static <T> int executeQuery(Connection conn, RowProcessor<T> rowProcessor, String sql, Object... args) {
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	int count = 0;
    	try {
    		pstmt = conn.prepareStatement(sql);
    		Binder.bindParameters(pstmt, args);
    		rs = pstmt.executeQuery();
    		while (rs.next()) {
    			rowProcessor.processRow(rs);
    			++count;
    		}
    	} catch (SQLException e) {
       	 throw new WintException(e);
       }finally {
    		SqlUtil.close(rs);
    		SqlUtil.close(pstmt);
    		SqlUtil.close(conn);
    	}
    	return count;
    }
    
	public static <T> T queryForObject(Connection conn, RowProcessor<T> rowProcessor, String sql, Object... args) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			Binder.bindParameters(pstmt, args);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rowProcessor.processRow(rs);
			}
			return null;
		} catch (SQLException e) {
			throw new WintException(e);
		} finally {
			SqlUtil.close(rs);
			SqlUtil.close(pstmt);
			SqlUtil.close(conn);
		}
	}
	
	public static <T> List<T> queryForList(Connection conn, final RowProcessor<T> rowProcessor, String sql, Object... args) {
		final List<T> ret = new ArrayList<T>();
		executeQuery(conn, new RowProcessor<T>() {
			public T processRow(ResultSet rs) throws SQLException {
				T t = rowProcessor.processRow(rs);
				ret.add(t);
				return t;
			}
		}, sql, args);
		return ret;
	}
    
    public static long count(Connection conn, String tableName) {
    	String sql = "select count(0) from " + tableName;
    	Long result = queryForObject(conn, new RowProcessor<Long>() {
			public Long processRow(ResultSet rs) throws SQLException {
				return rs.getLong(1);
			}
    	}, sql);
    	return result == null ? 0 : result.longValue();
    }
    
}
