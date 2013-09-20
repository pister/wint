package wint.help.tools.ibatis;

import java.io.*;

import wint.lang.utils.StringUtil;

import java.net.URL;

import wint.lang.utils.FileUtil;

/**
 * Created with IntelliJ IDEA.
 * User: pister
 * Date: 13-9-20
 * Time: 上午7:36
 * To change this template use File | Settings | File Templates.
 */
public class AutoGenDAO {

    private String sqlmapPath = "src/main/resources/sqlmaps";

    private String daoPath = "src/main/resources/beans/biz-dao.xml";

    private String daoSrc = "src/main/java";

    private String testSrc = "src/test/java";

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

    public void gen(Class<?> clazz, String idName) {
        File baseFile = getProjectBasePath(clazz);

        IbatisGenerator ibatisGenerator = new IbatisGenerator();
        ibatisGenerator.setIdName(idName);
        ibatisGenerator.setTablePrefix(prefix);

        {
            // for sqlmap
            File sqlmapDir = new File(baseFile, sqlmapPath);
            String sqlmapFileName = getSqlmapName(clazz) + "-sqlmap.xml";
            StringWriter sqlmapStringWriter = new StringWriter();
            ibatisGenerator.genSqlMap(clazz, sqlmapStringWriter);
            if (!sqlmapDir.exists()) {
                sqlmapDir.mkdirs();
            }
            File sqlmapFile = new File(sqlmapDir, sqlmapFileName);
            writeToFile(sqlmapStringWriter.toString(), sqlmapFile);
        }
    }

    public void forceGen(Class<?> clazz, String idName) {

    }

    private void forceWriteToFile(String content, File filename) {
        try {
            FileUtil.writeContent(filename, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean writeToFile(String content, File filename) {
        if (filename.exists()) {
            return false;
        }
        forceWriteToFile(content, filename);
        return true;
    }


}
