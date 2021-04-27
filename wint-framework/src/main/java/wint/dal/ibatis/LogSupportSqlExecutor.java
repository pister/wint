package wint.dal.ibatis;

import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by songlihuang on 2021/4/21.
 */
public class LogSupportSqlExecutor extends com.ibatis.sqlmap.engine.execution.SqlExecutor {

    private Logger log;

    private boolean showLog;

    public LogSupportSqlExecutor(String logName, boolean showLog) {
        log = LoggerFactory.getLogger(logName);
        this.showLog = showLog;
    }

    protected void log(String sql, Object[] parameters) {
        if (!showLog) {
            return;
        }
        log.warn("sql:" + sql + "\n, parameters:" + Arrays.toString(parameters));
    }

    @Override
    public int executeUpdate(StatementScope statementScope, Connection conn, String sql, Object[] parameters) throws SQLException {
        log(sql, parameters);
        return super.executeUpdate(statementScope, conn, sql, parameters);
    }

    @Override
    public void addBatch(StatementScope statementScope, Connection conn, String sql, Object[] parameters) throws SQLException {
        log(sql, parameters);
        super.addBatch(statementScope, conn, sql, parameters);
    }

    @Override
    public void executeQuery(StatementScope statementScope, Connection conn, String sql, Object[] parameters, int skipResults, int maxResults, RowHandlerCallback callback) throws SQLException {
        log(sql, parameters);
        super.executeQuery(statementScope, conn, sql, parameters, skipResults, maxResults, callback);
    }

    @Override
    public int executeUpdateProcedure(StatementScope statementScope, Connection conn, String sql, Object[] parameters) throws SQLException {
        log(sql, parameters);
        return super.executeUpdateProcedure(statementScope, conn, sql, parameters);
    }

    @Override
    public void executeQueryProcedure(StatementScope statementScope, Connection conn, String sql, Object[] parameters, int skipResults, int maxResults, RowHandlerCallback callback) throws SQLException {
        log(sql, parameters);
        super.executeQueryProcedure(statementScope, conn, sql, parameters, skipResults, maxResults, callback);
    }

}
