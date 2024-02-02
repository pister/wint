package wint.help.tools.gen.common;

import wint.help.biz.annations.Domain;
import wint.help.tools.gen.dao.*;
import wint.lang.WintException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.Property;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: longyi
 * Date: 13-12-22
 * Time: 上午10:52
 */
public class SourceGenerator {

    private TemplateSourceGenerator templateSourceGenerator = new TemplateSourceGenerator();

    private static final Map<Class<?>, String> typeDefaults = new HashMap<Class<?>, String>();
    private static final Map<Class<?>, String> type2sql = new HashMap<Class<?>, String>();

    static {
        typeDefaults.put(Boolean.TYPE, "false");
        typeDefaults.put(Boolean.class, "false");
        typeDefaults.put(Short.TYPE, "(short)1");
        typeDefaults.put(Short.class, "(short)1");
        typeDefaults.put(Integer.TYPE, "2");
        typeDefaults.put(Integer.class, "2");
        typeDefaults.put(Long.TYPE, "3L");
        typeDefaults.put(Long.class, "3L");
        typeDefaults.put(Float.TYPE, "1.1");
        typeDefaults.put(Float.class, "1.1");
        typeDefaults.put(Double.TYPE, "1.2");
        typeDefaults.put(Double.class, "1.2");
        typeDefaults.put(String.class, "\"a\"");
        typeDefaults.put(Date.class, "new java.util.Date()");
        typeDefaults.put(java.sql.Date.class, "new java.sql.Date(new java.util.Date().getTime())");
        typeDefaults.put(Timestamp.class, "new java.sql.Timestamp(new Date().getTime())");
        typeDefaults.put(LocalDateTime.class, "java.time.LocalDateTime.now()");
        typeDefaults.put(LocalDate.class, "java.time.LocalDate.now()");
        typeDefaults.put(LocalTime.class, "java.time.LocalTime.now()");


        type2sql.put(Integer.TYPE, "int");
        type2sql.put(Integer.class, "int");
        type2sql.put(Long.TYPE, "bigint");
        type2sql.put(Long.class, "bigint");
        type2sql.put(String.class, "varchar(?)");
    }

    private MappingPolicy mappingPolicy = new DefaultMappingPolicy();

    private ResultRender resultRender = new DefaultResultRender();


    private String idName = "id";

    private boolean logicDeleted = true;

    private String encoding = "utf-8";

    private String sqlMapTemplateName = "gen-sqlmap-template-mysql.vm";

    private String createTableTemplateName = "create-table-template-mysql.vm";

    private String genIbatisDaoTemplateName = "gen-ibatis-dao.vm";

    private String genDaoTemplateName = "gen-dao.vm";

    private String genTestsTemplateName = "gen-tests.vm";

    private String genFormTemplateName = "gen-form-template.vm";

    private String genFormTemplateNameCn = "gen-form-template-cn.vm";

    private String genJavaActionTemplateName = "gen-java-action.vm";

    private String genJavaAOTemplateName = "gen-java-ao.vm";

    private String genJavaAOImplTemplateName = "gen-java-ao-impl.vm";

    private boolean withName = false;

    public String setTablePrefix(String prefix) {
        String ret = mappingPolicy.getTablePrefix();
        mappingPolicy.setTablePrefix(prefix);
        return ret;
    }


    public GenMetaInfo genSqlMap(Class<?> clazz, Writer out) {
        GenMetaInfo genMetaInfo = new GenMetaInfo(clazz, idName);

        Map<String, Object> context = MapUtil.newHashMap();
        String alias = DaoGenUtil.getDoAlias(clazz);
        String mappingName = alias + "Mapping";

        String tableName = mappingPolicy.getTablePrefix() + StringUtil.camelToUnderLineString(alias);
        String idType = genMetaInfo.getIdType();

        context.put("encoding", encoding);
        context.put("namespace", genMetaInfo.getDaoSimpleClassName());
        context.put("domainName", genMetaInfo.getDoFullClassName());
        context.put("aliasDomainName", genMetaInfo.getDoAlias());
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
        context.put("gmtModifiedName", StringUtil.camelToUnderLineString(ColumnNameContants.gmtModifiedName));
        context.put("gmtCreateName", ColumnNameContants.gmtCreateName);

        genFromTemplate(context, out, sqlMapTemplateName);
        DaoGenUtil.writeLine(out);
        return genMetaInfo;
    }

