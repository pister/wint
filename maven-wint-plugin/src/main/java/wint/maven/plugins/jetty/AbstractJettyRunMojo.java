package wint.maven.plugins.jetty;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.Scanner;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.mortbay.jetty.plugin.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("deprecation")
public abstract class AbstractJettyRunMojo extends AbstractMojo {

    /**
     * A wrapper for the Server object
     */
    protected JettyServer server;

    /**
     * List of connectors to use. If none are configured
     * then the default is a single SelectChannelConnector at port 8080. You can
     * override this default port number by using the system property jetty.port
     * on the command line, eg:  mvn -Djetty.port=9999 jetty:run
     *
     * @parameter
     */
    protected Connector[] connectors;


    /**
     * List of other contexts to set up. Optional.
     *
     * @parameter
     */
    protected ContextHandler[] contextHandlers;


    /**
     * List of security realms to set up. Optional.
     *
     * @parameter
     */
    protected LoginService[] loginServices;


    /**
     * A RequestLog implementation to use for the webapp at runtime.
     * Optional.
     *
     * @parameter
     */
    protected RequestLog requestLog;


    /**
     * The "virtual" webapp created by the plugin
     *
     * @parameter
     */
    protected JettyWebAppContext webAppConfig;


    /**
     * The maven project.
     *
     * @parameter expression="${executedProject}"
     * @required
     * @readonly
     */
    protected MavenProject executedProject;


    /**
     * The context path for the webapp. Defaults to the
     * name of the webapp's artifact.
     *
     * @parameter expression="/${project.artifactId}"
     * @required
     * @readonly
     */
    protected String contextPath;


    /**
     * The temporary directory to use for the webapp.
     * Defaults to target/tmp
     *
     * @parameter expression="${project.build.directory}/tmp"
     * @required
     * @readonly
     */
    protected File tmpDirectory;


    /**
     * The interval in seconds to scan the webapp for changes
     * and restart the context if necessary. Ignored if reload
     * is enabled. Disabled by default.
     *
     * @parameter expression="${jetty.scanIntervalSeconds}" default-value="0"
     * @required
     */
    protected int scanIntervalSeconds;


    /**
     * reload can be set to either 'automatic' or 'manual'
     * <p>
     * if 'manual' then the context can be reloaded by a linefeed in the console
     * if 'automatic' then traditional reloading on changed files is enabled.
     *
     * @parameter expression="${jetty.reload}" default-value="automatic"
     */
    protected String reload;

    /**
     * File containing system properties to be set before execution
     * <p>
     * Note that these properties will NOT override System properties
     * that have been set on the command line, by the JVM, or directly
     * in the POM via systemProperties. Optional.
     *
     * @parameter expression="${jetty.systemPropertiesFile}"
     */
    protected File systemPropertiesFile;

    /**
     * System properties to set before execution.
     * Note that these properties will NOT override System properties
     * that have been set on the command line or by the JVM. They WILL
     * override System properties that have been set via systemPropertiesFile.
     * Optional.
     *
     * @parameter
     */
    protected SystemProperties systemProperties;


    /**
     * Location of a jetty xml configuration file whose contents
     * will be applied before any plugin configuration. Optional.
     *
     * @parameter
     */
    protected String jettyConfig;

    /**
     * Port to listen to stop jetty on executing -DSTOP.PORT=&lt;stopPort&gt;
     * -DSTOP.KEY=&lt;stopKey&gt; -jar start.jar --stop
     *
     * @parameter
     */
    protected int stopPort;

    /**
     * Key to provide when stopping jetty on executing java -DSTOP.KEY=&lt;stopKey&gt;
     * -DSTOP.PORT=&lt;stopPort&gt; -jar start.jar --stop
     *
     * @parameter
     */
    protected String stopKey;

    /**
     * <p>
     * Determines whether or not the server blocks when started. The default
     * behavior (daemon = false) will cause the server to pause other processes
     * while it continues to handle web requests. This is useful when starting the
     * server with the intent to work with it interactively.
     * </p><p>
     * Often, it is desirable to let the server start and continue running subsequent
     * processes in an automated build environment. This can be facilitated by setting
     * daemon to true.
     * </p>
     *
     * @parameter expression="${jetty.daemon}" default-value="false"
     */
    protected boolean daemon;

    /**
     * @parameter expression="${jetty.skip}" default-value="false"
     */
    protected boolean skip;


    /**
     * Location of a context xml configuration file whose contents
     * will be applied to the webapp AFTER anything in &lt;webAppConfig&gt;.Optional.
     *
     * @parameter
     */
    protected String webAppXml;

    /**
     * A scanner to check for changes to the webapp
     */
    protected Scanner scanner;

    /**
     * List of files and directories to scan
     */
    protected ArrayList<File> scanList;

    /**
     * List of Listeners for the scanner
     */
    protected ArrayList<Scanner.BulkListener> scannerListeners;


    /**
     * A scanner to check ENTER hits on the console
     */
    protected Thread consoleScanner;


    public String PORT_SYSPROPERTY = "jetty.port";


    public abstract void checkPomConfiguration() throws MojoExecutionException;

