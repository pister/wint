package wint.help.tools.ibatis.gen;

import wint.lang.magic.Property;

public interface MappingPolicy {
	
	IbatisResult toResult(Property property);
	
	String toTableName(Class<?> clazz);

	public String getTablePrefix();

	public void setTablePrefix(String tablePrefix);
}
