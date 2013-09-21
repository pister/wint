package wint.help.tools.ibatis;

import wint.help.tools.ibatis.gen.*;
import wint.lang.magic.MagicClass;
import wint.lang.magic.Property;
import wint.lang.template.SimpleVelocityEngine;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

public class DaoGenerator {

    private static final Map<Class<?>, String> typeDefaults = new HashMap<Class<?>, String>();

    static {
        typeDefaults.put(Boolean.TYPE, "false");
        typeDefaults.put(Boolean.class,"false");
        typeDefaults.put(Short.TYPE,"(short)1");
        typeDefaults.put(Short.class, "(short)1");
        typeDefaults.put(Integer.TYPE, "2");
        typeDefaults.put(Integer.class, "2");
        typeDefaults.put(Long.TYPE, "3L");
        typeDefaults.put(Long.class, "3L");
        typeDefaults.put(Float.TYPE, "1.1L");
        typeDefaults.put(Float.class, "1.1L");
        typeDefaults.put(Double.TYPE, "1.2L");
        typeDefaults.put(Double.class, "1.2L");
        typeDefaults.put(String.class, "\"a\"");
        typeDefaults.put(Date.class, "new java.util.Date()");
        typeDefaults.put(java.sql.Date.class, "new java.sql.Date(new java.util.Date().getTime())");
        typeDefaults.put(Timestamp.class, "new java.sql.Timestamp(new Date().getTime())");
    }

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

	private String genTestsTemplateName = "gen-tests.vm";

	public String setTablePrefix(String prefix) {
		String ret = mappingPolicy.getTablePrefix();
		mappingPolicy.setTablePrefix(prefix);
		return ret;
	}
	

	
	public void genSqlMap(Class<?> clazz, Writer out) {
        DaoMetaInfo daoMetaInfo = new DaoMetaInfo(clazz, idName);

        Map<String, Object> context = MapUtil.newHashMap();
		String alias = DaoGenUtil.getDoAlias(clazz);
		String mappingName = alias + "Mapping";
		
		String tableName = mappingPolicy.getTablePrefix() + StringUtil.camelToUnderLineString(alias);
		String idType = daoMetaInfo.getIdType();
		
		context.put("encoding", encoding);
		context.put("namespace", daoMetaInfo.getDaoSimpleClassName());
		context.put("domainName", daoMetaInfo.getDoFullClassName());
		context.put("aliasDomainName", daoMetaInfo.getDoAlias());
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
        DaoGenUtil.writeLine(out);
	}

    public DaoMetaInfo genDAO(Class<?> clazz, Writer out) {
        DaoMetaInfo daoMetaInfo = new DaoMetaInfo(clazz, idName);

        Map<String, Object> context = MapUtil.newHashMap();

        context.put("thisPackage", daoMetaInfo.getDaoPackage());
        context.put("doFullClassName", daoMetaInfo.getDoFullClassName());

        context.put("className", daoMetaInfo.getDaoSimpleClassName());

        context.put("namespace", daoMetaInfo.getDaoSimpleClassName());
        context.put("idType", daoMetaInfo.getIdType());
        context.put("idTypeWrapper", daoMetaInfo.getIdTypeWrapper());
        context.put("domainName",  daoMetaInfo.getDoSimpleClassName());
        context.put("paramName",  daoMetaInfo.getDoAlias());

        genFromTemplate(context, out, genDaoTemplateName);
        DaoGenUtil.writeLine(out);

        return daoMetaInfo;
    }


    private String getBaseTestPackage( String doPackage) {
        return StringUtil.getLastBefore(doPackage, ".biz.");
    }

    private String propertySetter(Class<?> clazz, String doObjectName) {
        Set<String> filters = new HashSet<String>();
        filters.add("class");
        filters.add(idName);
        List<IbatisResult> columnsResult = getResult(clazz, filters, OptionEnum.READ_AND_WRITE);
        StringBuilder sb = new StringBuilder();
        for (IbatisResult ibatisResult : columnsResult) {
            String name = ibatisResult.getProperty();
            String value = getTestValueForType(ibatisResult.getType());
            if (value == null) {
                continue;
            }
            sb.append("\t\t");
            sb.append(doObjectName);
            String setterName = ".set" + StringUtil.uppercaseFirstLetter(name);
            sb.append(setterName);
            sb.append("(");
            sb.append(value);
            sb.append(");");
            sb.append(SystemUtil.LINE_SEPARATOR);
        }
        String ret = sb.toString();
        if (ret.endsWith(SystemUtil.LINE_SEPARATOR)) {
            ret = ret.substring(0, ret.length() - SystemUtil.LINE_SEPARATOR.length());
        }
        return ret;
    }

