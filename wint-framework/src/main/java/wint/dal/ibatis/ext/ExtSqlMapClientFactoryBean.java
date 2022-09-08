package wint.dal.ibatis.ext;

import com.ibatis.common.xml.NodeletException;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.builder.xml.SqlMapConfigParser;
import com.ibatis.sqlmap.engine.builder.xml.SqlMapParser;
import com.ibatis.sqlmap.engine.builder.xml.XmlParserState;
import com.ibatis.sqlmap.engine.config.SqlMapConfiguration;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;
import wint.dal.ibatis.ext.handlers.LocalDateTimeTypeHandler;
import wint.dal.ibatis.ext.handlers.LocalDateTypeHandler;
import wint.dal.ibatis.ext.handlers.LocalTimeTypeHandler;
import wint.dal.ibatis.spring.SqlMapClientFactoryBean;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Properties;

/**
 * Created by songlihuang on 2022/8/17.
 */
public class ExtSqlMapClientFactoryBean extends SqlMapClientFactoryBean {

    private void hackerRegisterTypes(SqlMapConfigParser configParser) {
        XmlParserState xmlParserState = null;
        try {
            Field stateField = SqlMapConfigParser.class.getDeclaredField("state");
            stateField.setAccessible(true);
            xmlParserState = (XmlParserState) stateField.get(configParser);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        SqlMapConfiguration sqlMapConfiguration = xmlParserState.getConfig();
        TypeHandlerFactory typeHandlerFactory = sqlMapConfiguration.getTypeHandlerFactory();
        registerExtTypes(typeHandlerFactory);
    }

    private void registerExtTypes(TypeHandlerFactory typeHandlerFactory) {
        typeHandlerFactory.register(LocalDateTime.class, new LocalDateTimeTypeHandler());
        typeHandlerFactory.register(LocalDate.class, new LocalDateTypeHandler());
        typeHandlerFactory.register(LocalTime.class, new LocalTimeTypeHandler());

    }

    @Override
    protected SqlMapClient buildSqlMapClient(
            Resource[] configLocations, Resource[] mappingLocations, Properties properties)
            throws IOException {

        if (ObjectUtils.isEmpty(configLocations)) {
            throw new IllegalArgumentException("At least 1 'configLocation' entry is required");
        }

        SqlMapClient client = null;
        SqlMapConfigParser configParser = new SqlMapConfigParser();
        hackerRegisterTypes(configParser);
        for (Resource configLocation : configLocations) {
            InputStream is = configLocation.getInputStream();
            try {
                client = configParser.parse(is, properties);
            } catch (RuntimeException ex) {
                throw new NestedIOException("Failed to parse config resource: " + configLocation, ex.getCause());
            }
        }

        if (mappingLocations != null) {
            SqlMapParser mapParser = SqlMapParserFactory.createSqlMapParser(configParser);
            for (Resource mappingLocation : mappingLocations) {
                try {
                    mapParser.parse(mappingLocation.getInputStream());
                } catch (NodeletException ex) {
                    throw new NestedIOException("Failed to parse mapping resource: " + mappingLocation, ex);
                }
            }
        }

        return client;
    }

    /**
     * Inner class to avoid hard-coded iBATIS 2.3.2 dependency (XmlParserState class).
     */
    private static class SqlMapParserFactory {

        public static SqlMapParser createSqlMapParser(SqlMapConfigParser configParser) {
            // Ideally: XmlParserState state = configParser.getState();
            // Should raise an enhancement request with iBATIS...
            XmlParserState state = null;
            try {
                Field stateField = SqlMapConfigParser.class.getDeclaredField("state");
                stateField.setAccessible(true);
                state = (XmlParserState) stateField.get(configParser);
            } catch (Exception ex) {
                throw new IllegalStateException("iBATIS 2.3.2 'state' field not found in SqlMapConfigParser class - " +
                        "please upgrade to IBATIS 2.3.2 or higher in order to use the new 'mappingLocations' feature. " + ex);
            }
            return new SqlMapParser(state);
        }
    }
}
