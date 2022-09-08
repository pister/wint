package wint.help.sql.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import com.ibatis.sqlmap.engine.builder.xml.SqlMapClasspathEntityResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.support.lob.LobHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wint.core.io.resource.autoload.LastModifiedFile;
import wint.core.service.parser.XMLParseUtil;
import wint.dal.ibatis.LogSupportSqlMapExecutorDelegate;
import wint.dal.ibatis.spring.SqlMapClientFactoryBean;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicObject;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.execution.BatchException;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;

/**
 * 1、支持自动加载sqlmap的配置文件
 * 2、支持show sql log
 *
 * @author pister 2012-3-25 下午08:10:43
 */
@SuppressWarnings("deprecation")
public class WintSqlMapClientFactoryBean extends SqlMapClientFactoryBean implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(WintSqlMapClientFactoryBean.class);

    private Resource[] orgConfigLocations;
    private Resource[] orgMappingLocations;
    private Properties orgProperties;

    private ApplicationContext applicationContext;

    private SqlMapClientProxy sqlMapClientProxy;

    private boolean showSql;

    private String logName;

    private long checkInterval = 1000L;

    private Object lock = new Object();

    public WintSqlMapClientFactoryBean(SqlMapClientFactoryBean sqlMapClientFactoryBean, boolean showSql, String logName) {
        super();
        MagicObject sqlMapClientFactoryBeanWrapper = MagicObject.wrap(sqlMapClientFactoryBean);
        setConfigLocations((Resource[]) sqlMapClientFactoryBeanWrapper.getField("configLocations"));
        setMappingLocations((Resource[]) sqlMapClientFactoryBeanWrapper.getField("mappingLocations"));
        setSqlMapClientProperties((Properties) sqlMapClientFactoryBeanWrapper.getField("sqlMapClientProperties"));
        setDataSource((DataSource) sqlMapClientFactoryBeanWrapper.getField("dataSource"));
        setUseTransactionAwareDataSource((Boolean) sqlMapClientFactoryBeanWrapper.getField("useTransactionAwareDataSource"));
        setTransactionConfigClass((Class<?>) sqlMapClientFactoryBeanWrapper.getField("transactionConfigClass"));
        setTransactionConfigProperties((Properties) sqlMapClientFactoryBeanWrapper.getField("transactionConfigProperties"));
        setLobHandler((LobHandler) sqlMapClientFactoryBeanWrapper.getField("lobHandler"));

        this.showSql = showSql;
        this.logName = logName;
    }

    public WintSqlMapClientFactoryBean() {
        super();
    }

    public SqlMapClientImpl superBuildSqlMapClient() throws IOException {
        SqlMapClient sqlMapClient = super.buildSqlMapClient(orgConfigLocations, orgMappingLocations, orgProperties);
        return (SqlMapClientImpl) sqlMapClient;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected SqlMapClient buildSqlMapClient(Resource[] configLocations, Resource[] mappingLocations /* ignore */, Properties properties) throws IOException {
        synchronized (lock) {
            if (sqlMapClientProxy != null) {
                return sqlMapClientProxy;
            }
            this.orgConfigLocations = configLocations;
            this.orgMappingLocations = mappingLocations;
            this.orgProperties = properties;

            try {
                sqlMapClientProxy = new SqlMapClientProxy(this, configLocations, applicationContext);
            } catch (Exception e) {
                log.warn("SqlMapClientProxy failed, use default sqlmapClient ", e);
                // 如果出错直接使用父类的实现
                return superBuildSqlMapClient();
            }
            Thread thread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(checkInterval);
                            if (sqlMapClientProxy.needReload()) {
                                sqlMapClientProxy.reload();
                            }
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                }
            };
            thread.start();

            return sqlMapClientProxy;
        }
    }

    @SuppressWarnings("rawtypes")
    static class SqlMapClientProxy extends SqlMapClientImpl {

        private XPathExpression sqlmapExpr;

        private WintSqlMapClientFactoryBean autoLoadConfigSqlMapClientFactoryBean;

        private SqlMapClient targetSqlMapClient;

        private ResourceLoader resourceLoader;

        private MagicList<LastModifiedFile> configLocations = MagicList.newList();

        private MagicList<LastModifiedFile> mappingLocations = MagicList.newList();

        public boolean needReload() throws IOException {
            if (configLocationsModified() || mappingLocationsModified()) {
                return true;
            }
            return false;
        }

        private boolean configLocationsModified() {
            for (LastModifiedFile lastModifiedFile : configLocations) {
                if (lastModifiedFile.hasModified()) {
                    return true;
                }
            }
            return false;
        }

        private boolean mappingLocationsModified() {
            for (LastModifiedFile lastModifiedFile : mappingLocations) {
                if (lastModifiedFile.hasModified()) {
                    return true;
                }
            }
            return false;
        }

        protected void log(String msg) {
            log.warn(Thread.currentThread() + ": " + msg);
        }

        public synchronized void reload() throws IOException {
            for (LastModifiedFile lastModifiedFile : mappingLocations) {
                if (lastModifiedFile.hasModified()) {
                    log("======> sqlmap has been modified:" + lastModifiedFile.getFile());
                }
            }
            uploadLocations();
            updateMappings();
            this.targetSqlMapClient = autoLoadConfigSqlMapClientFactoryBean.superBuildSqlMapClient();
        }

        public SqlMapClientProxy(WintSqlMapClientFactoryBean wintConfigSqlMapClientFactoryBean, Resource[] configLocations, ResourceLoader resourceLoader) throws IOException {
            super(new LogSupportSqlMapExecutorDelegate(wintConfigSqlMapClientFactoryBean.logName, wintConfigSqlMapClientFactoryBean.showSql));
            this.autoLoadConfigSqlMapClientFactoryBean = wintConfigSqlMapClientFactoryBean;
            this.resourceLoader = resourceLoader;
            try {
                XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();
                sqlmapExpr = xpath.compile("/sqlMapConfig/sqlMap");
            } catch (Exception e) {
                log.error("", e);
            }
            reloadLocations(configLocations);
            reload();
        }

        private void updateMappings() throws IOException {
            MagicList<LastModifiedFile> newMappingLocations = MagicList.newList();
            for (LastModifiedFile lastModifiedFile : configLocations) {
                FileInputStream fis = new FileInputStream(lastModifiedFile.getFile());
                newMappingLocations.addAll(getSqlmapsImpl(fis));
            }
            this.mappingLocations = newMappingLocations;
        }

        private MagicList<LastModifiedFile> getSqlmapsImpl(InputStream is) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setNamespaceAware(true);
                DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
                builder.setEntityResolver(new SqlMapClasspathEntityResolver());
                Document document = builder.parse(is);

                NodeList sqlmapNodes = (NodeList) sqlmapExpr.evaluate(document, XPathConstants.NODESET);
                MagicList<LastModifiedFile> newMappingLocations = MagicList.newList();
                if (sqlmapNodes == null) {
                    return newMappingLocations;
                }
                for (int i = 0, len = sqlmapNodes.getLength(); i < len; ++i) {
                    Node node = sqlmapNodes.item(i);
                    String location = XMLParseUtil.getAttribute(node, "resource");
                    Resource resource = resourceLoader.getResource(location);
                    File file = resource.getFile();
                    long lastModified = file.lastModified();
                    LastModifiedFile lmf = new LastModifiedFile(lastModified, file);
                    newMappingLocations.add(lmf);
                }
                return newMappingLocations;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void reloadLocations(Resource[] configLocations) throws IOException {
            MagicList<LastModifiedFile> newConfigLocations = MagicList.newList();
            for (Resource configLocationResource : configLocations) {
                File file = configLocationResource.getFile();
                long lastModified = file.lastModified();
                LastModifiedFile lmf = new LastModifiedFile(lastModified, file);
                newConfigLocations.add(lmf);
            }
            this.configLocations = newConfigLocations;
            updateMappings();
        }

        private void uploadLocations() throws IOException {
            for (LastModifiedFile configLocation : configLocations) {
                configLocation.setLastModified(configLocation.getFile().lastModified());
            }
        }

        public void commitTransaction() throws SQLException {
            targetSqlMapClient.commitTransaction();
        }

        public int delete(String id, Object parameterObject) throws SQLException {
            return targetSqlMapClient.delete(id, parameterObject);
        }

        public int delete(String id) throws SQLException {
            return targetSqlMapClient.delete(id);
        }

        public void endTransaction() throws SQLException {
            targetSqlMapClient.endTransaction();
        }

        public int executeBatch() throws SQLException {
            return targetSqlMapClient.executeBatch();
        }

        public List executeBatchDetailed() throws SQLException, BatchException {
            return targetSqlMapClient.executeBatchDetailed();
        }

        public void flushDataCache() {
            targetSqlMapClient.flushDataCache();
        }

        public void flushDataCache(String cacheId) {
            targetSqlMapClient.flushDataCache(cacheId);
        }

        public Connection getCurrentConnection() throws SQLException {
            return targetSqlMapClient.getCurrentConnection();
        }

        public DataSource getDataSource() {
            return targetSqlMapClient.getDataSource();
        }

        public SqlMapSession getSession() {
            return targetSqlMapClient.getSession();
        }

        public Connection getUserConnection() throws SQLException {
            return targetSqlMapClient.getUserConnection();
        }

        public Object insert(String id, Object parameterObject) throws SQLException {
            return targetSqlMapClient.insert(id, parameterObject);
        }

        public Object insert(String id) throws SQLException {
            return targetSqlMapClient.insert(id);
        }

        public SqlMapSession openSession() {
            return targetSqlMapClient.openSession();
        }

        public SqlMapSession openSession(Connection conn) {
            return targetSqlMapClient.openSession(conn);
        }

        public List queryForList(String id, int skip, int max) throws SQLException {
            return targetSqlMapClient.queryForList(id, skip, max);
        }

        public List queryForList(String id, Object parameterObject, int skip, int max) throws SQLException {
            return targetSqlMapClient.queryForList(id, parameterObject, skip, max);
        }

        public List queryForList(String id, Object parameterObject) throws SQLException {
            return targetSqlMapClient.queryForList(id, parameterObject);
        }

        public List queryForList(String id) throws SQLException {
            return targetSqlMapClient.queryForList(id);
        }

        public Map queryForMap(String id, Object parameterObject, String keyProp, String valueProp) throws SQLException {
            return targetSqlMapClient.queryForMap(id, parameterObject, keyProp, valueProp);
        }

        public Map queryForMap(String id, Object parameterObject, String keyProp) throws SQLException {
            return targetSqlMapClient.queryForMap(id, parameterObject, keyProp);
        }

        public Object queryForObject(String id, Object parameterObject, Object resultObject) throws SQLException {
            return targetSqlMapClient.queryForObject(id, parameterObject, resultObject);
        }

        public Object queryForObject(String id, Object parameterObject) throws SQLException {
            return targetSqlMapClient.queryForObject(id, parameterObject);
        }

        public Object queryForObject(String id) throws SQLException {
            return targetSqlMapClient.queryForObject(id);
        }

        public PaginatedList queryForPaginatedList(String id, int pageSize) throws SQLException {
            return targetSqlMapClient.queryForPaginatedList(id, pageSize);
        }

        public PaginatedList queryForPaginatedList(String id, Object parameterObject, int pageSize) throws SQLException {
            return targetSqlMapClient.queryForPaginatedList(id, parameterObject, pageSize);
        }

        public void queryWithRowHandler(String id, Object parameterObject, RowHandler rowHandler) throws SQLException {
            targetSqlMapClient.queryWithRowHandler(id, parameterObject, rowHandler);
        }

        public void queryWithRowHandler(String id, RowHandler rowHandler) throws SQLException {
            targetSqlMapClient.queryWithRowHandler(id, rowHandler);
        }

        public void setUserConnection(Connection connnection) throws SQLException {
            targetSqlMapClient.setUserConnection(connnection);
        }

        public void startBatch() throws SQLException {
            targetSqlMapClient.startBatch();
        }

        public void startTransaction() throws SQLException {
            targetSqlMapClient.startTransaction();
        }

        public void startTransaction(int transactionIsolation) throws SQLException {
            targetSqlMapClient.startTransaction(transactionIsolation);
        }

        public int update(String id, Object parameterObject) throws SQLException {
            return targetSqlMapClient.update(id, parameterObject);
        }

        public int update(String id) throws SQLException {
            return targetSqlMapClient.update(id);
        }
    }

}
