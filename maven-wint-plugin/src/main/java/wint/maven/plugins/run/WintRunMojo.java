package wint.maven.plugins.run;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.MavenFileFilter;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import wint.maven.plugins.jetty.WintJettyRunMojoBase;
import wint.maven.plugins.war.AbstractWarMojo;
import wint.maven.plugins.war.WarInPlaceMojo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * @goal run
 * @requiresDependencyResolution runtime
 * @execute phase="test-compile"
 */
public class WintRunMojo extends WintJettyRunMojoBase {

    /**
     * Expression preceded with this String won't be interpolated.
     * <code>\${foo}</code> will be replaced with <code>${foo}</code>.
     *
     * @parameter expression="${maven.war.escapeString}"
     * @since 2.1-beta-1
     */
    protected String escapeString;
    /**
     * The Maven project.
     *
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    /**
     * The directory containing compiled classes.
     *
     * @parameter default-value="${project.build.outputDirectory}"
     * @required
     * @readonly
     */
    private File classesDirectory;
    /**
     * Whether a JAR file will be created for the classes in the webapp. Using this optional configuration
     * parameter will make the compiled classes to be archived into a JAR file
     * and the classes directory will then be excluded from the webapp.
     *
     * @parameter expression="${archiveClasses}" default-value="false"
     * @since 2.0.1
     */
    private boolean archiveClasses;
    /**
     * The JAR archiver needed for archiving the classes directory into a JAR file under WEB-INF/lib.
     *
     * @component role="org.codehaus.plexus.archiver.Archiver" role-hint="jar"
     * @required
     */
    private JarArchiver jarArchiver;
    /**
     * The directory where the webapp is built.
     *
     * @parameter default-value="${project.build.directory}/${project.build.finalName}"
     * @required
     */
    private File webappDirectory;
    /**
     * Single directory for extra files to include in the WAR. This is where
     * you place your JSP files.
     *
     * @parameter default-value="${basedir}/src/main/webapp"
     * @required
     */
    private File warSourceDirectory;
    /**
     * The list of webResources we want to transfer.
     *
     * @parameter
     */
    private Resource[] webResources;
    /**
     * Filters (property files) to include during the interpolation of the pom.xml.
     *
     * @parameter
     */
    private List filters;
    /**
     * The path to the web.xml file to use.
     *
     * @parameter expression="${maven.war.webxml}"
     */
    private File webXml;
    /**
     * The path to a configuration file for the servlet container. Note that
     * the file name may be different for different servlet containers.
     * Apache Tomcat uses a configuration file named context.xml. The file will
     * be copied to the META-INF directory.
     *
     * @parameter expression="${maven.war.containerConfigXML}"
     */
    private File containerConfigXML;
    /**
     * Directory to unpack dependent WARs into if needed.
     *
     * @parameter default-value="${project.build.directory}/war/work"
     * @required
     */
    private File workDirectory;
    /**
     * The file name mapping to use when copying libraries and TLDs. If no file mapping is
     * set (default) the files are copied with their standard names.
     *
     * @parameter
     * @since 2.1-alpha-1
     */
    private String outputFileNameMapping;
    /**
     * The file containing the webapp structure cache.
     *
     * @parameter default-value="${project.build.directory}/war/work/webapp-cache.xml"
     * @required
     * @since 2.1-alpha-1
     */
    private File cacheFile;
    /**
     * Whether the cache should be used to save the status of the webapp
     * across multiple runs. Experimental feature so disabled by default.
     *
     * @parameter expression="${useCache}" default-value="false"
     * @since 2.1-alpha-1
     */
    private boolean useCache = false;
    /**
     * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
     * @required
     * @readonly
     */
    private ArtifactFactory artifactFactory;
    /**
     * To look up Archiver/UnArchiver implementations.
     *
     * @component role="org.codehaus.plexus.archiver.manager.ArchiverManager"
     * @required
     */
    private ArchiverManager archiverManager;
    /**
     * @component role="org.apache.maven.shared.filtering.MavenFileFilter" role-hint="default"
     * @required
     */
    private MavenFileFilter mavenFileFilter;
    /**
     * @component role="org.apache.maven.shared.filtering.MavenResourcesFiltering" role-hint="default"
     * @required
     */
    private MavenResourcesFiltering mavenResourcesFiltering;
    /**
     * The comma separated list of tokens to include when copying the content
     * of the warSourceDirectory.
     *
     * @parameter alias="includes" default-value="**"
     */
    private String warSourceIncludes;
    /**
     * The comma separated list of tokens to exclude when copying the content
     * of the warSourceDirectory.
     *
     * @parameter alias="excludes"
     */
    private String warSourceExcludes;
    /**
     * The comma separated list of tokens to include when doing
     * a WAR overlay.
     * Default is '**'
     *
     * @parameter
     * @deprecated Use &lt;overlay&gt;/&lt;includes&gt; instead
     */
    private String dependentWarIncludes = "**/**";
    /**
     * The comma separated list of tokens to exclude when doing
     * a WAR overlay.
     *
     * @parameter
     * @deprecated Use &lt;overlay&gt;/&lt;excludes&gt; instead
     */
    private String dependentWarExcludes = "META-INF/**";
    /**
     * The overlays to apply.
     *
     * @parameter
     * @since 2.1-alpha-1
     */
    private List overlays = new ArrayList();
    /**
     * A list of file extensions that should not be filtered.
     * <b>Will be used when filtering webResources and overlays.</b>
     *
     * @parameter
     * @since 2.1-alpha-2
     */
    private List nonFilteredFileExtensions;
    /**
     * @parameter default-value="${session}"
     * @readonly
     * @required
     * @since 2.1-alpha-2
     */
    private MavenSession session;
    /**
     * To filter deployment descriptors. <b>Disabled by default.</b>
     *
     * @parameter expression="${maven.war.filteringDeploymentDescriptors}" default-value="false"
     * @since 2.1-alpha-2
     */
    private boolean filteringDeploymentDescriptors = false;
    /**
     * To escape interpolated values with Windows path
     * <code>c:\foo\bar</code> will be replaced with <code>c:\\foo\\bar</code>.
     *
     * @parameter expression="${maven.war.escapedBackslashesInFilePath}" default-value="false"
     * @since 2.1-alpha-2
     */
    private boolean escapedBackslashesInFilePath = false;

    public void execute() throws MojoExecutionException, MojoFailureException {
        WarInPlaceMojo warInPlaceMojo = new WarInPlaceMojo();
        try {
            fillFields(warInPlaceMojo);
        } catch (Exception e) {
            this.getLog().error(e);
        }
        warInPlaceMojo.execute();

        super.execute();
    }

    private void fillFields(WarInPlaceMojo warInPlaceMojo) throws Exception {
        Field[] fields = WintRunMojo.class.getDeclaredFields();
        Class<?> targetClass = AbstractWarMojo.class;
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(this);
            Field targetField = targetClass.getDeclaredField(field.getName());
            targetField.setAccessible(true);
            targetField.set(warInPlaceMojo, value);
        }
    }

}
