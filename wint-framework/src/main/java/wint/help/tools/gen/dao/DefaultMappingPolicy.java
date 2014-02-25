package wint.help.tools.gen.dao;

import wint.lang.magic.Property;
import wint.lang.utils.StringUtil;

public class DefaultMappingPolicy implements MappingPolicy {

    private String tablePrefix = "";


    public DefaultMappingPolicy() {
        super();
    }

    public DefaultMappingPolicy(String tablePrefix) {
        super();
        this.tablePrefix = tablePrefix;
    }

    public IbatisResult toResult(Property property) {
        IbatisResult ret = new IbatisResult();
        ret.setProperty(property.getName());
        ret.setType(property.getPropertyClass().getTargetClass());
        ret.setColumn(StringUtil.camelToUnderLineString(property.getName()));
        return ret;
    }

    public String toTableName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        if (className.endsWith("DO")) {
            className = StringUtil.getLastBefore(className, "DO");
        }
        return tablePrefix + StringUtil.camelToUnderLineString(className);
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

}
