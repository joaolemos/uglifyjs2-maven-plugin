package org.codequarks.uglifyjsplugin;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal uglify
 * 
 * @phase prepare-package
 */
public class UglifyJs2Mojo extends AbstractMojo {

	/**
	 * Source directory of the JavaScript files
	 * 
	 * @parameter expression="${basedir}/src/main/webapp/js"
	 * @required
	 */
	private File jsSourceDir;

	/**
	 * Output directory for the minified JavaScript files.
	 * 
	 * @parameter expression="${project.build.directory}/webapp-build"
	 * @required
	 */
	private File jsOutputDir;

	/**
	 * Files/folders to exclude from minification
	 * 
	 * @parameter expression="${jsExcludes}" alias="resources"
	 */
	private ArrayList<String> jsExcludes;

	public void execute() throws MojoExecutionException {
		JsFileFilter jsFileFilter = new JsFileFilter(jsExcludes);
		Collection<File> jsFiles = FileUtils.listFiles(jsSourceDir,
				jsFileFilter, TrueFileFilter.INSTANCE);
		for (File file : jsFiles) {
			try {
				Runtime rt = Runtime.getRuntime();
				String command = "uglifyjs " + file.getPath() + " -o " + this.getOutputFile(file).getPath();
				getLog().info("Minimizing " + file.getName() + "...");
				rt.exec(command);
			 } catch (IOException e) { 
				 throw new MojoExecutionException("Failed to execute uglifyjs process", e);
			 }
		}
	}

	private final File getOutputFile(File inputFile) throws IOException {
		String relativePath = jsSourceDir.toURI().relativize(inputFile.getParentFile().toURI()).getPath();
		File outputBaseDir = new File(jsOutputDir, relativePath);
		if (!outputBaseDir.exists())
			FileUtils.forceMkdir(outputBaseDir);
		return new File(outputBaseDir, inputFile.getName());
	}
}
