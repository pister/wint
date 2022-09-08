package wint.dal.ibatis;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import wint.dal.ibatis.spring.SqlMapClientTemplate;
import wint.dal.ibatis.spring.support.SqlMapClientDaoSupport;
import wint.lang.utils.CollectionUtil;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * User: pister
 * Date: 13-7-7
 * Time: 上午9:52
 */
public class ReadWriteSqlMapClientSource implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(ReadWriteSqlMapClientSource.class);

    private DataSource masterDataSource;

    private List<DataSource> slaveDataSources = CollectionUtil.newArrayList(2);

    private SqlMapClientDaoSupport masterSqlMapClientDaoSupport;

    private ArrayList<SqlMapClientDaoSupport> slaveSqlMapClientDaoSupports = CollectionUtil.newArrayList(2);

    private LoadBalancePolicy slaveLoadBalancePolicy;

    private SqlMapClient sqlMapClient;

    private boolean hasSlave = false;

    /**
     * 在有slave的情况下，master是否作为读服务器
     */
    private boolean masterForRead = true;

    public void afterPropertiesSet() throws Exception {
        log.warn("master dataSource: " + masterDataSource);
        log.warn("slaves dataSource count: " + slaveDataSources.size());

        if (!CollectionUtil.isEmpty(slaveDataSources))  {
            log.warn("slave dataSources are: ");
            for (DataSource slaveDs : slaveDataSources) {
                log.warn("slave dataSource: " + slaveDs);
            }
        }

        slaveLoadBalancePolicy = new RandomLoadBalancePolicy(slaveDataSources.size());

        {
            log.warn("initializing master...");
            SqlMapClientDaoSupport sqlMapClientDaoSupport = new SqlMapClientDaoSupport() {
            };
            sqlMapClientDaoSupport.setDataSource(masterDataSource);
            sqlMapClientDaoSupport.setSqlMapClient(sqlMapClient);
            sqlMapClientDaoSupport.afterPropertiesSet();
            masterSqlMapClientDaoSupport = sqlMapClientDaoSupport;
            log.warn("initialize master success.");
        }

        if (slaveDataSources.size() > 0) {
            log.warn("initializing slaves...");
            slaveSqlMapClientDaoSupports = CollectionUtil.newArrayList(slaveDataSources.size());
            for (DataSource ds : slaveDataSources) {
                SqlMapClientDaoSupport sqlMapClientDaoSupport = new SqlMapClientDaoSupport() {
                };
                sqlMapClientDaoSupport.setDataSource(ds);
                sqlMapClientDaoSupport.setSqlMapClient(sqlMapClient);
                sqlMapClientDaoSupport.afterPropertiesSet();
                slaveSqlMapClientDaoSupports.add(sqlMapClientDaoSupport);
            }

            if (masterForRead) {
                log.warn("master is as read service.");
                slaveSqlMapClientDaoSupports.add(masterSqlMapClientDaoSupport);
            } else {
                log.warn("master is not as read service.");

            }
            log.warn("initialize slaves success.");
            hasSlave = true;
        } else {
            log.warn("there was not any slaves.");
            hasSlave = false;
        }

    }

    public SqlMapClientTemplate getSqlMapClient(SqlMapClientTarget target) {
        if (!hasSlave) {
            return masterSqlMapClientDaoSupport.getSqlMapClientTemplate();
        }
        switch (target) {
            case MASTER:
                if (log.isDebugEnabled()) {
                    log.debug("use master");
                }
                return masterSqlMapClientDaoSupport.getSqlMapClientTemplate();
            case SLAVE:
                if (log.isDebugEnabled()) {
                    log.debug("use slave");
                }
                int slaveIndex = slaveLoadBalancePolicy.getNext();
                if (log.isDebugEnabled()) {
                    log.debug("slave index: " + slaveIndex);
                }
                return slaveSqlMapClientDaoSupports.get(slaveIndex).getSqlMapClientTemplate();
            default:
                return null;
        }
    }

    public SqlMapClientTemplate getSqlMapClientImpl(SqlMapClientTarget target) {
        if (!hasSlave) {
            return masterSqlMapClientDaoSupport.getSqlMapClientTemplate();
        }
        switch (target) {
            case MASTER:
                if (log.isDebugEnabled()) {
                    log.debug("use master");
                }
                return masterSqlMapClientDaoSupport.getSqlMapClientTemplate();
            case SLAVE:
                if (log.isDebugEnabled()) {
                    log.debug("use slave");
                }
                int slaveIndex = slaveLoadBalancePolicy.getNext();
                if (log.isDebugEnabled()) {
                    log.debug("slave index: " + slaveIndex);
                }
                return slaveSqlMapClientDaoSupports.get(slaveIndex).getSqlMapClientTemplate();
            default:
                return null;
        }
    }

    public void setMasterDataSource(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public void setSlaveDataSources(List<DataSource> slaveDataSources) {
        this.slaveDataSources = slaveDataSources;
    }

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    public void setMasterForRead(boolean masterForRead) {
        this.masterForRead = masterForRead;
    }
}
