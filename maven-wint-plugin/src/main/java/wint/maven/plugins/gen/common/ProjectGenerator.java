package wint.maven.plugins.gen.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.maven.plugin.logging.Log;

import wint.maven.plugins.gen.common.util.ClassUtil;
import wint.maven.plugins.gen.common.util.FileUtil;
import wint.maven.plugins.gen.common.util.IoUtil;
import wint.maven.plugins.gen.common.util.MD5;


public class ProjectGenerator {
	
	private String charset = "utf-8";
	
	private SimpleVelocityEngine velocityEngine;
	
	private String basePath = "wint/maven/plugins/gen";
	
	public ProjectGenerator(Log log) {
		velocityEngine = new SimpleVelocityEngine(log);
	}
	
	public void genProject(ProjectConfig projectConfig) throws IOException {
		File basePath = new File(projectConfig.getPath());
		File projectPath = new File(basePath, projectConfig.getArtifactId());
		if (!projectPath.exists()) {
			projectPath.mkdirs();
		}
		genRoot(projectPath, projectConfig);
		genSourcePackages(projectPath, projectConfig);
		genScripts(projectPath, projectConfig);
	}
	
	private void genSourcePackages(File projectPath, ProjectConfig projectConfig) throws IOException {
		String mainJava = "src/main/java";
		String mainResources = "src/main/resources";
		String testJava = "src/test/java";
		String testResources = "src/test/resources";
		File mainJavaFile = new File(projectPath, mainJava);
		mainJavaFile.mkdirs();
		File mainResourcesFile = new File(projectPath, mainResources);
		mainResourcesFile.mkdirs();
		File testJavaFile = new File(projectPath, testJava);
		testJavaFile.mkdirs();
		File testResourcesFile = new File(projectPath, testResources);
		testResourcesFile.mkdirs();
		
		File javaMainFile = new File(projectPath, "src/main");
		genForJavaMain(javaMainFile, projectConfig);
		
		genResourceFiles(mainResourcesFile, projectConfig);
		
		genJavaPackage(mainJavaFile, projectConfig);
		
		genTestResourceFiles(testResourcesFile, projectConfig);
		
		genTestJavaPackage(testJavaFile, projectConfig);
	}
	
	
	private void genTestJavaPackage(File testJavaFile, ProjectConfig projectConfig) throws IOException {
		File javaPackage = new File(testJavaFile, projectConfig.getWintPackage().replace('.', '/'));
		javaPackage.mkdirs();
		renderFile(basePath +  "/test-src/BaseTest-java.vm", new File(javaPackage, "BaseTest.java"), projectConfig);
	}
	
	private void genTestResourceFiles(File testResourcesFile, ProjectConfig projectConfig) throws IOException {
		copyFile(basePath +  "/test-resources/testApplicationContext.vm", new File(testResourcesFile, "testApplicationContext.xml"));
		renderFile(basePath +  "/test-resources/logback.vm", new File(testResourcesFile, "logback.xml"), projectConfig);
		File beansDir = new File(testResourcesFile, "beans");
		beansDir.mkdirs();
		renderFile(basePath +  "/test-resources/beans/jdbc-datasource.vm", new File(beansDir, "jdbc-datasource.xml"), projectConfig);
	}
	
	private void genJavaPackage(File mainJavaFile, ProjectConfig projectConfig) throws IOException {
		File javaPackage = new File(mainJavaFile, projectConfig.getWintPackage().replace('.', '/'));
		javaPackage.mkdirs();
		
		File bizPackage = new File(javaPackage, "biz");
		bizPackage.mkdirs();
		
		File webPackage = new File(javaPackage, "web");
		webPackage.mkdirs();
		
		File actionPackage = new File(webPackage, "action");
		actionPackage.mkdirs();
		
		renderFile(basePath +  "/src/Index-java.vm", new File(actionPackage, "Index.java"), projectConfig);
		
	}
	
	private void genScripts(File projectPath, ProjectConfig projectConfig) throws IOException {
		if (projectConfig.isJrebelSupport()) {
			renderFile(basePath + "/set-debug-jrebel.vm", new File(projectPath, "set-debug.bat"), projectConfig);
		} else {
			copyFile(basePath +  "/set-debug.vm", new File(projectPath, "set-debug.bat"));
		}
	}
	
	private void genResourceFiles(File mainResourcesFile, ProjectConfig projectConfig) throws IOException {
		copyFile(basePath +  "/resources/applicationContext.vm", new File(mainResourcesFile, "applicationContext.xml"));
		File beansDir = new File(mainResourcesFile, "beans");
		beansDir.mkdirs();
		copyFile(basePath +  "/resources/beans/empty-beans.vm", new File(beansDir, "biz-ao.xml"));
		copyFile(basePath +  "/resources/beans/empty-beans.vm", new File(beansDir, "biz-bo.xml"));
		copyFile(basePath +  "/resources/beans/empty-beans.vm", new File(beansDir, "biz-dao.xml"));
		
		renderFile(basePath +  "/resources/beans/biz-datasource.vm", new File(beansDir, "biz-datasource.xml"), projectConfig);
		
		copyFile(basePath +  "/resources/beans/persistence.vm", new File(beansDir, "persistence.xml"));
		
		renderFile(basePath +  "/resources/sql-map.vm", new File(mainResourcesFile, "sql-map.xml"), projectConfig);
		
		File sqlmapDir = new File(mainResourcesFile, "sqlmaps");
		sqlmapDir.mkdirs();
		
		copyFile(basePath +  "/resources/sqlmaps/sqlmap-sample.vm", new File(sqlmapDir, "sqlmap-sample.xml"));
		
		
		renderFile(basePath +  "/resources/logback.vm", new File(mainResourcesFile, "logback.xml"), projectConfig);
		renderFile(basePath +  "/resources/url.vm", new File(mainResourcesFile, "url.xml"), projectConfig);
		copyFile(basePath +  "/resources/form.vm", new File(mainResourcesFile, "form.xml"));
		
		
		File webDir = new File(mainResourcesFile, "web");
		webDir.mkdirs();
		
		renderFile(basePath +  "/resources/web/web.vm", new File(webDir, "web.xml"), projectConfig);

		
		File wintSessionKeyFile = new File(mainResourcesFile, "wint-session-key");
		copyFile(basePath +  "/resources/wint-session-key", wintSessionKeyFile);
		
		FileUtil.writeContent(wintSessionKeyFile, genWintSessionKey());
	}
	
