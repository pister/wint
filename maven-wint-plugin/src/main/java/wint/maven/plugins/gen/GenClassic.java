package wint.maven.plugins.gen;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import wint.maven.plugins.gen.common.ProjectConfig;
import wint.maven.plugins.gen.common.ProjectGenerator;
import wint.maven.plugins.gen.common.util.StringUtil;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal gen
 * @requiresProject false
 * 
 * @phase process-sources
 */
public class GenClassic extends AbstractMojo {
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private volatile File outputDirectory;

	private static void out(String msg) {
		System.out.println(msg);
	}

	private static ProjectConfig enter(BufferedReader reader) throws IOException {
		String groupId = null;
		do {
			out("step 1. please enter your project groupId:");
			groupId = StringUtil.trimToEmpty(reader.readLine());
		} while (StringUtil.isEmpty(groupId));
		
		String artifactId = null;
		do {
			out("step 2. please enter your project artifactId:");
			artifactId = StringUtil.trimToEmpty(reader.readLine());
		} while (StringUtil.isEmpty(artifactId));


		String wintPackage = null;
		do {
			out("step 3. please enter the project package(for example: com.company.project):");
			wintPackage = StringUtil.trimToEmpty(reader.readLine());
		} while (StringUtil.isEmpty(wintPackage));

		ProjectConfig projectConfig = new ProjectConfig();
		projectConfig.setGroupId(groupId);
		projectConfig.setArtifactId(artifactId);
		projectConfig.setWintPackage(wintPackage);
		
		projectConfig.setDbName(artifactId);
		projectConfig.setDbUser(artifactId + "_user");
		projectConfig.setDbPwd(artifactId + "_pwd");
		
		return projectConfig;

	}

	private static void createProject(ProjectConfig projectConfig, BufferedReader reader, Log log) throws IOException {
		ProjectGenerator projectGenerator = new ProjectGenerator(log);
		projectGenerator.genProject(projectConfig);
		File path = new File(projectConfig.getPath());
		File projectPath = new File(path, projectConfig.getArtifactId());
		out("create project " + projectConfig.getArtifactId() + " success at " + projectPath.getCanonicalPath());
	}

	public void execute() throws MojoExecutionException {
		try {
			System.out.println("outputDirectory:" + outputDirectory);
			
			File currentFile = new File(".");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			out("creating wint project... (3 steps)");

			while (true) {
				ProjectConfig projectConfig = enter(reader);
				projectConfig.setPath(currentFile.getCanonicalPath());
				out("your wint project will be:");
				out("groupId: " + projectConfig.getGroupId());
				out("artifactId: " + projectConfig.getArtifactId());
				out("package: " + projectConfig.getWintPackage());
				out("are your sure? (Y/N)");
				String yes = null;
				do {
					yes = StringUtil.trimToEmpty(reader.readLine());
				} while (StringUtil.isEmpty(yes));
				yes = yes.toLowerCase();
				if (yes.equals("y") || yes.equals("yes") || yes.equals("ok")) {
					createProject(projectConfig, reader, getLog());
					break;
				}
			}
		} catch (Exception e) {
			throw new MojoExecutionException("create wint project error", e);
		}
	}
}
