package wint.help.sql.connection;

import java.sql.Connection;

/**
 * 2012-9-11 下午3:22:13
 */
public class ConnectionUtil {

	/**
	 * 返回不能被close关闭的connection
	 * @param conn
	 * @return
	 */
	public static Connection notCloseConnection(Connection conn) {
		return new NotCloseConnection(conn);
	}
	
}
