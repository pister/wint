package wint.dal.ibatis;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import java.util.List;

/**
 * User: huangsongli
 * Date: 16/4/28
 * Time: 上午10:47
 */
public interface SqlExecutor {
    Object execute(SqlMapClientCallback action);

    Object queryForObject(String statementName);

    Object queryForObject(String statementName, Object parameterObject);

    List queryForList(String statementName);

    List queryForList(String statementName, Object parameterObject);

    Object insert(String statementName);

    Object insert(String statementName, Object parameterObject);

    int update(String statementName);

    int update(String statementName, Object parameterObject);

    int delete(String statementName);

    int delete(String statementName, Object parameterObject);
}
