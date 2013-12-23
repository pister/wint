package wint.help.tools.ibatis;

import wint.help.tools.gen.common.BaseAutoGen;
import wint.help.tools.gen.common.FileWriter;
import wint.help.tools.gen.dao.DaoGenUtil;
import wint.help.tools.gen.common.GenMetaInfo;
import wint.help.tools.gen.common.SourceGenerator;
import wint.lang.utils.FileUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: pister
 * Date: 13-9-20
 * Time: 上午7:36
 * To change this template use File | Settings | File Templates.
 */
public class AutoGenDAO extends BaseAutoGen {

    private String sqlmapRootPath = "src/main/resources/sql-map.xml";

    private String sqlmapPath = "src/main/resources/sqlmaps";

    private String daoPath = "src/main/resources/beans/biz-dao.xml";

    private String daoSuffix = "Ibatis";

    private String prefix;

    public AutoGenDAO(String prefix) {
        this.prefix = prefix;
    }
    private String getBeanId(String beanClassName) {
        String className = StringUtil.getLastAfter(beanClassName, ".");
        if (className.endsWith(daoSuffix)) {
            return StringUtil.lowercaseFirstLetter(className.substring(0, className.length() - daoSuffix.length()));
        }
        return StringUtil.lowercaseFirstLetter(className);
    }

    private String getBeanClassName(Class<?> clazz) {
        String alias = DaoGenUtil.getDoAlias(clazz);
        String namespace = StringUtil.uppercaseFirstLetter(alias) + "DAO";
        String doPackage = clazz.getPackage().getName();
        String baseDalPackage = StringUtil.getLastBefore(doPackage, ".domain");

        String daoPackage = baseDalPackage + ".dao";
        String daoClassName = namespace;
        String ibatisDaoPackage = daoPackage + ".ibatis";
        String ibatisDaoClassName = daoClassName + daoSuffix;
        String ibatisDaoFullClassName = ibatisDaoPackage + "." + ibatisDaoClassName;
        return ibatisDaoFullClassName;
    }


    private void genTests(SourceGenerator sourceGenerator, Class<?> clazz, File baseFile, FileWriter fileWriter) throws IOException {
        log("generating dao tests");
        StringWriter stringWriter = new StringWriter();
        GenMetaInfo genMetaInfo = sourceGenerator.genDaoTests(clazz, stringWriter);
        String name = genMetaInfo.getTestDaoFullClassName();
        String content = stringWriter.toString();
        File javaTestSrcPath = new File(baseFile, JAVA_TEST_SRC);
        genJavaSrc(name, content, javaTestSrcPath, fileWriter);
        log("generate dao tests finish");
    }

    private void printSqlScripts(SourceGenerator sourceGenerator, Class<?> clazz) {
        System.out.println("==============create table sql script================");
        System.out.println();
        System.out.println();
        sourceGenerator.genCreateTableSql(clazz, new OutputStreamWriter(System.out), true);
        System.out.println();
        System.out.println();
        System.out.flush();
        System.out.println("=====================================================");
    }

    private void addSqlmapToRootFile(File baseFile, String sqlmapFileName) throws IOException {
        File rootFile = new File(baseFile, sqlmapRootPath);
        if (!rootFile.exists()) {
            return;
        }
        String nodeStr = "<sqlMap resource=\"sqlmaps/" + sqlmapFileName + "\" />";
        if (checkExist(rootFile, nodeStr)) {
            return;
        }
        nodeStr = "\t" + nodeStr + SystemUtil.LINE_SEPARATOR;
        addNodeToPath(rootFile, "(</sqlMapConfig>)", nodeStr);
    }


