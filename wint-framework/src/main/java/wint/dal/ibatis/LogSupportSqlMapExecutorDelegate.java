package wint.dal.ibatis;

import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;

/**
 * Created by songlihuang on 2021/4/21.
 */
public class LogSupportSqlMapExecutorDelegate extends SqlMapExecutorDelegate {
    public LogSupportSqlMapExecutorDelegate(String logName, boolean showLog) {
        super();
        this.sqlExecutor = new LogSupportSqlExecutor(logName, showLog);
    }
}
