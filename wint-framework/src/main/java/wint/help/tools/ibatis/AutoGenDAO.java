package wint.help.tools.ibatis;

import wint.lang.utils.FileUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: pister
 * Date: 13-9-20
 * Time: 上午7:36
 * To change this template use File | Settings | File Templates.
 */
public class AutoGenDAO {

    private String sqlmapRootPath = "src/main/resources/sql-map.xml";

    private String sqlmapPath = "src/main/resources/sqlmaps";

    private String daoPath = "src/main/resources/beans/biz-dao.xml";

    private String javaMainSrc = "src/main/java";

    private String javaTestSrc = "src/test/java";

    private String daoSuffix = "Ibaits";

    private String prefix;

    private File getProjectBasePath(Class<?> clazz) {
        URL url = clazz.getResource("/");
        String fileForTarget = url.getFile();
        String basePath = StringUtil.getLastBefore(fileForTarget, "/target/");
        File baseFile = new File(basePath);
        if (!baseFile.exists() || !baseFile.isDirectory()) {
            throw new RuntimeException("can not file dir: " + basePath);
        }
        return baseFile;
    }

    private String getSqlmapName(Class<?> clazz) {
        String name = clazz.getSimpleName();
        if (name.endsWith("DO") || name.endsWith("Do")) {
            return name.substring(0, name.length() - 2);
        }
        return name;
    }

    private void log(String msg) {
        System.out.println("======= " + msg);
    }

    private void addNodeToPath(File xml, String parentEndPattern, String nodeStr) {
        try {
            String content = FileUtil.readAsString(xml);
            Pattern pattern = Pattern.compile(parentEndPattern, Pattern.CASE_INSENSITIVE);
            Matcher m = pattern.matcher(content);
            String newContent = m.replaceAll(nodeStr + "$1");
            FileUtil.writeContent(xml, newContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addSpringBean(File beanConfigPath, String beanClassName, String beanId) throws IOException {
        String nodeStr = "\t<bean id=\"" + beanId + "\" class=\"" + beanClassName + "\" />" + SystemUtil.LINE_SEPARATOR;
        if (checkExist(beanConfigPath, nodeStr)) {
            return;
        }
        addNodeToPath(beanConfigPath, "(</beans>)", nodeStr);
    }

    private String getBeanId(String beanClassName) {
        String className = StringUtil.getLastAfter(beanClassName, ".");
        if (className.endsWith(daoSuffix)) {
            return StringUtil.lowercaseFirstLetter(className.substring(0, className.length() - daoSuffix.length()));
        }
        return StringUtil.lowercaseFirstLetter(className);
    }

    private String getBeanClassName(Class<?> clazz) {
        String alias = IbatisGenerator.getAlias(clazz);
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


    private void genTests() throws IOException {
        // TODO gen tests java files.
    }

    private void printSqlScripts(IbatisGenerator ibatisGenerator,  Class<?> clazz) {
        System.out.println("==============create table sql script================");
        System.out.println();
        System.out.println();
        System.out.println();
        ibatisGenerator.genCreateTableSql(clazz,  new OutputStreamWriter(System.out), true);
        System.out.flush();
        System.out.flush();
        System.out.println("=====================================================");
    }

    private boolean checkExist(File file, String input) throws IOException {
        String content = FileUtil.readAsString(file);
        if (content.contains(input)) {
            return true;
        }
        return false;
    }

    private void addSqlmapToRootFile(File baseFile, String sqlmapFileName) throws IOException {
        File rootFile = new File(baseFile, sqlmapRootPath);
        if (!rootFile.exists()) {
            return;
        }
        String nodeStr = "\t<sqlMap resource=\"sqlmaps/" + sqlmapFileName + "\" />" + SystemUtil.LINE_SEPARATOR;
        if (checkExist(rootFile, nodeStr)) {
            return;
        }
        addNodeToPath(rootFile, "(</sqlMapConfig>)", nodeStr);
    }


    private void genSqlmap(IbatisGenerator ibatisGenerator, Class<?> clazz, File baseFile, FileWriter fileWriter) throws IOException {
        // for sqlmap
        log("generating sqlmap");
        File sqlmapDir = new File(baseFile, sqlmapPath);
        String sqlmapFileName = getSqlmapName(clazz) + "-sqlmap.xml";
        StringWriter sqlmapStringWriter = new StringWriter();
        ibatisGenerator.genSqlMap(clazz, sqlmapStringWriter);
        if (!sqlmapDir.exists()) {
            sqlmapDir.mkdirs();
        }
        File sqlmapFile = new File(sqlmapDir, sqlmapFileName);
        fileWriter.writeToFile(sqlmapStringWriter.toString(), sqlmapFile);
        log("generate sqlmap finish");

        addSqlmapToRootFile(baseFile, sqlmapFileName);
    }

    private void genJavaSrc(String name, String content, File javaMainSrcPath, FileWriter fileWriter) {
        String className = StringUtil.getLastAfter(name, ".");
        String packageName = StringUtil.getLastBefore(name, ".");
        String relativeDir = packageName.replace('.', File.separatorChar);
        File ibtaisDAO = new File(javaMainSrcPath, relativeDir);
        if (!ibtaisDAO.exists()) {
            ibtaisDAO.mkdirs();
        }
        File ibatisDAOJavaFile = new File(ibtaisDAO, className + ".java");
        fileWriter.writeToFile(content, ibatisDAOJavaFile);
    }

    private void genDAO(IbatisGenerator ibatisGenerator, Class<?> clazz, File baseFile, FileWriter fileWriter, File javaMainSrcPath) {
        log("generating dao interface");
        StringWriter stringWriter = new StringWriter();
        String name = ibatisGenerator.genDAO(clazz, stringWriter);
        String content = stringWriter.toString();
        genJavaSrc(name, content, javaMainSrcPath, fileWriter);
        log("generate dao interface finish");
    }

    private void genIbatisDAO(IbatisGenerator ibatisGenerator, Class<?> clazz, File baseFile, FileWriter fileWriter, File javaMainSrcPath) throws IOException {
        log("generating ibatis dao");
        StringWriter stringWriter = new StringWriter();
        String name = ibatisGenerator.genIbatisDao(clazz, stringWriter, daoSuffix);
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
        IbatisGenerator ibatisGenerator = new IbatisGenerator();
        ibatisGenerator.setIdName(idName);
        ibatisGenerator.setTablePrefix(prefix);

        File javaMainSrcPath = new File(baseFile, javaMainSrc);

        try {
            genSqlmap(ibatisGenerator, clazz, baseFile, fileWriter);
            genDAO(ibatisGenerator, clazz, baseFile, fileWriter, javaMainSrcPath);
            genIbatisDAO(ibatisGenerator, clazz, baseFile, fileWriter, javaMainSrcPath);
            printSqlScripts(ibatisGenerator, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void gen(Class<?> clazz, String idName) {
        genImpl(clazz, idName, new FileWriter() {
            public void writeToFile(String content, File filename) {
                if (filename.exists()) {
                    return;
                }
                forceWriteToFile(content, filename);
            }
        });
    }

    public void forceGen(Class<?> clazz, String idName) {
        genImpl(clazz, idName, new FileWriter() {
            public void writeToFile(String content, File filename) {
                forceWriteToFile(content, filename);
            }
        });
    }

    private void forceWriteToFile(String content, File filename) {
        try {
            FileUtil.writeContent(filename, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    static interface FileWriter {
        void writeToFile(String content, File filename);
    }


}