    private String getTestValueForType(Class<?> type) {
        String value = DaoGenerator.typeDefaults.get(type);
        return value;
    }

    public DaoMetaInfo genDaoTests(Class<?> clazz, Writer out) {
        DaoMetaInfo daoMetaInfo = new DaoMetaInfo(clazz, idName);
        Map<String, Object> context = MapUtil.newHashMap();

        String doPackage = clazz.getPackage().getName();
        String doClassName = clazz.getSimpleName();

        String daoClassName = daoMetaInfo.getDaoSimpleClassName();

        String baseTestClassPackage = getBaseTestPackage(doPackage);
        String baseTestClassName = "BaseTest";
        String baseTestFullName = baseTestClassPackage + "." + baseTestClassName;
        String daoPropertyname = StringUtil.lowercaseFirstLetter(daoClassName);

        String baseDoName =  StringUtil.lowercaseFirstLetter(doClassName);
        String doObject_1 = baseDoName;
        String doObject_2 = baseDoName + "_2";
        String doObject_3 = baseDoName + "_3";

        String propertySetter = propertySetter(clazz, doObject_1);

        context.put("propertySetter", propertySetter);
        context.put("doObject_1", doObject_1);
        context.put("doObject_2", doObject_2);
        context.put("doObject_3", doObject_3);
        context.put("daoPropertyname", daoPropertyname);
        context.put("baseTestClassName", baseTestClassName);
        context.put("baseTestFullName", baseTestFullName);
        context.put("thisPackage", daoMetaInfo.getTestDaoPackage());
        context.put("thisClassName", daoMetaInfo.getTestDaoSimpleClassName());
        context.put("doFullClassName", daoMetaInfo.getDoFullClassName());

        context.put("daoClassName", daoMetaInfo.getDaoSimpleClassName());

        context.put("idType", daoMetaInfo.getIdType());
        context.put("idTypeWrapper", daoMetaInfo.getIdTypeWrapper());
        context.put("doName",  daoMetaInfo.getDoSimpleClassName());

        genFromTemplate(context, out, genTestsTemplateName);
        DaoGenUtil.writeLine(out);

        return daoMetaInfo;
    }

	public DaoMetaInfo genIbatisDao(Class<?> clazz, Writer out, String implSuffix) {
        DaoMetaInfo daoMetaInfo = new DaoMetaInfo(clazz, idName);
        if (implSuffix == null) {
            implSuffix = "Ibatis";
            daoMetaInfo.setImplSuffix(implSuffix);
        }
        Map<String, Object> context = MapUtil.newHashMap();

        context.put("thisPackage", daoMetaInfo.getIbatisPackage());
        context.put("doFullClassName", daoMetaInfo.getDoFullClassName());
        context.put("daoFullClassName", daoMetaInfo.getDaoFullClassName());
        context.put("daoClassName", daoMetaInfo.getDaoSimpleClassName());

        context.put("className", daoMetaInfo.getIbatisSimpleClassName());

		context.put("namespace", daoMetaInfo.getDaoSimpleClassName());
		context.put("idType", daoMetaInfo.getIdType());
		context.put("idTypeWrapper", daoMetaInfo.getIdTypeWrapper());
		context.put("domainName",  daoMetaInfo.getDoSimpleClassName());
		context.put("paramName",  daoMetaInfo.getDoAlias());
		
		genFromTemplate(context, out, genIbatisDaoTemplateName);
        DaoGenUtil.writeLine(out);

        return daoMetaInfo;
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
		InputStream is = DaoGenerator.class.getResourceAsStream(templateName);
		if (is != null) {
			return new InputStreamReader(is);
		}
		is = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateName);
		if (is != null) {
			return new InputStreamReader(is);
		}
		is = DaoGenerator.class.getClassLoader().getResourceAsStream(templateName);
		if (is != null) {
			return new InputStreamReader(is);
		}
		return null;
	}
	
	public void genCreateTableSql(Class<?> clazz, Writer out, boolean notNull) {
		Map<String, Object> context = MapUtil.newHashMap();
		String alias = DaoGenUtil.getDoAlias(clazz);
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
			sb.append("\r\n\t\t\t\t");
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
		sb.append("\t\t\twhere ");
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
		sb.append(") \r\n\t\t\t\tvalues (");
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