    private void genSqlmap(SourceGenerator sourceGenerator, Class<?> clazz, File baseFile, FileWriter fileWriter) throws IOException {
        // for sqlmap
        log("generating sqlmap");
        File sqlmapDir = new File(baseFile, sqlmapPath);
        StringWriter sqlmapStringWriter = new StringWriter();
        GenMetaInfo genMetaInfo = sourceGenerator.genSqlMap(clazz, sqlmapStringWriter);
        String sqlmapFileName = genMetaInfo.getDoAlias() + "-sqlmap.xml";
        if (!sqlmapDir.exists()) {
            sqlmapDir.mkdirs();
        }
        File sqlmapFile = new File(sqlmapDir, sqlmapFileName);
        fileWriter.writeToFile(sqlmapStringWriter.toString(), sqlmapFile);
        log("generate sqlmap finish");

        addSqlmapToRootFile(baseFile, sqlmapFileName);
    }

    private void genDAO(SourceGenerator sourceGenerator, Class<?> clazz, File baseFile, FileWriter fileWriter, File javaMainSrcPath) {
        log("generating dao interface");
        StringWriter stringWriter = new StringWriter();
        GenMetaInfo genMetaInfo = sourceGenerator.genDAO(clazz, stringWriter);
        String name = genMetaInfo.getDaoFullClassName();
        String content = stringWriter.toString();
        genJavaSrc(name, content, javaMainSrcPath, fileWriter);
        log("generate dao interface finish");
    }

    private void genIbatisDAO(SourceGenerator sourceGenerator, Class<?> clazz, File baseFile, FileWriter fileWriter, File javaMainSrcPath) throws IOException {
        log("generating ibatis dao");
        StringWriter stringWriter = new StringWriter();
        GenMetaInfo genMetaInfo = sourceGenerator.genIbatisDao(clazz, stringWriter, daoSuffix);
        String name = genMetaInfo.getIbatisFullClassName();
        String content = stringWriter.toString();

        genJavaSrc(name, content, javaMainSrcPath, fileWriter);

        log("generate ibatis dao finish");

        log("adding dao to dao.xml");
        File daoSpring = new File(baseFile, daoPath);
        if (daoSpring.exists()) {
            String beanClassName = getBeanClassName(clazz);
            String beanId = getBeanId(beanClassName);
            addSpringBean(daoSpring, beanClassName, beanId);
            log("add dao to dao.xml finish");
        } else {
            log("dao.xml not exist.");
        }

    }

    private void genImpl(Class<?> clazz, String idName, FileWriter fileWriter) {
        File baseFile = getProjectBasePath(clazz);
        SourceGenerator sourceGenerator = new SourceGenerator();
        sourceGenerator.setIdName(idName);
        sourceGenerator.setTablePrefix(prefix);

        File javaMainSrcPath = new File(baseFile, JAVA_MAIN_SRC);

        try {
            genSqlmap(sourceGenerator, clazz, baseFile, fileWriter);
            genDAO(sourceGenerator, clazz, baseFile, fileWriter, javaMainSrcPath);
            genIbatisDAO(sourceGenerator, clazz, baseFile, fileWriter, javaMainSrcPath);
            genTests(sourceGenerator, clazz, baseFile, fileWriter);
            printSqlScripts(sourceGenerator, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void gen(Class<?> clazz) {
        gen(clazz, "id");
    }

    public void forceGen(Class<?> clazz) {
        forceGen(clazz, "id");
    }

    public void gen(Class<?> clazz, String idName) {
        genImpl(clazz, idName, genWriter());
    }

    public void forceGen(Class<?> clazz, String idName) {
        genImpl(clazz, idName, forceGenWriter());
    }

    public void setDaoPath(String daoPath) {
        this.daoPath = daoPath;
    }

    public void setDaoSuffix(String daoSuffix) {
        this.daoSuffix = daoSuffix;
    }

    public void setSqlmapPath(String sqlmapPath) {
        this.sqlmapPath = sqlmapPath;
    }

    public void setSqlmapRootPath(String sqlmapRootPath) {
        this.sqlmapRootPath = sqlmapRootPath;
    }
}
