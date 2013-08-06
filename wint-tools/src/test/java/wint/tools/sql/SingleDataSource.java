package wint.tools.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sql.DataSource;

import wint.tools.util.CollectionUtil;

public class SingleDataSource implements DataSource {

	private String url;
	
	private String driverClass;
	
	private String username;
	
	private String password;
	
	private LinkedBlockingQueue<Connection> pool;
	
	private int size = 5;
	
	public SingleDataSource(String url, String driverClass, String username, String password) {
		super();
		this.url = url;
		this.driverClass = driverClass;
		this.username = username;
		this.password = password;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void init() {
		pool = new LinkedBlockingQueue<Connection>(size);
		try {
			Class.forName(driverClass).newInstance();
			for (int i = 0; i < size; ++i) {
				pool.add(DriverManager.getConnection(url, username, password));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void destroy() {
		List<Connection> connections = CollectionUtil.newArrayList(pool);
		for (Connection conn : connections) {
			try {
				conn.close();
			} catch (SQLException e) {
				// ignore
			}
		}
		pool = null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public Connection getConnection() throws SQLException {
		try {
			return new AutoBackConnection(pool.take(), pool);
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return getConnection();
	}

}
