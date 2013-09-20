package wint.help.tools.ibatis;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import wint.help.tools.ibatis.gen.DefaultMappingPolicy;
import wint.help.tools.ibatis.gen.DefaultResultRender;
import wint.help.tools.ibatis.gen.IbatisResult;
import wint.help.tools.ibatis.gen.MappingPolicy;
import wint.help.tools.ibatis.gen.OptionEnum;
import wint.help.tools.ibatis.gen.ResultRender;
import wint.help.tools.ibatis.gen.SqlTypes;
import wint.lang.magic.MagicClass;
import wint.lang.magic.Property;
import wint.lang.template.SimpleVelocityEngine;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

public class IbatisGenerator {
	
	private MappingPolicy mappingPolicy = new DefaultMappingPolicy();
	
	private ResultRender resultRender = new DefaultResultRender();
	
	private String gmtModifiedName = "gmtModified";
	
	private String gmtCreateName = "gmtCreate";
	
	private String idName = "id";
	
	private boolean logicDeleted = true;
	
	private String encoding = "utf-8";
	
	private String sqlMapTemplateName = "gen-sqlmap-template-mysql.vm";
	
	private String createTableTemplateName = "create-table-template-mysql.vm";
	
	private String genIbatisDaoTemplateName = "gen-ibatis-dao.vm";

	private String genDaoTemplateName = "gen-dao.vm";

	public String setTablePrefix(String prefix) {
		String ret = mappingPolicy.getTablePrefix();
		mappingPolicy.setTablePrefix(prefix);
		return ret;
	}
	
	public static String getAlias(Class<?> clazz) {
		String name = ClassUtil.getShortClassName(clazz);
		name = StringUtil.lowercaseFirstLetter(name);
		if (name.endsWith("DO") || name.endsWith("Do")) {
			return name.substring(0, name.length() - 2);
		} else {
			return name;
		}
	}
	
