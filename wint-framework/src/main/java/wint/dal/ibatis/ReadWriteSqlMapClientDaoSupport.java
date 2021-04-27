package wint.dal.ibatis;

import org.springframework.beans.factory.InitializingBean;
import wint.dal.MasterForcer;

/**
 * User: pister
 * Date: 13-7-6
 * Time: 下午9:54
 */
public class ReadWriteSqlMapClientDaoSupport implements InitializingBean {

    private ReadWriteSqlMapClientSource readWriteSqlMapClientSource;

    protected SqlExecutor sqlMapExecutor;

    public void afterPropertiesSet() throws Exception {
        sqlMapExecutor = new DefaultSqlExecutor(readWriteSqlMapClientSource);
    }

    public SqlExecutor getSqlExecutor() {
        return  sqlMapExecutor;
    }

    public SqlExecutor getSqlMapClientTemplate() {
        return getSqlExecutor();
    }

    public void setReadWriteSqlMapClientSource(ReadWriteSqlMapClientSource readWriteSqlMapClientSource) {
        this.readWriteSqlMapClientSource = readWriteSqlMapClientSource;
    }
}
