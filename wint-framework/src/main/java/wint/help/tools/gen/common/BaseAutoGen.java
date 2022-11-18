package wint.help.tools.gen.common;

import wint.lang.utils.FileUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: longyi
 * Date: 13-12-22
 * Time: 上午10:46
 */
public class BaseAutoGen {

    protected static final String JAVA_MAIN_SRC = "src/main/java";

    protected static final String JAVA_TEST_SRC = "src/test/java";

    private TemplateSourceGenerator templateSourceGenerator = new TemplateSourceGenerator();

    protected TemplateSourceGenerator getTemplateSourceGenerator() {
        return templateSourceGenerator;
    }


    protected void genJavaSrc(String name, String content, File javaMainSrcPath, FileWriter fileWriter) {
        String className = StringUtil.getLastAfter(name, ".");
        String packageName = StringUtil.getLastBefore(name, ".");
        String relativeDir = packageName.replace('.', File.separatorChar);
        File javaPackage = new File(javaMainSrcPath, relativeDir);
        if (!javaPackage.exists()) {
            javaPackage.mkdirs();
        }
        File javaFile = new File(javaPackage, className + ".java");
        fileWriter.writeToFile(content, javaFile);
    }


    protected File getProjectBasePath(Class<?> clazz) {
        URL url = clazz.getResource("/");
        String fileForTarget = url.getFile();
        String basePath = StringUtil.getLastBefore(fileForTarget, "/target/");
        File baseFile = new File(basePath);
        if (!baseFile.exists() || !baseFile.isDirectory()) {
            throw new RuntimeException("can not file dir: " + basePath);
        }
        return baseFile;
    }

    protected void log(String msg) {
        System.out.println("======= " + msg);
    }

    protected void addSpringBean(File beanConfigPath, String beanClassName, String beanId) throws IOException {
        String nodeStr = "<bean id=\"" + beanId + "\" class=\"" + beanClassName + "\" />";
        if (checkExist(beanConfigPath, nodeStr)) {
            return;
        }
        nodeStr = "\t" + nodeStr + SystemUtil.LINE_SEPARATOR;
        addNodeToPath(beanConfigPath, "(</beans>)", nodeStr);
    }

    protected boolean checkExist(File file, String input) throws IOException {
        String content = FileUtil.readAsString(file);
        if (content.contains(input)) {
            return true;
        }
        return false;
    }

    protected void addNodeToPath(File xml, String parentEndPattern, String nodeStr) {
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

    protected FileWriter getFileWriter(boolean force) {
        if (force) {
            return forceGenWriter();
        } else {
            return genWriter();
        }
    }

    protected FileWriter forceGenWriter() {
        return new FileWriter() {
            public void writeToFile(String content, File filename) {
                forceWriteToFile(content, filename);
            }
        };
    }

    protected FileWriter genWriter() {
        return new FileWriter() {
            public void writeToFile(String content, File filename) {
                if (filename.exists()) {
                    return;
                }
                forceWriteToFile(content, filename);
            }
        };
    }

    private void forceWriteToFile(String content, File filename) {
        try {
            FileUtil.writeContent(filename, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
