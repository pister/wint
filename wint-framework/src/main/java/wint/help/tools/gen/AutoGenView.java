package wint.help.tools.gen;

import wint.help.tools.gen.common.BaseAutoGen;
import wint.help.tools.gen.common.FileWriter;
import wint.help.tools.gen.common.SourceGenerator;
import wint.help.tools.gen.dao.DaoGenUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

/**
 * User: longyi
 * Date: 13-12-22
 * Time: 上午10:42
 */
public class AutoGenView extends BaseAutoGen {

    private String prefix;

    private List<String> targetActions = Arrays.asList("list", "create", "delete");

    private String formPath = "src/main/resources/forms";

    private String aoPath = "src/main/resources/beans/biz-ao.xml";

    private String viewTemplatePath = "src/main/webapp/templates";

    private String formRootPath = "src/main/resources/form.xml";

    public AutoGenView(String prefix) {
        this.prefix = prefix;
    }

    private void genImpl(Class<?> clazz, String idName, String actionContext, boolean force) {
        try {
            File baseFile = getProjectBasePath(clazz);

            SourceGenerator sourceGenerator = new SourceGenerator();
            sourceGenerator.setIdName(idName);
            sourceGenerator.setTablePrefix(prefix);

            File javaMainSrcPath = new File(baseFile, JAVA_MAIN_SRC);

            genForm(sourceGenerator, clazz, baseFile, getFileWriter(force));
            genJavaAO(sourceGenerator, clazz, getFileWriter(force), javaMainSrcPath);
            genJavaAOImpl(sourceGenerator, clazz, baseFile, getFileWriter(force), javaMainSrcPath);
            genJavaAction(sourceGenerator, clazz, actionContext, getFileWriter(force), javaMainSrcPath);
            genViewTemplates(sourceGenerator, clazz, actionContext, getFileWriter(force));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void gen(Class<?> clazz, String idName, String actionContext) {
        genImpl(clazz, idName, actionContext, false);
    }

    public void gen(Class<?> clazz, String actionContext) {
        gen(clazz, "id", actionContext);
    }

    public void forceGen(Class<?> clazz, String idName, String actionContext) {
        genImpl(clazz, idName, actionContext, true);
    }

    public void forceGen(Class<?> clazz, String actionContext) {
        forceGen(clazz, "id", actionContext);
    }

    private void genJavaAO(SourceGenerator sourceGenerator, Class<?> clazz, FileWriter fileWriter, File javaMainSrcPath) {
        StringWriter stringWriter = new StringWriter();
        String aoFullname = sourceGenerator.genJavaAO(clazz, stringWriter);
        String content = stringWriter.toString();
        this.genJavaSrc(aoFullname, content, javaMainSrcPath, fileWriter);
    }

    private void genJavaAOImpl(SourceGenerator sourceGenerator, Class<?> clazz, File baseFile, FileWriter fileWriter, File javaMainSrcPath) throws IOException {
        StringWriter stringWriter = new StringWriter();
        String aoImplFullname = sourceGenerator.genJavaAOImpl(clazz, stringWriter);
        String content = stringWriter.toString();
        this.genJavaSrc(aoImplFullname, content, javaMainSrcPath, fileWriter);

        File daoSpring = new File(baseFile, aoPath);
        if (daoSpring.exists()) {
            String alias = DaoGenUtil.getDoAlias(clazz);
            addSpringBean(daoSpring, aoImplFullname, alias + "AO");
        }

    }

    private void genViewTemplates(SourceGenerator sourceGenerator, Class<?> clazz, String actionContext, FileWriter fileWriter) {
        File viewTemplateFile = new File(viewTemplatePath);
        if (!viewTemplateFile.exists()) {
            viewTemplateFile.mkdirs();
        }
        File pageDir = new File(viewTemplatePath, "page");
        if (!pageDir.exists()) {
            pageDir.mkdirs();
        }
        File layoutDir = new File(viewTemplatePath, "layout");
        if (!layoutDir.exists()) {
            layoutDir.mkdirs();
        }
        File widgetDir = new File(viewTemplatePath, "widget");
        if (!widgetDir.exists()) {
            widgetDir.mkdirs();
        }

        File actionDir = new File(pageDir, actionContext);
        if (!actionDir.exists()) {
             actionDir.mkdirs();
        }
        String alias = DaoGenUtil.getDoAlias(clazz);
        File moduleDir = new File(actionDir, alias);
        if (!moduleDir.exists()) {
            moduleDir.mkdirs();
        }

        actionContext = normalizeContext(actionContext);
        sourceGenerator.genViewTemplates(clazz, moduleDir, actionContext, fileWriter);
    }

    private String normalizeContext(String actionContext) {
        actionContext = actionContext.replace('\\', '/');
        while (actionContext.startsWith("/")) {
            actionContext = actionContext.substring(1);
        }
        while (actionContext.endsWith("/")) {
            actionContext = actionContext.substring(0, actionContext.length() - 1);
        }
        return actionContext;
    }

    private void genJavaAction(SourceGenerator sourceGenerator, Class<?> clazz, String actionContext, FileWriter fileWriter, File javaMainSrcPath) {
        String doPackage = clazz.getPackage().getName();
        String baseBizPackage = StringUtil.getLastBefore(doPackage, ".biz.");
        String baseActionPackage = baseBizPackage + ".web.action";
        actionContext = normalizeContext(actionContext);
        String actionContextPackage = actionContext.replace('/', '.');
        String targetActionPackage = baseActionPackage + "." + actionContextPackage;
        String actionClassName = StringUtil.uppercaseFirstLetter(DaoGenUtil.getDoAlias(clazz));
        String fullActionName = targetActionPackage + "." + actionClassName;
        StringWriter stringWriter = new StringWriter();
        sourceGenerator.genJavaAction(fullActionName, clazz, stringWriter, actionContextPackage);
        String content = stringWriter.toString();
        this.genJavaSrc(fullActionName, content, javaMainSrcPath, fileWriter);
    }

    private void genForm(SourceGenerator sourceGenerator, Class<?> clazz, File baseFile, FileWriter fileWriter) throws IOException {
        File formDir = new File(baseFile, formPath);
        String alias = DaoGenUtil.getDoAlias(clazz);
        String formFileName = alias + "_autogen.xml";

        if (!formDir.exists()) {
            formDir.mkdirs();
        }
        File formFile = new File(formDir, formFileName);
        sourceGenerator.genForm(clazz, formFile, fileWriter);
        addFormToRoot(baseFile, formFileName);
    }

    private void addFormToRoot(File baseFile, String formName) throws IOException {
        File rootFile = new File(baseFile, formRootPath);
        if (!rootFile.exists()) {
            return;
        }

        String nodeStr = "<resource file=\"forms/" + formName + "\" />";
        if (checkExist(rootFile, nodeStr)) {
            return;
        }
        nodeStr = "\t" + nodeStr + SystemUtil.LINE_SEPARATOR;
        addNodeToPath(rootFile, "(</forms>)", nodeStr);
    }



}
