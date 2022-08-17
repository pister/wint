package wint.dal.ibatis.ext;

import com.ibatis.sqlmap.engine.builder.xml.XmlParserState;
import com.ibatis.sqlmap.engine.config.SqlMapConfiguration;

/**
 * Created by songlihuang on 2022/8/17.
 */
public class ExtXmlParserState extends XmlParserState {

    private SqlMapConfiguration config;

    public ExtXmlParserState(SqlMapConfiguration config) {
        this.config = config;
    }

    @Override
    public SqlMapConfiguration getConfig() {
        return config;
    }
}
