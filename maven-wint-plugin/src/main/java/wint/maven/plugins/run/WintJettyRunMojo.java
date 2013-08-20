package wint.maven.plugins.run;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.Scanner;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.mortbay.jetty.plugin.ScanTargetPattern;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @goal jetty-run
 * @requiresDependencyResolution runtime
 *
 * @execute phase="test-compile"
 */
public class WintJettyRunMojo extends AbstractRunMojo {

    /**
     * If true, the &lt;testOutputDirectory&gt;
     * and the dependencies of &lt;scope&gt;test&lt;scope&gt;
     * will be put first on the runtime classpath.
     * @parameter default-value="false"
     */
    private boolean useTestClasspath;



    /**
     * The default location of the web.xml file. Will be used
     * if <webAppConfig><descriptor> is not set.
     *
     * @parameter expression="${maven.war.webxml}"
     * @readonly
     */
    private String webXml;


    /**
     * The directory containing generated classes.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     *
     */
    private File classesDirectory;



    /**
     * The directory containing generated test classes.
     *
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     */
    private File testClassesDirectory;

    /**
     * Root directory for all html/jsp etc files
     *
     * @parameter expression="${maven.war.src}"
     *
     */
    private File webAppSourceDirectory;

    /**
     * @parameter expression="${plugin.artifacts}"
     * @readonly
     */
    private List pluginArtifacts;

    /**
     * List of files or directories to additionally periodically scan for changes. Optional.
     * @parameter
     */
    private File[] scanTargets;


    /**
     * List of directories with ant-style &lt;include&gt; and &lt;exclude&gt; patterns
     * for extra targets to periodically scan for changes. Can be used instead of,
     * or in conjunction with &lt;scanTargets&gt;.Optional.
     * @parameter
     */
    private ScanTargetPattern[] scanTargetPatterns;


    /**
     * List of files on the classpath for the webapp
     */
    private List<File> classPathFiles;


    /**
     * Extra scan targets as a list
     */
    private List<File> extraScanTargets;


    public String getWebXml()
    {
        return this.webXml;
    }



    public File getClassesDirectory()
    {
        return this.classesDirectory;
    }

    public File getWebAppSourceDirectory()
    {
        return this.webAppSourceDirectory;
    }



    public void setClassPathFiles (List<File> list)
    {
        this.classPathFiles = new ArrayList<File>(list);
    }

    public List<File> getClassPathFiles ()
    {
        return this.classPathFiles;
    }


    public List<File> getExtraScanTargets ()
    {
        return this.extraScanTargets;
    }

    public void setExtraScanTargets(List<File> list)
    {
        this.extraScanTargets = list;
    }



    /**
     * Verify the configuration given in the pom.
     *
     * @see org.mortbay.jetty.plugin.AbstractJettyMojo#checkPomConfiguration()
     */
    public void checkPomConfiguration () throws MojoExecutionException
    {
        // check the location of the static content/jsps etc
        try
        {
            if ((getWebAppSourceDirectory() == null) || !getWebAppSourceDirectory().exists())
            {
                webAppSourceDirectory = new File (project.getBasedir(), "src"+File.separator+"main"+File.separator+"webapp");
                getLog().info("webAppSourceDirectory "+getWebAppSourceDirectory() +" does not exist. Defaulting to "+webAppSourceDirectory.getAbsolutePath());
            }
            else
                getLog().info( "Webapp source directory = " + getWebAppSourceDirectory().getCanonicalPath());
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Webapp source directory does not exist", e);
        }

        // check reload mechanic
        if ( !"automatic".equalsIgnoreCase( reload ) && !"manual".equalsIgnoreCase( reload ) )
        {
            throw new MojoExecutionException( "invalid reload mechanic specified, must be 'automatic' or 'manual'" );
        }
        else
        {
            getLog().info("Reload Mechanic: " + reload );
        }


        // check the classes to form a classpath with
        try
        {
            //allow a webapp with no classes in it (just jsps/html)
            if (getClassesDirectory() != null)
            {
                if (!getClassesDirectory().exists())
                    getLog().info( "Classes directory "+ getClassesDirectory().getCanonicalPath()+ " does not exist");
                else
                    getLog().info("Classes = " + getClassesDirectory().getCanonicalPath());
            }
            else
                getLog().info("Classes directory not set");
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Location of classesDirectory does not exist");
        }


        setExtraScanTargets(new ArrayList<File>());
        if (scanTargets != null)
        {
            for (int i=0; i< scanTargets.length; i++)
            {
                getLog().info("Added extra scan target:"+ scanTargets[i]);
                getExtraScanTargets().add(scanTargets[i]);
            }
        }


        if (scanTargetPatterns!=null)
        {
            for (int i=0;i<scanTargetPatterns.length; i++)
            {
                Iterator itor = scanTargetPatterns[i].getIncludes().iterator();
                StringBuffer strbuff = new StringBuffer();
                while (itor.hasNext())
                {
                    strbuff.append((String)itor.next());
                    if (itor.hasNext())
                        strbuff.append(",");
                }
                String includes = strbuff.toString();

                itor = scanTargetPatterns[i].getExcludes().iterator();
                strbuff= new StringBuffer();
                while (itor.hasNext())
                {
                    strbuff.append((String)itor.next());
                    if (itor.hasNext())
                        strbuff.append(",");
                }
                String excludes = strbuff.toString();

                try
                {
                    List<File> files = FileUtils.getFiles(scanTargetPatterns[i].getDirectory(), includes, excludes);
                    itor = files.iterator();
                    while (itor.hasNext())
                        getLog().info("Adding extra scan target from pattern: "+itor.next());
                    List<File> currentTargets = getExtraScanTargets();
                    if(currentTargets!=null && !currentTargets.isEmpty())
                        currentTargets.addAll(files);
                    else
                        setExtraScanTargets(files);
                }
                catch (IOException e)
                {
                    throw new MojoExecutionException(e.getMessage());
                }
            }
        }
    }