	private byte[] genWintSessionKey() {
		return MD5.encrypt(UUID.randomUUID().toString()).getBytes();
	}
	
	
	
	private void genForJavaMain(File javaMain, ProjectConfig projectConfig) throws IOException {
		genFilters(javaMain, projectConfig);
		genMetaFiles(javaMain, projectConfig);
		getWebapps(javaMain, projectConfig);
	}
	
	private void genFilters(File javaMain, ProjectConfig projectConfig) throws IOException {
		File filterDir = new File(javaMain, "filters");
		filterDir.mkdirs();
		
		File filterDev = new File(filterDir, projectConfig.getArtifactId() + "_dev.properties");
		File filterTest = new File(filterDir, projectConfig.getArtifactId() + "_test.properties");
		File filterProject = new File(filterDir, projectConfig.getArtifactId() + "_product.properties");
		
		renderFile(basePath +  "/filters/xxx_dev.properties.vm", filterDev, projectConfig);
		renderFile(basePath +  "/filters/xxx_test.properties.vm", filterTest, projectConfig);
		renderFile(basePath +  "/filters/xxx_product.properties.vm", filterProject, projectConfig);
	}
	
	private void genMetaFiles(File javaMain, ProjectConfig projectConfig) throws IOException {
		File metaFileDir = new File(javaMain, "meta-files");
		metaFileDir.mkdirs();
		
		renderFile(basePath +  "/meta-files/init-sql.txt", new File(metaFileDir, "init-sql.txt"), projectConfig);
	}
	
	private void getWebapps(File javaMain, ProjectConfig projectConfig) throws IOException {
		File webAppFile = new File(javaMain, "webapp");
		webAppFile.mkdirs();
		
		File templatesFile = new File(webAppFile, "templates");
		templatesFile.mkdirs();
		
		genTemplates(templatesFile, projectConfig);
		
		copyFile(basePath +  "/temp-index.vm", new File(webAppFile, "index.htm"));
		
	//	File devJettyFile = new File(webInfFile, "jetty-env.xml");
	//	renderFile(basePath +  "/jetty-env-dev.vm", devJettyFile, projectConfig);
		
	//	File devWebXml = new File(webInfFile, "web.xml"); 
	//	renderFile(basePath +  "/web-dev.vm", devWebXml, projectConfig);
	}
	
	private void genTemplates(File templatesFile, ProjectConfig projectConfig)  throws IOException {
		renderFile(basePath +  "/templates/macro.vm", new File(templatesFile, "macro.vm"), projectConfig);
		
		File pageDir = new File(templatesFile, "page");
		pageDir.mkdirs();
		
		renderFile(basePath +  "/templates/page/index.vm", new File(pageDir, "index.vm"), projectConfig);
		copyFile(basePath +  "/templates/page/error.vm", new File(pageDir, "error.vm"));
		
		File layoutDir = new File(templatesFile, "layout");
		layoutDir.mkdirs();
		
		renderFile(basePath +  "/templates/layout/default.vm", new File(layoutDir, "default.vm"), projectConfig);
		renderFile(basePath +  "/templates/layout/index.vm", new File(layoutDir, "index.vm"), projectConfig);
		
		File widgetDir = new File(templatesFile, "widget");
		widgetDir.mkdirs();
		
		renderFile(basePath +  "/templates/widget/sample.vm", new File(widgetDir, "sample.vm"), projectConfig);
	}
	
	
	
	private void renderFile(String templateName, File outOutFile, ProjectConfig projectConfig) throws IOException {
		InputStream is = getResouceAsStream(templateName);
		FileWriter writer = new FileWriter(outOutFile);
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("config", projectConfig);
		velocityEngine.merge(new InputStreamReader(is, charset), writer, context);
		IoUtil.close(is);
		IoUtil.close(writer);
	}
	
	private void copyFile(String templateName, File outOutFile) throws IOException {
		InputStream is = getResouceAsStream(templateName);
		OutputStream os = new FileOutputStream(outOutFile);
		IoUtil.copyAndClose(is, os);
		IoUtil.close(is);
		IoUtil.close(os);
	}
	
	private void genRoot(File projectPath, ProjectConfig projectConfig) throws IOException {
		renderFile(basePath +  "/pom.vm", new File(projectPath, "pom.xml"), projectConfig);
		renderFile(basePath +  "/readme.vm", new File(projectPath, "readme.txt"), projectConfig);
	}
	
	protected InputStream getResouceAsStream(String name) {
		return ClassUtil.getResourceAsStream(ProjectGenerator.class, name);
	}

}
