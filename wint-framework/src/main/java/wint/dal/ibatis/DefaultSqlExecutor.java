package wint.dal.ibatis;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import wint.dal.MasterForcer;

import java.util.List;

/**
 * User: pister
 * Date: 13-7-7
 * Time: 上午11:16
 */
public class DefaultSqlExecutor implements SqlExecutor {

    private ReadWriteSqlMapClientSource readWriteSqlMapClientSource;

    public DefaultSqlExecutor(ReadWriteSqlMapClientSource readWriteSqlMapClientSource) {
        this.readWriteSqlMapClientSource = readWriteSqlMapClientSource;
    }

    protected SqlMapClientTemplate getWriteSqlMapClientTemplate() {
        SqlMapClientTemplate sqlMapClientTemplate = MasterForcer.getSqlMapClientTemplate();
        if (sqlMapClientTemplate != null) {
            return sqlMapClientTemplate;
        }
        return readWriteSqlMapClientSource.getSqlMapClient(SqlMapClientTarget.MASTER);
    }

    protected SqlMapClientTemplate getReadSqlMapClientTemplate() {
        SqlMapClientTemplate sqlMapClientTemplate = MasterForcer.getSqlMapClientTemplate();
        if (sqlMapClientTemplate != null) {
            return sqlMapClientTemplate;
        }
        return readWriteSqlMapClientSource.getSqlMapClient(SqlMapClientTarget.SLAVE);
    }

    @Override
    public Object execute(SqlMapClientCallback action) {
        return getWriteSqlMapClientTemplate().execute(action);
    }

    @Override
    public Object queryForObject(String statementName) {
        return getReadSqlMapClientTemplate().queryForObject(statementName);
    }

    @Override
    public Object queryForObject(String statementName, Object parameterObject) {
        return getReadSqlMapClientTemplate().queryForObject(statementName, parameterObject);
    }

    @Override
    public List queryForList(String statementName) {
        return getReadSqlMapClientTemplate().queryForList(statementName);
    }

    @Override
    public List queryForList(String statementName, Object parameterObject) {
        return getReadSqlMapClientTemplate().queryForList(statementName, parameterObject);
    }

    @Override
    public Object insert(String statementName) {
        return getWriteSqlMapClientTemplate().insert(statementName);
    }

    @Override
    public Object insert(String statementName, Object parameterObject) {
        return getWriteSqlMapClientTemplate().insert(statementName, parameterObject);
    }

    @Override
    public int update(String statementName) {
        return getWriteSqlMapClientTemplate().update(statementName);
    }

    @Override
    public int update(String statementName, Object parameterObject) {
        return getWriteSqlMapClientTemplate().update(statementName, parameterObject);
    }

    @Override
    public int delete(String statementName) {
        return getWriteSqlMapClientTemplate().delete(statementName);
    }

    @Override
    public int delete(String statementName, Object parameterObject) {
        return getWriteSqlMapClientTemplate().delete(statementName, parameterObject);
    }

}
