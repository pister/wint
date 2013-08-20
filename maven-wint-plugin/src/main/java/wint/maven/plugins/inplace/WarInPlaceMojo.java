package wint.maven.plugins.inplace;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * User: longyi
 * Date: 13-8-20
 * Time: 下午2:20
 *
 * @goal inplace
 * @requiresDependencyResolution runtime
 */
public class WarInPlaceMojo extends AbstractWarMojo {
    public void execute()
            throws MojoExecutionException, MojoFailureException
    {
        /*

        <configuration>
					<webResources>
						<resource>
							<directory>${basedir}/src/main/resources/web</directory>
							<targetPath>WEB-INF</targetPath>
							<filtering>true</filtering>
							<includes>
								<include>**  /*.xml</include>
        </includes>
        </resource>
        </webResources>
        </configuration>
         */

        Resource[] resources = new Resource[1];

        resources[0] = new Resource();
        try {
            resources[0].setDirectory(new File(this.getProject().getBasedir(), "src/main/resources/web").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        resources[0].setTargetPath("WEB-INF");
        resources[0].setFiltering(true);
        List<String> includes = Arrays.asList("**/*.xml");
        resources[0].setIncludes(includes);

        this.setWebResources(resources);

        getLog().info("clearing all old files ...");

        clearAll();
        getLog().info("clearing all old files finish.");


        getLog().info( "Generating webapp in source directory [" + getWarSourceDirectory() + "]" );

        buildExplodedWebapp( getWarSourceDirectory() );

        getLog().info("clear lib files...");
        clearLibFiles();
        getLog().info("clear lib files finish.");

        getLog().info("context:     " +this.getPluginContext());
    }

    protected void clearAll() {
        File file = getWebInfoPath();
        if (!file.exists()) {
             return;
        }
        deleteFiles(file);
    }

    private File getWebInfoPath() {
       return new File(this.getProject().getBasedir(), "/src/main/webapp/WEB-INF");
    }

    protected void clearLibFiles() {
        File file = getWebInfoPath();
        if (!file.exists()) {
            return;
        }
        File libFile = new File(file, "lib");
        if (!libFile.exists()) {
            return;
        }
        deleteFiles(libFile);
    }

    static void deleteFiles(File path) {
       if (!path.exists()) {
            return;
       }
       if (path.isFile()) {
           path.delete();
           return;
       }
       for (File file : path.listFiles()) {
           deleteFiles(file);
       }
       path.delete();
    }
}
