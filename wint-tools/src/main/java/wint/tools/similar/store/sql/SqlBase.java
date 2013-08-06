package wint.tools.similar.store.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import wint.tools.util.SqlUtil;

public class SqlBase {
	
	private DataSource dataSource;

	protected Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static void close(Connection conn) {
		SqlUtil.close(conn);
	}
	
	protected static void close(Statement stmt) {
		SqlUtil.close(stmt);
	}
	
	protected static void close(ResultSet rs) {
		SqlUtil.close(rs);
	}

	public final void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

}