    public abstract void configureScanner() throws MojoExecutionException;

    public abstract void applyJettyXml() throws Exception;

    public abstract void finishConfigurationBeforeStart() throws Exception;


    public MavenProject getExecutedProject() {
        return this.executedProject;
    }

    public File getTmpDirectory() {
        return this.tmpDirectory;
    }

    /**
     * @return Returns the contextPath.
     */
    public String getContextPath() {
        return this.contextPath;
    }

    /**
     * @return Returns the scanIntervalSeconds.
     */
    public int getScanIntervalSeconds() {
        return this.scanIntervalSeconds;
    }

    /**
     * @return returns the path to the systemPropertiesFile
     */
    public File getSystemPropertiesFile() {
        return this.systemPropertiesFile;
    }

    public void setSystemPropertiesFile(File file) throws Exception {
        this.systemPropertiesFile = file;
        FileInputStream propFile = new FileInputStream(systemPropertiesFile);
        Properties properties = new Properties();
        properties.load(propFile);

        if (this.systemProperties == null)
            this.systemProperties = new SystemProperties();

        for (Enumeration keys = properties.keys(); keys.hasMoreElements(); ) {
            String key = (String) keys.nextElement();
            if (!systemProperties.containsSystemProperty(key)) {
                SystemProperty prop = new SystemProperty();
                prop.setKey(key);
                prop.setValue(properties.getProperty(key));

                this.systemProperties.setSystemProperty(prop);
            }
        }

    }

    public void setSystemProperties(SystemProperties systemProperties) {
        if (this.systemProperties == null)
            this.systemProperties = systemProperties;
        else {
            Iterator itor = systemProperties.getSystemProperties().iterator();
            while (itor.hasNext()) {
                SystemProperty prop = (SystemProperty) itor.next();
                this.systemProperties.setSystemProperty(prop);
            }
        }
    }

    public List<File> getJettyXmlFiles() {
        if (this.jettyConfig == null) {
            return null;
        }

        List<File> jettyXmlFiles = new ArrayList<File>();

        if (this.jettyConfig.indexOf(',') == -1) {
            jettyXmlFiles.add(new File(this.jettyConfig));
        } else {
            String[] files = this.jettyConfig.split(",");

            for (String file : files) {
                jettyXmlFiles.add(new File(file));
            }
        }

        return jettyXmlFiles;
    }


    public JettyServer getServer() {
        return this.server;
    }

    public void setServer(JettyServer server) {
        this.server = server;
    }


    public void setScanList(ArrayList<File> list) {
        this.scanList = new ArrayList<File>(list);
    }

    public ArrayList<File> getScanList() {
        return this.scanList;
    }


    public void setScannerListeners(ArrayList<Scanner.BulkListener> listeners) {
        this.scannerListeners = new ArrayList<Scanner.BulkListener>(listeners);
    }

    public ArrayList getScannerListeners() {
        return this.scannerListeners;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Configuring Jetty for project: " + getExecutedProject().getName());
        if (skip) {
            getLog().info("Skipping Jetty start: jetty.skip==true");
            return;
        }
        PluginLog.setLog(getLog());
        checkPomConfiguration();
        startJetty();
    }