	public void genSqlMap(Class<?> clazz, Writer out) {
		Map<String, Object> context = MapUtil.newHashMap();
		String alias = getAlias(clazz);
		String namespace = StringUtil.uppercaseFirstLetter(alias) + "DAO";
		String mappingName = alias + "Mapping";
		
		String tableName = mappingPolicy.getTablePrefix() + StringUtil.camelToUnderLineString(alias);
		MagicClass magicClass = MagicClass.wrap(clazz);
		Property idProperty = magicClass.getProperty(idName);
		String idType = ClassUtil.getShortClassName(idProperty.getPropertyClass().getTargetClass());
		
		context.put("encoding", encoding);
		context.put("namespace", namespace);
		context.put("domainName", clazz.getName());
		context.put("aliasDomainName", alias);
		context.put("mappingName", mappingName);
		Set<String> mappingFilters = new HashSet<String>();
		mappingFilters.add("class");
		List<IbatisResult> mappingColumns = getResult(clazz, mappingFilters, OptionEnum.READ_AND_WRITE);
		context.put("mappingColumns", mappingColumns);
		String fullSQLColumns = genFullSQLColumns(clazz);
		context.put("fullSQLColumns", fullSQLColumns);
		
		String genInsertNoId = genInsertNoId(clazz);
		String genUpdateNoId = genUpdateNoId(clazz);
		context.put("insertSqlNoId", genInsertNoId);
		context.put("genUpdateNoId", genUpdateNoId);
		context.put("idType", idType);
		context.put("tableName", tableName);
		context.put("logicDeleted", logicDeleted);
		context.put("idNameColumn", StringUtil.camelToUnderLineString(idName));
		context.put("idNameProperty", StringUtil.fixedCharToCamel(idName, "_-"));
		context.put("gmtModifiedName", StringUtil.camelToUnderLineString(gmtModifiedName));
		context.put("gmtCreateName", gmtCreateName);
		
		genFromTemplate(context, out, sqlMapTemplateName);
        try {
            out.write(SystemUtil.LINE_SEPARATOR);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

    public String genDAO(Class<?> clazz, Writer out) {
        Map<String, Object> context = MapUtil.newHashMap();
        String alias = getAlias(clazz);
        String namespace = StringUtil.uppercaseFirstLetter(alias) + "DAO";

        MagicClass magicClass = MagicClass.wrap(clazz);
        Property idProperty = magicClass.getProperty(idName);
        if (idProperty == null) {
            throw new RuntimeException("the id " + idName + " from " + clazz + " not exist!");
        }
        String idType = ClassUtil.getShortClassName(idProperty.getPropertyClass().getTargetClass());

        String doPackage = clazz.getPackage().getName();
        String doFullClassName = clazz.getName();
        String baseDalPackage = StringUtil.getLastBefore(doPackage, ".domain");

        String thisPackage = baseDalPackage + ".dao";
        String className = namespace;

        context.put("thisPackage", thisPackage);
        context.put("doFullClassName", doFullClassName);

        context.put("className", className);

        context.put("namespace", namespace);
        context.put("idType", idType);
        context.put("idTypeWrapper", ClassUtil.getShortClassName(ClassUtil.getWrapperClass(idProperty.getPropertyClass().getTargetClass())));
        context.put("domainName",  ClassUtil.getShortClassName(clazz.getName()));
        context.put("paramName",  alias);

        genFromTemplate(context, out, genDaoTemplateName);
        try {
            out.write(SystemUtil.LINE_SEPARATOR);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //genIbatisDaoTemplateName

        return thisPackage + "." +  className;
    }
	
	public String genIbatisDao(Class<?> clazz, Writer out, String implSuffix) {
        if (implSuffix == null) {
            implSuffix = "Ibatis";
        }
		Map<String, Object> context = MapUtil.newHashMap();
		String alias = getAlias(clazz);
		String namespace = StringUtil.uppercaseFirstLetter(alias) + "DAO";
		
		MagicClass magicClass = MagicClass.wrap(clazz);
		Property idProperty = magicClass.getProperty(idName);
        if (idProperty == null) {
            throw new RuntimeException("the id " + idName + " from " + clazz + " not exist!");
        }
		String idType = ClassUtil.getShortClassName(idProperty.getPropertyClass().getTargetClass());

        // import com.zuirenmai.danta.biz.dal.domain.CareerDO;

        String doPackage = clazz.getPackage().getName();
        String doFullClassName = clazz.getName();
        String baseDalPackage = StringUtil.getLastBefore(doPackage, ".domain");

        String daoPackage = baseDalPackage + ".dao";
        String daoClassName = namespace;
        String daoFullClassName = daoPackage + "." + daoClassName;

        String thisPackage = daoPackage + ".ibatis";

        String className = daoClassName + implSuffix;

        context.put("thisPackage", thisPackage);
        context.put("doFullClassName", doFullClassName);
        context.put("daoFullClassName", daoFullClassName);
        context.put("daoClassName", daoClassName);

        context.put("className", className);

		context.put("namespace", namespace);
		context.put("idType", idType);
		context.put("idTypeWrapper", ClassUtil.getShortClassName(ClassUtil.getWrapperClass(idProperty.getPropertyClass().getTargetClass())));
		context.put("domainName",  ClassUtil.getShortClassName(clazz.getName()));
		context.put("paramName",  alias);
		
		genFromTemplate(context, out, genIbatisDaoTemplateName);
        try {
            out.write(SystemUtil.LINE_SEPARATOR);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //genIbatisDaoTemplateName

        return thisPackage + "." +  className;
	}
	
	protected void genFromTemplate(Map<String, Object> context, Writer out, String templateName) {
		SimpleVelocityEngine simpleTemplateEngine = new SimpleVelocityEngine();
		simpleTemplateEngine.init(encoding);
		Reader reader = getTemplateReader(templateName);

		simpleTemplateEngine.merge(reader, out, context);
		try {
			reader.close();
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Reader getTemplateReader(String templateName) {
		InputStream is = IbatisGenerator.class.getResourceAsStream(templateName);
		if (is != null) {
			return new InputStreamReader(is);
		}
		is = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateName);
		if (is != null) {
			return new InputStreamReader(is);
		}
		is = IbatisGenerator.class.getClassLoader().getResourceAsStream(templateName);
		if (is != null) {
			return new InputStreamReader(is);
		}
		return null;
	}
	
	public void genCreateTableSql(Class<?> clazz, Writer out, boolean notNull) {
		Map<String, Object> context = MapUtil.newHashMap();
		String alias = getAlias(clazz);
		String tableName = mappingPolicy.getTablePrefix() + StringUtil.camelToUnderLineString(alias);
		
		Set<String> filters = new HashSet<String>();
		filters.add("class");
		filters.add(idName);
		List<IbatisResult> columnsResult = getResult(clazz, filters, OptionEnum.READ_AND_WRITE);
		
		context.put("tableName", tableName);
		context.put("idNameColumn", StringUtil.camelToUnderLineString(idName));
		context.put("encoding", encoding);
		context.put("mysqlEncoding", StringUtil.replace(encoding, "-", ""));
		
		IbatisResult deletedIbatisResult = new IbatisResult();
		deletedIbatisResult.setColumn("deleted");
		deletedIbatisResult.setProperty("deleted");
		deletedIbatisResult.setType(Byte.TYPE);
		deletedIbatisResult.setExtend("default 0");
		columnsResult.add(deletedIbatisResult);
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (IbatisResult result : columnsResult) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
				sb.append("\r\n");
			}
			sb.append("`");
			sb.append(result.getColumn());
			sb.append("`");
			sb.append(" ");
			sb.append(getSqlType(result.getType()));
			sb.append(" ");
			if (notNull) {
				sb.append(" not null? ");
			}
			String extend = result.getExtend();
			if (!StringUtil.isEmpty(extend)) {
				sb.append(" ");
				sb.append(extend);
				sb.append(" ");
			}
		}
		context.put("columns", sb.toString());
		
		genFromTemplate(context, out, createTableTemplateName);
        try {
            out.write(SystemUtil.LINE_SEPARATOR);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	protected String getSqlType(Class<?> type) {
		return SqlTypes.getByClass(type);
	}
	

	public String genMapping(Class<?> clazz) {
		Set<String> filters = new HashSet<String>();
		filters.add("class");
		return genMapping(clazz, filters, OptionEnum.READ_AND_WRITE);
	}
	
	public String genFullSQLColumns(Class<?> clazz) {
		Set<String> filters = new HashSet<String>();
		filters.add("class");
		return genFullSQLColumns(clazz, "t.", filters, OptionEnum.READ_AND_WRITE);
	}
	
	public String genInsertNoId(Class<?> clazz) {
		Set<String> filters = new HashSet<String>();
		filters.add("class");
		filters.add(idName);
		return genInsert(clazz, filters, OptionEnum.READ_AND_WRITE);
	}
	
	public String genUpdateNoId(Class<?> clazz) {
		Set<String> filters = new HashSet<String>();
		filters.add("class");
		filters.add(idName);
		return genUpdate(clazz, filters, OptionEnum.READ_AND_WRITE);
	}
	
	public String genUpdate(Class<?> clazz, Set<String> filters, OptionEnum resultOptionEnum) {
		List<IbatisResult> results = getResult(clazz, filters, resultOptionEnum);
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(mappingPolicy.toTableName(clazz));
		sb.append(" set ");
		boolean first = true;
		for (IbatisResult result : results) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append("\r\n");
			sb.append(result.getColumn());
			sb.append("= ");
			String propertyName = result.getProperty();
			if (propertyName.equals(gmtModifiedName)) {
				sb.append("now()");
			} else {
				sb.append("#");
				sb.append(propertyName);
				sb.append("#");
			}
		}
		sb.append("\r\n");
		sb.append("where ");
		sb.append(StringUtil.camelToUnderLineString(idName));
		sb.append("=");
		sb.append("#");
		sb.append(StringUtil.fixedCharToCamel(idName, "_-"));
		sb.append("#");
		return sb.toString();
	}
	
	public String genInsert(Class<?> clazz, Set<String> filters, OptionEnum resultOptionEnum) {
		List<IbatisResult> results = getResult(clazz, filters, resultOptionEnum);
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		sb.append(mappingPolicy.toTableName(clazz));
		sb.append("(");
		boolean first = true;
		for (IbatisResult result : results) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(result.getColumn());
		}
		sb.append(") \r\nvalues (");
		first = true;
		for (IbatisResult result : results) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			String propertyName = result.getProperty();
			if (propertyName.equals(gmtModifiedName) || propertyName.equals(gmtCreateName)) {
				sb.append("now()");
			} else {
				sb.append("#");
				sb.append(propertyName);
				sb.append("#");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	public String genFullSQLColumns(Class<?> clazz, String prefix, Set<String> filters, OptionEnum resultOptionEnum) {
		List<IbatisResult> results = getResult(clazz, filters, resultOptionEnum);
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (IbatisResult result : results) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(prefix);
			sb.append(result.getColumn());
		}
		return sb.toString();
	}
	
	private List<IbatisResult> getResult(Class<?> c, Set<String> filters, OptionEnum resultOptionEnum) {
		MagicClass clazz = MagicClass.wrap(c);
		Map<String, Property> properties = clazz.getProperties();
		List<IbatisResult> ret = new ArrayList<IbatisResult>();
		for (Map.Entry<String, Property> entry : properties.entrySet()) {
			if (filters != null && filters.contains(entry.getKey())) {
				continue;
			}
			Property property = entry.getValue();
			if (needRead(resultOptionEnum) && !property.isReadable()) {
				continue;
			}
			if (needWrite(resultOptionEnum) && !property.isWritable()) {
				continue;
			}
			IbatisResult result = mappingPolicy.toResult(property);
			ret.add(result);
		}
		return ret;
	}
	
	public String genMapping(Class<?> clazz, Set<String> filters, OptionEnum resultOptionEnum) {
		List<IbatisResult> results = getResult(clazz, filters, resultOptionEnum);
		StringBuilder sb = new StringBuilder();
		for (IbatisResult result : results) {
			sb.append(resultRender.render(result));
			sb.append("\r\n");
		}
		return sb.toString();
	}
	
	private static boolean needRead(OptionEnum resultOptionEnum) {
		return (resultOptionEnum.getValue() & OptionEnum.READ.getValue()) == OptionEnum.READ.getValue();
	}
	
	private static boolean needWrite(OptionEnum resultOptionEnum) {
		return (resultOptionEnum.getValue() & OptionEnum.WRITE.getValue()) == OptionEnum.WRITE.getValue();
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

}