    public void configureWebApplication() throws Exception
    {
        super.configureWebApplication();

        //Set up the location of the webapp.
        //There are 2 parts to this: setWar() and setBaseResource(). On standalone jetty,
        //the former could be the location of a packed war, while the latter is the location
        //after any unpacking. With this mojo, you are running an unpacked, unassembled webapp,
        //so the two locations should be equal.
        Resource webAppSourceDirectoryResource = Resource.newResource(webAppSourceDirectory.getCanonicalPath());
        if (webAppConfig.getWar() == null)
            webAppConfig.setWar(webAppSourceDirectoryResource.toString());

        if (webAppConfig.getBaseResource() == null)
            webAppConfig.setBaseResource(webAppSourceDirectoryResource);

        webAppConfig.setWebInfClasses (getClassesDirs());
        webAppConfig.setWebInfLib (getDependencyFiles());

        setClassPathFiles(setUpClassPath(webAppConfig.getWebInfClasses(), webAppConfig.getWebInfLib()));

        //if we have not already set web.xml location, need to set one up
        if (webAppConfig.getDescriptor() == null)
        {
            //Has an explicit web.xml file been configured to use?
            if (webXml != null)
            {
                Resource r = Resource.newResource(webXml);
                if (r.exists() && !r.isDirectory())
                {
                    webAppConfig.setDescriptor(r.toString());
                }
            }

            //Still don't have a web.xml file: try the resourceBase of the webapp, if it is set
            if (webAppConfig.getDescriptor() == null && webAppConfig.getBaseResource() != null)
            {
                Resource r = webAppConfig.getBaseResource().addPath("WEB-INF/web.xml");
                if (r.exists() && !r.isDirectory())
                {
                    webAppConfig.setDescriptor(r.toString());
                }
            }

            //Still don't have a web.xml file: finally try the configured static resource directory if there is one
            if (webAppConfig.getDescriptor() == null && (webAppSourceDirectory != null))
            {
                File f = new File (new File (webAppSourceDirectory, "WEB-INF"), "web.xml");
                if (f.exists() && f.isFile())
                {
                    webAppConfig.setDescriptor(f.getCanonicalPath());
                }
            }
        }
        getLog().info( "web.xml file = "+webAppConfig.getDescriptor());

        if (webAppConfig.getClassPathFiles() == null)
            webAppConfig.setClassPathFiles(getClassPathFiles());


        getLog().info("Webapp directory = " + getWebAppSourceDirectory().getCanonicalPath());
    }

    public void configureScanner ()
            throws MojoExecutionException
    {
        // start the scanner thread (if necessary) on the main webapp
        final ArrayList<File> scanList = new ArrayList<File>();
        if (webAppConfig.getDescriptor() != null)
        {
            try
            {
                Resource r = Resource.newResource(webAppConfig.getDescriptor());
                scanList.add(r.getFile());
            }
            catch (IOException e)
            {
                throw new MojoExecutionException("Problem configuring scanner for web.xml", e);
            }
        }

        if (webAppConfig.getJettyEnvXml() != null)
        {
            try
            {
                Resource r = Resource.newResource(webAppConfig.getJettyEnvXml());
                scanList.add(r.getFile());
            }
            catch (IOException e)
            {
                throw new MojoExecutionException("Problem configuring scanner for jetty-env.xml", e);
            }
        }

        if (webAppConfig.getDefaultsDescriptor() != null)
        {
            try
            {
                if (!WebAppContext.WEB_DEFAULTS_XML.equals(webAppConfig.getDefaultsDescriptor()))
                {
                    Resource r = Resource.newResource(webAppConfig.getDefaultsDescriptor());
                    scanList.add(r.getFile());
                }
            }
            catch (IOException e)
            {
                throw new MojoExecutionException("Problem configuring scanner for webdefaults.xml", e);
            }
        }

        if (webAppConfig.getOverrideDescriptor() != null)
        {
            try
            {
                Resource r = Resource.newResource(webAppConfig.getOverrideDescriptor());
                scanList.add(r.getFile());
            }
            catch (IOException e)
            {
                throw new MojoExecutionException("Problem configuring scanner for webdefaults.xml", e);
            }
        }


        File jettyWebXmlFile = findJettyWebXmlFile(new File(getWebAppSourceDirectory(),"WEB-INF"));
        if (jettyWebXmlFile != null)
            scanList.add(jettyWebXmlFile);
        scanList.addAll(getExtraScanTargets());
        scanList.add(getProject().getFile());
        scanList.addAll(getClassPathFiles());
        setScanList(scanList);
        ArrayList<Scanner.BulkListener> listeners = new ArrayList<Scanner.BulkListener>();
        listeners.add(new Scanner.BulkListener()
        {
            public void filesChanged (List changes)
            {
                try
                {
                    boolean reconfigure = changes.contains(getProject().getFile().getCanonicalPath());
                    restartWebApp(reconfigure);
                }
                catch (Exception e)
                {
                    getLog().error("Error reconfiguring/restarting webapp after change in watched files",e);
                }
            }
        });
        setScannerListeners(listeners);
    }