    public void startJetty() throws MojoExecutionException {
        try {
            getLog().debug("Starting Jetty Server ...");

            printSystemProperties();
            this.server = new JettyServer();
            setServer(this.server);

            //apply any config from a jetty.xml file first which is able to
            //be overwritten by config in the pom.xml
            applyJettyXml();


            // if the user hasn't configured their project's pom to use a
            // different set of connectors,
            // use the default
            Connector[] connectors = this.server.getConnectors();
            if (connectors == null || connectors.length == 0) {
                //try using ones configured in pom
                this.server.setConnectors(this.connectors);

                connectors = this.server.getConnectors();
                if (connectors == null || connectors.length == 0) {
                    //if a SystemProperty -Djetty.port=<portnum> has been supplied, use that as the default port
                    this.connectors = new Connector[]{this.server.createDefaultConnector(System.getProperty(PORT_SYSPROPERTY, null))};
                    this.server.setConnectors(this.connectors);
                }
            }


            //set up a RequestLog if one is provided
            if (this.requestLog != null)
                getServer().setRequestLog(this.requestLog);

            //set up the webapp and any context provided
            this.server.configureHandlers();
            configureWebApplication();
            this.server.addWebApplication(webAppConfig);

            // set up security realms
            for (int i = 0; (this.loginServices != null) && i < this.loginServices.length; i++) {
                getLog().debug(this.loginServices[i].getClass().getName() + ": " + this.loginServices[i].toString());
                getServer().addBean(this.loginServices[i]);
            }

            //do any other configuration required by the
            //particular Jetty version
            finishConfigurationBeforeStart();

            // start Jetty
            this.server.start();

            getLog().info("Started Jetty Server");

            if (stopPort > 0 && stopKey != null) {
                Monitor monitor = new Monitor(stopPort, stopKey, new Server[]{server}, !daemon);
                monitor.start();
            }

            // start the scanner thread (if necessary) on the main webapp
            configureScanner();
            startScanner();

            // start the new line scanner thread if necessary
            startConsoleScanner();

            // keep the thread going if not in daemon mode
            if (!daemon) {
                server.join();
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failure", e);
        } finally {
            if (!daemon) {
                getLog().info("Jetty server exiting.");
            }
        }

    }


    public abstract void restartWebApp(boolean reconfigureScanner) throws Exception;

    /**
     * Subclasses should invoke this to setup basic info
     * on the webapp
     *
     * @throws MojoExecutionException
     */
    public void configureWebApplication() throws Exception {
        //As of jetty-7, you must use a <webAppConfig> element
        if (webAppConfig == null)
            webAppConfig = new JettyWebAppContext();

        //Apply any context xml file to set up the webapp
        //CAUTION: if you've defined a <webAppConfig> element then the
        //context xml file can OVERRIDE those settings
        if (webAppXml != null) {
            File file = FileUtils.getFile(webAppXml);
            XmlConfiguration xmlConfiguration = new XmlConfiguration(file.toURI().toURL());
            getLog().info("Applying context xml file " + webAppXml);
            xmlConfiguration.configure(webAppConfig);
        }


        //If no contextPath was specified, go with our default
        String cp = webAppConfig.getContextPath();
        if (cp == null || "".equals(cp)) {
            webAppConfig.setContextPath((contextPath.startsWith("/") ? contextPath : "/" + contextPath));
        }

        //If no tmp directory was specified, and we have one, use it
        if (webAppConfig.getTempDirectory() == null && tmpDirectory != null) {
            if (!tmpDirectory.exists())
                tmpDirectory.mkdirs();

            webAppConfig.setTempDirectory(tmpDirectory);
        }

        getLog().info("Context path = " + webAppConfig.getContextPath());
        getLog().info("Tmp directory = " + (webAppConfig.getTempDirectory() == null ? " determined at runtime" : webAppConfig.getTempDirectory()));
        getLog().info("Web defaults = " + (webAppConfig.getDefaultsDescriptor() == null ? " jetty default" : webAppConfig.getDefaultsDescriptor()));
        getLog().info("Web overrides = " + (getOverrideDescriptor(webAppConfig) == null ? " none" : getOverrideDescriptor(webAppConfig)));
    }

    private static String getOverrideDescriptor(JettyWebAppContext webAppConfig) {
        List<String> descriptors = webAppConfig.getOverrideDescriptors();
        if (descriptors.size() != 1)
            return null;
        return descriptors.get(0);
    }

    /**
     * Run a scanner thread on the given list of files and directories, calling
     * stop/start on the given list of LifeCycle objects if any of the watched
     * files change.
     */
    private void startScanner() {
        // check if scanning is enabled
        if (getScanIntervalSeconds() <= 0) return;

        // check if reload is manual. It disables file scanning
        if ("manual".equalsIgnoreCase(reload)) {
            // issue a warning if both scanIntervalSeconds and reload
            // are enabled
            getLog().warn("scanIntervalSeconds is set to " + scanIntervalSeconds + " but will be IGNORED due to manual reloading");
            return;
        }

        scanner = new Scanner();
        scanner.setReportExistingFilesOnStartup(false);
        scanner.setScanInterval(getScanIntervalSeconds());
        scanner.setScanDirs(getScanList());
        scanner.setRecursive(true);
        List listeners = getScannerListeners();
        Iterator itor = (listeners == null ? null : listeners.iterator());
        while (itor != null && itor.hasNext())
            scanner.addListener((Scanner.Listener) itor.next());
        getLog().info("Starting scanner at interval of " + getScanIntervalSeconds() + " seconds.");
        try {
            scanner.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Run a thread that monitors the console input to detect ENTER hits.
     */
    protected void startConsoleScanner() {
        if ("manual".equalsIgnoreCase(reload)) {
            getLog().info("Console reloading is ENABLED. Hit ENTER on the console to restart the context.");
            consoleScanner = new ConsoleScanner(this);
            consoleScanner.start();
        }
    }

    private void printSystemProperties() {
        // print out which system properties were set up
        if (getLog().isDebugEnabled()) {
            if (systemProperties != null) {
                Iterator itor = systemProperties.getSystemProperties().iterator();
                while (itor.hasNext()) {
                    SystemProperty prop = (SystemProperty) itor.next();
                    getLog().debug("Property " + prop.getName() + "=" + prop.getValue() + " was " + (prop.isSet() ? "set" : "skipped"));
                }
            }
        }
    }

    /**
     * Try and find a jetty-web.xml file, using some
     * historical naming conventions if necessary.
     *
     * @param webInfDir
     * @return the jetty web xml file
     */
    public File findJettyWebXmlFile(File webInfDir) {
        if (webInfDir == null)
            return null;
        if (!webInfDir.exists())
            return null;

        File f = new File(webInfDir, "jetty-web.xml");
        if (f.exists())
            return f;

        //try some historical alternatives
        f = new File(webInfDir, "web-jetty.xml");
        if (f.exists())
            return f;

        return null;
    }
}