    public GenMetaInfo genDAO(Class<?> clazz, Writer out) {
        GenMetaInfo genMetaInfo = new GenMetaInfo(clazz, idName);

        Map<String, Object> context = MapUtil.newHashMap();

        context.put("thisPackage", genMetaInfo.getDaoPackage());
        context.put("doFullClassName", genMetaInfo.getDoFullClassName());

        context.put("className", genMetaInfo.getDaoSimpleClassName());

        context.put("namespace", genMetaInfo.getDaoSimpleClassName());
        context.put("idType", genMetaInfo.getIdType());
        context.put("domainName", genMetaInfo.getDoSimpleClassName());
        context.put("paramName", genMetaInfo.getDoAlias());
        context.put("idTypeWrapper", genMetaInfo.getIdTypeWrapper());


        genFromTemplate(context, out, genDaoTemplateName);
        DaoGenUtil.writeLine(out);

        return genMetaInfo;
    }


    private String getBaseTestPackage(String doPackage) {
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

    public String getActionContext(String actionContext) {
        if (StringUtil.isEmpty(actionContext)) {
            return "";
        }
        return actionContext + "/";
    }

    private String getTestValueForType(Class<?> type) {
        String value = SourceGenerator.typeDefaults.get(type);
        return value;
    }

    private List<DomainField> getDomainFields(Class<?> domainClass) {
        Map<String, Property> propertyMap = PropertiesUtil.getProperties(domainClass);
        List<DomainField> fields = new ArrayList<DomainField>();
        for (Map.Entry<String, Property> entry : propertyMap.entrySet()) {
            Property property = entry.getValue();
            String name = entry.getKey();
            if (!property.isWritable() || !property.isReadable()) {
                continue;
            }
            if (name.equals(idName) || name.equals(ColumnNameContants.gmtModifiedName) || name.equals(ColumnNameContants.gmtCreateName)) {
                continue;
            }

            fields.add(new DomainField(name, property.getPropertyClass().getTargetClass()));
        }
        return fields;
    }

    public String genJavaAOImpl(Class<?> domainClass, Writer out) {
        Map<String, Object> context = MapUtil.newHashMap();
        String alias = DaoGenUtil.getDoAlias(domainClass);
        String domainUpper = StringUtil.uppercaseFirstLetter(alias);
        String aoClassName = domainUpper + "AO";

        GenMetaInfo genMetaInfo = new GenMetaInfo(domainClass, idName);
        String aoPackage = genMetaInfo.getBaseBizPackage() + ".ao";
        String domainName = ClassUtil.getShortClassName(domainClass);
        String domainParam = StringUtil.lowercaseFirstLetter(domainName);

        String aoFullName = aoPackage + "." + aoClassName;
        String thisPackage = aoPackage + ".impl";
        String thisClassName = aoClassName + "Impl";
        String aoImplFullClassName = thisPackage + "." + thisClassName;
        String idType = genMetaInfo.getIdType();

        String daoClassName = genMetaInfo.getDaoSimpleClassName();

        String daoBeanName = StringUtil.lowercaseFirstLetter(daoClassName);
        String daoParamName = daoBeanName;
        String domainInDb = alias + "Indb";
        String idGetter = "get" + StringUtil.uppercaseFirstLetter(idName);



        context.put("fields", getDomainFields(domainClass));
        context.put("daoFullClassName", genMetaInfo.getDaoFullClassName());
        context.put("domainInDb", domainInDb);
        context.put("idGetter", idGetter);
        context.put("aliasAllUpper", alias.toUpperCase());
        context.put("daoParamName", daoParamName);
        context.put("daoBeanName", daoBeanName);
        context.put("daoClassName", daoClassName);
        context.put("idType", idType);
        context.put("alias", alias);
        context.put("domainFullName", domainClass.getName());
        context.put("aoClassName", aoClassName);
        context.put("thisPackage", thisPackage);
        context.put("thisClassName", thisClassName);
        context.put("aoFullName", aoFullName);
        context.put("domainParam", domainParam);
        context.put("domainName", domainName);
        context.put("domainUpper", domainUpper);

        templateSourceGenerator.genSource(context, out, genJavaAOImplTemplateName);
        return aoImplFullClassName;
    }

    public String genJavaAO(Class<?> domainClass, Writer out) {
        Map<String, Object> context = MapUtil.newHashMap();
        String domainUpper = StringUtil.uppercaseFirstLetter(DaoGenUtil.getDoAlias(domainClass));
        String aoClassName = domainUpper + "AO";

        GenMetaInfo genMetaInfo = new GenMetaInfo(domainClass, idName);
        String aoPackage = genMetaInfo.getBaseBizPackage() + ".ao";

        String aoFullName = aoPackage + "." + aoClassName;
        String domainName = ClassUtil.getShortClassName(domainClass);
        String domainParam = StringUtil.lowercaseFirstLetter(domainName);

        String idType = genMetaInfo.getIdType();
        String idTypeUpper = StringUtil.uppercaseFirstLetter(idType);

        context.put("idTypeUpper", idTypeUpper);
        context.put("idType", idType);
        context.put("domainFullName", domainClass.getName());
        context.put("domainParam", domainParam);
        context.put("domainName", domainName);
        context.put("domainUpper", domainUpper);
        context.put("thisClassName", aoClassName);
        context.put("thisPackage", aoPackage);

        templateSourceGenerator.genSource(context, out, genJavaAOTemplateName);
        return aoFullName;
    }

    private void renderCreate(Class<?> domainClass, File moduleDir, String actionContext, FileWriter fileWriter) {
        Map<String, Object> context = MapUtil.newHashMap();

        String alias = DaoGenUtil.getDoAlias(domainClass);

        String formCreateDefine = "#set($form=$formFactory.getForm(\"" + alias + ".create\"))\n";
        context.put("formCreateDefine", formCreateDefine);
        context.put("alias", alias);
        context.put("doCreateAction", "$baseModule.setTarget('" + getActionContext(actionContext) + alias + "/doCreate" + "')");

        String listPageAction = "$baseModule.setTarget('" + getActionContext(actionContext) + alias + "/list')";
        context.put("listPageAction", listPageAction);

        context.put("fields", getDomainFields(domainClass));

        String targetName = getCreateName(alias) + ".vm";

        File createFile = new File(moduleDir, targetName);
        templateSourceGenerator.genSource(context, "gen-templates-create-vm.vm", createFile, fileWriter);

    }

    private String getCreateName(String alias) {
        if (withName) {
            return "create" + StringUtil.uppercaseFirstLetter(alias);
        } else {
            return "create";
        }
    }
    private String getEditName(String alias) {
        if (withName) {
            return "edit" + StringUtil.uppercaseFirstLetter(alias);
        } else {
            return "edit";
        }
    }

    private String getListName(String alias) {
        if (withName) {
            return "list" + StringUtil.uppercaseFirstLetter(alias);
        } else {
            return "list";
        }
    }

    private String getDetailName(String alias) {
        if (withName) {
            return alias + "Detail";
        } else {
            return "detail";
        }
    }


    private void renderList(Class<?> domainClass, File moduleDir, String actionContext, FileWriter fileWriter) {
        Map<String, Object> context = MapUtil.newHashMap();

        String alias = DaoGenUtil.getDoAlias(domainClass);

        String foreachStart = "#foreach($" + alias + " in $" + alias + "s)";

        String end = "#end";
        String createPageAction = "$baseModule.setTarget('" + getActionContext(actionContext) + alias + "/create')";
        String deleteDoAction = "$baseModule.setTarget('" + getActionContext(actionContext) + alias + "/doDelete').param('id', $" + alias + ".id).withToken()";
        String editPageAction = "$baseModule.setTarget('" + getActionContext(actionContext) + alias + "/edit').param('id', $" + alias + ".id)";
        String detailPageAction = "$baseModule.setTarget('" + getActionContext(actionContext) + alias + "/detail').param('id', $" + alias + ".id)";

        String paginationWidget = "$widget.setTemplate('common/pagination').addToContext('pageModule', $baseModule.setTarget('" + getActionContext(actionContext) + alias + "/list')).addToContext('query', $query)";


        context.put("paginationWidget", paginationWidget);
        context.put("fields", getDomainFields(domainClass));
        context.put("alias", alias);
        context.put("foreachStart", foreachStart);
        context.put("end", end);
        context.put("createPageAction", createPageAction);
        context.put("deleteDoAction", deleteDoAction);
        context.put("editPageAction", editPageAction);
        context.put("detailPageAction", detailPageAction);

        String targetName = getListName(alias) + ".vm";

        File createFile = new File(moduleDir, targetName);
        templateSourceGenerator.genSource(context, "gen-templates-list-vm.vm", createFile, fileWriter);
    }

    private void renderDetail(Class<?> domainClass, File moduleDir, String actionContext, FileWriter fileWriter) {
        Map<String, Object> context = MapUtil.newHashMap();

        String alias = DaoGenUtil.getDoAlias(domainClass);

        String listPageAction = "$baseModule.setTarget('" + getActionContext(actionContext) + alias + "/list')";
        String editPageAction = "$baseModule.setTarget('" + getActionContext(actionContext) + alias + "/edit').param('id', $" + alias + ".id)";

        MagicClass magicClass = MagicClass.wrap(domainClass);
        Set<String> propertyNames = new TreeSet<String>(magicClass.getReadableProperties().keySet());

        context.put("alias", alias);
        context.put("propertyNames", propertyNames);
        context.put("editPageAction", editPageAction);
        context.put("listPageAction", listPageAction);


        String targetName = getDetailName(alias) + ".vm";

        File createFile = new File(moduleDir, targetName);
        templateSourceGenerator.genSource(context, "gen-templates-detail-vm.vm", createFile, fileWriter);
    }

    private void renderEdit(Class<?> domainClass, File moduleDir, String actionContext, FileWriter fileWriter) {
        Map<String, Object> context = MapUtil.newHashMap();

        String alias = DaoGenUtil.getDoAlias(domainClass);
        String formEditDefine = "#set($form=$formFactory.getForm(\"" + alias + ".edit\"))\n";

        String listPageAction = "$baseModule.setTarget('" + actionContext + "/" + alias + "/list')";
        String doUpdateAction = "$baseModule.setTarget('" + actionContext + "/" + alias + "/doUpdate')";

        context.put("listPageAction", listPageAction);

        context.put("fields", getDomainFields(domainClass));

        context.put("alias", alias);
        context.put("idName", idName);
        context.put("formEditDefine", formEditDefine);
        context.put("doUpdateAction", doUpdateAction);
        context.put("listPageAction", listPageAction);


        String targetName = getEditName(alias) + ".vm";


        File createFile = new File(moduleDir, targetName);
        templateSourceGenerator.genSource(context, "gen-templates-edit-vm.vm", createFile, fileWriter);
    }


    public void genViewTemplates(Class<?> domainClass, File moduleDir, String actionContext, FileWriter fileWriter) {
        renderCreate(domainClass, moduleDir, actionContext, fileWriter);
        renderList(domainClass, moduleDir, actionContext, fileWriter);
        renderDetail(domainClass, moduleDir, actionContext, fileWriter);
        renderEdit(domainClass, moduleDir, actionContext, fileWriter);
    }

    public void genJavaAction(String className, Class<?> domainClass, Writer out, String actionContextPackage) {
        Map<String, Object> context = MapUtil.newHashMap();
        String thisPackage = StringUtil.getLastBefore(className, ".");
        String thisClassName = StringUtil.getLastAfter(className, ".");
        GenMetaInfo genMetaInfo = new GenMetaInfo(domainClass, idName);
        String alias = DaoGenUtil.getDoAlias(domainClass);
        String domainUpper = StringUtil.uppercaseFirstLetter(alias);
        String aoPackage = genMetaInfo.getBaseBizPackage() + ".ao";
        String aoClassName = thisClassName + "AO";
        String aoFullName = aoPackage + "." + aoClassName;
        String aoBeanName = StringUtil.lowercaseFirstLetter(aoClassName);
        String formName = genMetaInfo.getDoAlias();
        String domainFullName = domainClass.getName();
        String domainName = ClassUtil.getShortClassName(domainClass);
        String domainParam = StringUtil.lowercaseFirstLetter(domainName);


        String doPackage = domainClass.getPackage().getName();
        String baseBizPackage = StringUtil.getLastBefore(doPackage, ".biz.");
        String baseActionPackage = baseBizPackage + ".web.common.base";

        String contextPath = actionContextPackage.replace('.', '/');
        String baseActionTarget = getActionContext(contextPath) + alias;

        String idType = genMetaInfo.getIdType();
        String idTypeUpper = StringUtil.uppercaseFirstLetter(idType);
        String idGetter = "get" + StringUtil.uppercaseFirstLetter(idName);


        context.put("idGetter", idGetter);
        context.put("alias", alias);
        context.put("idTypeUpper", idTypeUpper);
        context.put("idType", idType);
        context.put("baseActionTarget", baseActionTarget);
        context.put("baseActionPackage", baseActionPackage);
        context.put("domainUpper", domainUpper);
        context.put("domainParam", domainParam);
        context.put("domainName", domainName);
        context.put("domainFullName", domainFullName);
        context.put("formName", formName);
        context.put("aoFullName", aoFullName);
        context.put("aoClassName", aoClassName);
        context.put("aoBeanName", aoBeanName);
        context.put("aoParamName", aoBeanName);
        context.put("thisPackage", thisPackage);
        context.put("thisClassName", thisClassName);

        templateSourceGenerator.genSource(context, out, genJavaActionTemplateName);
    }

    public void genForm(Class<?> clazz, File formFile, FileWriter fileWriter) {
        GenMetaInfo genMetaInfo = new GenMetaInfo(clazz, idName);
        Map<String, Object> context = MapUtil.newHashMap();

        context.put("name", genMetaInfo.getDoAlias());
        context.put("fields", getDomainFields(clazz));
        context.put("idName", idName);

        String cnName = cnName(clazz);
        if (StringUtil.isEmpty(cnName)) {
            templateSourceGenerator.genSource(context, genFormTemplateName, formFile, fileWriter);
        } else {
            templateSourceGenerator.genSource(context, genFormTemplateNameCn, formFile, fileWriter);
        }

    }

    private String cnName(Class<?> clazz) {
        Domain domain = clazz.getAnnotation(Domain.class);
        if (domain == null) {
            return null;
        }
        return domain.cnName();
    }


    public GenMetaInfo genDaoTests(Class<?> clazz, Writer out) {
        GenMetaInfo genMetaInfo = new GenMetaInfo(clazz, idName);
        Map<String, Object> context = MapUtil.newHashMap();

        String doPackage = clazz.getPackage().getName();
        String doClassName = clazz.getSimpleName();

        String daoClassName = genMetaInfo.getDaoSimpleClassName();

        String baseTestClassPackage = getBaseTestPackage(doPackage);
        String baseTestClassName = "BaseTest";
        String baseTestFullName = baseTestClassPackage + "." + baseTestClassName;
        String daoPropertyName = StringUtil.lowercaseFirstLetter(daoClassName);

        String baseDoName = StringUtil.lowercaseFirstLetter(doClassName);
        String doObject_1 = baseDoName;
        String doObject_2 = baseDoName + "_2";
        String doObject_3 = baseDoName + "_3";

        String propertySetter = propertySetter(clazz, doObject_1);

        context.put("propertySetter", propertySetter);
        context.put("doObject_1", doObject_1);
        context.put("doObject_2", doObject_2);
        context.put("doObject_3", doObject_3);
        context.put("daoPropertyname", daoPropertyName);
        context.put("baseTestClassName", baseTestClassName);
        context.put("baseTestFullName", baseTestFullName);
        context.put("thisPackage", genMetaInfo.getTestDaoPackage());
        context.put("thisClassName", genMetaInfo.getTestDaoSimpleClassName());
        context.put("doFullClassName", genMetaInfo.getDoFullClassName());

        context.put("daoClassName", genMetaInfo.getDaoSimpleClassName());

        context.put("idType", genMetaInfo.getIdType());
        context.put("idTypeWrapper", genMetaInfo.getIdTypeWrapper());
        context.put("doName", genMetaInfo.getDoSimpleClassName());

        genFromTemplate(context, out, genTestsTemplateName);
        DaoGenUtil.writeLine(out);

        return genMetaInfo;
    }


    public GenMetaInfo genIbatisDao(Class<?> clazz, Writer out, String implSuffix) {
        GenMetaInfo genMetaInfo = new GenMetaInfo(clazz, idName);
        if (implSuffix == null) {
            implSuffix = "Ibatis";
            genMetaInfo.setImplSuffix(implSuffix);
        }
        Map<String, Object> context = MapUtil.newHashMap();

        context.put("thisPackage", genMetaInfo.getIbatisPackage());
        context.put("doFullClassName", genMetaInfo.getDoFullClassName());
        context.put("daoFullClassName", genMetaInfo.getDaoFullClassName());
        context.put("daoClassName", genMetaInfo.getDaoSimpleClassName());

        context.put("className", genMetaInfo.getIbatisSimpleClassName());

        context.put("namespace", genMetaInfo.getDaoSimpleClassName());
        context.put("idType", genMetaInfo.getIdType());
        context.put("idTypeWrapper", genMetaInfo.getIdTypeWrapper());
        context.put("domainName", genMetaInfo.getDoSimpleClassName());
        context.put("paramName", genMetaInfo.getDoAlias());

        genFromTemplate(context, out, genIbatisDaoTemplateName);
        DaoGenUtil.writeLine(out);

        return genMetaInfo;
    }

    protected void genFromTemplate(Map<String, Object> context, Writer out, String templateName) {
        templateSourceGenerator.genSource(context, out, templateName);
    }

    protected void genFromTemplate(Map<String, Object> context, String templateName, File targetFile, FileWriter fileWriter) {
        templateSourceGenerator.genSource(context, templateName, targetFile, fileWriter);
    }

    public void genCreateTableSql(Class<?> clazz, Writer out, boolean defaultNotNull) {
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

        MagicClass mc = MagicClass.wrap(clazz);
        Property idProperty = mc.getProperty(idName);


        context.put("idType", type2sql.get(idProperty.getPropertyClass().getTargetClass()));
        context.put("mysqlEncoding", "utf8mb4");

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

            if (!SqlTypes.isDefaultNullable(result.getType())) {
                if (defaultNotNull) {
                    sb.append(" not null? ");
                }
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
            if (propertyName.equals(ColumnNameContants.gmtModifiedName)) {
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
            if (propertyName.equals(ColumnNameContants.gmtModifiedName) || propertyName.equals(ColumnNameContants.gmtCreateName)) {
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

    private void checkPropertyName(String name) {
        String normalizeName = StringUtil.camelToUnderLineString(name).toUpperCase();
        if (Keywords.MYSQL_KEYWORDS.contains(normalizeName)) {
            throw new WintException("property '" + name + "' is a keyword of mysql.");
        }
    }

    private List<IbatisResult> getResult(Class<?> c, Set<String> filters, OptionEnum resultOptionEnum) {
        Map<String, Property> properties = PropertiesUtil.getProperties(c);
        List<IbatisResult> ret = new ArrayList<IbatisResult>();
        for (Map.Entry<String, Property> entry : properties.entrySet()) {
            checkPropertyName(entry.getKey());
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

    public boolean isWithName() {
        return withName;
    }

    public void setWithName(boolean withName) {
        this.withName = withName;
    }
}