    public void restartWebApp(boolean reconfigureScanner) throws Exception
    {
        getLog().info("restarting "+webAppConfig);
        getLog().debug("Stopping webapp ...");
        webAppConfig.stop();
        getLog().debug("Reconfiguring webapp ...");

        checkPomConfiguration();
        configureWebApplication();

        // check if we need to reconfigure the scanner,
        // which is if the pom changes
        if (reconfigureScanner)
        {
            getLog().info("Reconfiguring scanner after change to pom.xml ...");
            scanList.clear();
            scanList.add(new File(webAppConfig.getDescriptor()));
            if (webAppConfig.getJettyEnvXml() != null)
                scanList.add(new File(webAppConfig.getJettyEnvXml()));
            scanList.addAll(getExtraScanTargets());
            scanList.add(getProject().getFile());
            scanList.addAll(getClassPathFiles());
            getScanner().setScanDirs(scanList);
        }

        getLog().debug("Restarting webapp ...");
        webAppConfig.start();
        getLog().info("Restart completed at "+new Date().toString());
    }

    private List<File> getDependencyFiles ()
    {
        List<File> dependencyFiles = new ArrayList<File>();
        List<Resource> overlays = new ArrayList<Resource>();
        for ( Iterator<Artifact> iter = getProject().getArtifacts().iterator(); iter.hasNext(); )
        {
            Artifact artifact = (Artifact) iter.next();
            // Include runtime and compile time libraries, and possibly test libs too
            if(artifact.getType().equals("war"))
            {
                try
                {
                    Resource r=Resource.newResource("jar:"+artifact.getFile().toURL().toString()+"!/");
                    overlays.add(r);
                    getExtraScanTargets().add(artifact.getFile());
                }
                catch(Exception e)
                {
                    throw new RuntimeException(e);
                }
                continue;
            }
            if (((!Artifact.SCOPE_PROVIDED.equals(artifact.getScope())) && (!Artifact.SCOPE_TEST.equals( artifact.getScope())))
                    ||
                    (useTestClasspath && Artifact.SCOPE_TEST.equals( artifact.getScope())))
            {
                dependencyFiles.add(artifact.getFile());
                getLog().debug( "Adding artifact " + artifact.getFile().getName() + " for WEB-INF/lib " );
            }
        }

        webAppConfig.setOverlays(overlays);


        return dependencyFiles;
    }




    private List<File> setUpClassPath(List<File> webInfClasses, List<File> webInfJars)
    {
        List<File> classPathFiles = new ArrayList<File>();
        classPathFiles.addAll(webInfClasses);
        classPathFiles.addAll(webInfJars);

        if (getLog().isDebugEnabled())
        {
            for (int i = 0; i < classPathFiles.size(); i++)
            {
                getLog().debug("classpath element: "+ ((File) classPathFiles.get(i)).getName());
            }
        }
        return classPathFiles;
    }

    private List<File> getClassesDirs ()
    {
        List<File> classesDirs = new ArrayList<File>();

        //if using the test classes, make sure they are first
        //on the list
        if (useTestClasspath && (testClassesDirectory != null))
            classesDirs.add(testClassesDirectory);

        if (getClassesDirectory() != null)
            classesDirs.add(getClassesDirectory());

        return classesDirs;
    }


    public void finishConfigurationBeforeStart() throws Exception
    {
        HandlerCollection contexts = (HandlerCollection)server.getChildHandlerByClass(ContextHandlerCollection.class);
        if (contexts==null)
            contexts = (HandlerCollection)server.getChildHandlerByClass(HandlerCollection.class);

        for (int i=0; (this.contextHandlers != null) && (i < this.contextHandlers.length); i++)
        {
            contexts.addHandler(this.contextHandlers[i]);
        }
    }




    public void applyJettyXml() throws Exception
    {
        if (getJettyXmlFiles() == null)
            return;

        for ( File xmlFile : getJettyXmlFiles() )
        {
            getLog().info( "Configuring Jetty from xml configuration file = " + xmlFile.getCanonicalPath() );
            XmlConfiguration xmlConfiguration = new XmlConfiguration(xmlFile.toURI().toURL());
            xmlConfiguration.configure(this.server);
        }
    }



    public void execute() throws MojoExecutionException, MojoFailureException
    {
        super.execute();
    }
}
