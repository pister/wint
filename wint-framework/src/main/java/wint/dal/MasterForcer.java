package wint.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import wint.dal.ibatis.ReadWriteSqlMapClientSource;
import wint.dal.ibatis.SqlMapClientTarget;

/**
 * User: pister
 * Date: 13-7-7
 * Time: 上午11:46
 */
public class MasterForcer {

    private static final Logger log = LoggerFactory.getLogger(MasterForcer.class);

    static final ThreadLocal<SqlMapClientTemplate> masterSqlMapClientTemplate = new ThreadLocal<SqlMapClientTemplate>();

    private static ReadWriteSqlMapClientSource readWriteSqlMapClientSource;

    public static void beginForce() {
        if (log.isDebugEnabled()) {
              log.debug("begin force master .");
        }
        SqlMapClientTemplate master = readWriteSqlMapClientSource.getSqlMapClient(SqlMapClientTarget.MASTER);
        masterSqlMapClientTemplate.set(master);
    }

    public static void endForce() {
        if (log.isDebugEnabled()) {
            log.debug("end force master .");
        }
        masterSqlMapClientTemplate.remove();
    }

    public static SqlMapClientTemplate getSqlMapClientTemplate() {
        return masterSqlMapClientTemplate.get();
    }

    public static void setReadWriteSqlMapClientSource(ReadWriteSqlMapClientSource readWriteSqlMapClientSource) {
        MasterForcer.readWriteSqlMapClientSource = readWriteSqlMapClientSource;
    }
}
