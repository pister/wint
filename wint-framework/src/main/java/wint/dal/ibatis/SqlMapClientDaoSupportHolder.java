package wint.dal.ibatis;


import wint.dal.ibatis.spring.support.SqlMapClientDaoSupport;

/**
 * User: huangsongli
 * Date: 15/3/4
 * Time: 下午4:48
 */
public class SqlMapClientDaoSupportHolder {

    private static final ThreadLocal<SqlMapClientDaoSupport> sqlMapClientDaoSupportHolder = new ThreadLocal<SqlMapClientDaoSupport>();

    public static void set(SqlMapClientDaoSupport sqlMapClientDaoSupport) {
        sqlMapClientDaoSupportHolder.set(sqlMapClientDaoSupport);
    }

    public static SqlMapClientDaoSupport get() {
       return sqlMapClientDaoSupportHolder.get();
    }



}
