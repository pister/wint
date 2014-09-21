package wint.help.sql.datasource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * User: longyi
 * Date: 14-2-7
 * Time: 下午8:45
 */
public class SimpleDataSource implements DataSource {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driverClassName).newInstance();
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
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

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    // override for jdk7 compiler.
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

}
