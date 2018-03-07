package wint.maven.plugins.gen.common;

public class ProjectConfig {

    private String projectPath;
	
	private String groupId;
	
	private String artifactId;
	
	private String path;
	
	private String wintPackage;
	
	private String dbName;
	
	private String dbUser;
	
	private String dbPwd;
	
	private String dbUrl = "127.0.0.1";
	
	private String charset = "utf-8";
	
	private boolean sampleSupport = true;
	
	private boolean fileUploadSupport = true;
	
	private boolean jrebelSupport = false;
	
	private String jrebelPath;
	
	private String javaVersion ="1.6";
	
	private String wintVersion = "1.5.1";
	
	private String servletVersion = "2.4";
	
	private String junitVersion = "3.8.1";
	
	private String springVersion = "2.5.6";
	
	private String ibatisVersion = "2.3.4.726";
	
	private String c3p0Version = "0.9.1.2";
	
	private String mysqlJdbcVersion = "5.1.9";
	
	private String fileuploadVersion = "1.3.1";
	
	private String commonsIOVersion = "2.1";
	
	private String slf4jApiVersion = "1.6.4";
	
	private String logbackVersion = "1.0.1";

    private String driudVersion = "1.0.26";

    private String fastjsonVersion ="1.2.20";

    private String wintPluginVersion = "1.1.3";

    public String getFastjsonVersion() {
        return fastjsonVersion;
    }

    public void setFastjsonVersion(String fastjsonVersion) {
        this.fastjsonVersion = fastjsonVersion;
    }

    public String getDriudVersion() {
        return driudVersion;
    }

    public void setDriudVersion(String driudVersion) {
        this.driudVersion = driudVersion;
    }

    public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getWintPackage() {
		return wintPackage;
	}

	public void setWintPackage(String wintPackage) {
		this.wintPackage = wintPackage;
	}

	public boolean isSampleSupport() {
		return sampleSupport;
	}

	public void setSampleSupport(boolean sampleSupport) {
		this.sampleSupport = sampleSupport;
	}

	public boolean isFileUploadSupport() {
		return fileUploadSupport;
	}

	public void setFileUploadSupport(boolean fileUploadSupport) {
		this.fileUploadSupport = fileUploadSupport;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getWintVersion() {
		return wintVersion;
	}

	public void setWintVersion(String wintVersion) {
		this.wintVersion = wintVersion;
	}

	public String getServletVersion() {
		return servletVersion;
	}

	public void setServletVersion(String servletVersion) {
		this.servletVersion = servletVersion;
	}

	public String getJunitVersion() {
		return junitVersion;
	}

	public void setJunitVersion(String junitVersion) {
		this.junitVersion = junitVersion;
	}

	public String getSpringVersion() {
		return springVersion;
	}

	public void setSpringVersion(String springVersion) {
		this.springVersion = springVersion;
	}

	public String getIbatisVersion() {
		return ibatisVersion;
	}

	public void setIbatisVersion(String ibatisVersion) {
		this.ibatisVersion = ibatisVersion;
	}

	public String getC3p0Version() {
		return c3p0Version;
	}

	public void setC3p0Version(String c3p0Version) {
		this.c3p0Version = c3p0Version;
	}

	public String getMysqlJdbcVersion() {
		return mysqlJdbcVersion;
	}

	public void setMysqlJdbcVersion(String mysqlJdbcVersion) {
		this.mysqlJdbcVersion = mysqlJdbcVersion;
	}

	public String getFileuploadVersion() {
		return fileuploadVersion;
	}

	public void setFileuploadVersion(String fileuploadVersion) {
		this.fileuploadVersion = fileuploadVersion;
	}

	public String getCommonsIOVersion() {
		return commonsIOVersion;
	}

	public void setCommonsIOVersion(String commonsIOVersion) {
		this.commonsIOVersion = commonsIOVersion;
	}

	public String getSlf4jApiVersion() {
		return slf4jApiVersion;
	}

	public void setSlf4jApiVersion(String slf4jApiVersion) {
		this.slf4jApiVersion = slf4jApiVersion;
	}

	public String getLogbackVersion() {
		return logbackVersion;
	}

	public void setLogbackVersion(String logbackVersion) {
		this.logbackVersion = logbackVersion;
	}

	public boolean isJrebelSupport() {
		return jrebelSupport;
	}

	public void setJrebelSupport(boolean jrebelSupport) {
		this.jrebelSupport = jrebelSupport;
	}

	public String getJrebelPath() {
		return jrebelPath;
	}

	public void setJrebelPath(String jrebelPath) {
		this.jrebelPath = jrebelPath;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPwd() {
		return dbPwd;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

    public String getWintPluginVersion() {
        return wintPluginVersion;
    }

    public void setWintPluginVersion(String wintPluginVersion) {
        this.wintPluginVersion = wintPluginVersion;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
}
