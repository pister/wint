package wint.help.sql;

import junit.framework.TestCase;
import wint.help.sql.datasource.SimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 CREATE DATABASE `foo1` default character set utf8mb4 collate utf8_general_ci;
 use `foo1`;

 grant select,update,delete,insert on `foo1`.* to 'foo1name'@'%' identified by 'foo1pass';
 grant select,update,delete,insert on `foo1`.* to 'foo1name'@'localhost' identified by 'foo1pass';
 flush privileges;


 CREATE TABLE `table1` (
 `id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
 `name` varchar(64)  not null ,
 `time` datetime not null
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
 */
public class SqlExecutorTest extends TestCase {

    private DataSource dataSource;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/foo1?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true&useSSL=false");
        dataSource.setUsername("foo1name");
        dataSource.setPassword("foo1pass");
        this.dataSource = dataSource;
    }

    public void testQuery() throws SQLException {
        Connection connection = dataSource.getConnection();
        SqlExecutor.executeQuery(connection, new RowProcessor<Object>() {
            @Override
            public Object processRow(ResultSet rs) throws SQLException {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                LocalDateTime time = rs.getObject("time", LocalDateTime.class);
                System.out.println("id:" + id + ", name:" + name + ", time:" + time);
                return null;
            }
        }, "select * from table1 where id > ? limit 10", 2);
    }

    public void testInsert() throws SQLException {
        Connection connection = dataSource.getConnection();
        String name = "拉了";
        int rows = SqlExecutor.executeUpdate(connection, "insert into table1(name, `time`) values(?, ?)", name, LocalDateTime.now());
        System.out.println(rows);
    }

}