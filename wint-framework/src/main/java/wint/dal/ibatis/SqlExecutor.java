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
public class SqlExecutor {

    private ReadWriteSqlMapClientSource readWriteSqlMapClientSource;

    public SqlExecutor(ReadWriteSqlMapClientSource readWriteSqlMapClientSource) {
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

    public Object execute(SqlMapClientCallback action) {
        return getWriteSqlMapClientTemplate().execute(action);
    }

    public Object queryForObject(String statementName) {
        return getReadSqlMapClientTemplate().queryForObject(statementName);
    }

    public Object queryForObject(String statementName, Object parameterObject) {
        return getReadSqlMapClientTemplate().queryForObject(statementName, parameterObject);
    }

    public List queryForList(String statementName) {
        return getReadSqlMapClientTemplate().queryForList(statementName);
    }

    public List queryForList(String statementName, Object parameterObject) {
        return getReadSqlMapClientTemplate().queryForList(statementName, parameterObject);
    }

    public Object insert(String statementName) {
        return getWriteSqlMapClientTemplate().insert(statementName);
    }

    public Object insert(String statementName, Object parameterObject) {
        return getWriteSqlMapClientTemplate().insert(statementName, parameterObject);
    }

    public int update(String statementName) {
        return getWriteSqlMapClientTemplate().update(statementName);
    }

    public int update(String statementName, Object parameterObject) {
        return getWriteSqlMapClientTemplate().update(statementName, parameterObject);
    }

    public int delete(String statementName) {
        return getWriteSqlMapClientTemplate().delete(statementName);
    }

    public int delete(String statementName, Object parameterObject) {
        return getWriteSqlMapClientTemplate().delete(statementName, parameterObject);
    }

}
