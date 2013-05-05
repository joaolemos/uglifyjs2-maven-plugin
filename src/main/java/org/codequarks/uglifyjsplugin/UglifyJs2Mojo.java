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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

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
				Process proc = runUglifyJs2Process(file);
				if(proc.waitFor() != 0) {
					warnOfUglifyJs2Error(proc.getErrorStream(), file.getName());
				}
			 } catch (IOException e) { 
				 throw new MojoExecutionException("Failed to execute uglifyjs process", e);
			 } catch (InterruptedException e) {
				 throw new MojoExecutionException("UglifyJs process interrupted", e);
			 }
		}
	}
	
	private Process runUglifyJs2Process(File inputFile) throws IOException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		String command = "uglifyjs " + inputFile.getPath() + " -o " + this.getOutputFile(inputFile).getPath();
		getLog().info("Minifying " + inputFile.getName() + "...");
		getLog().debug("Minifying from " + inputFile.getPath() + " to " + this.getOutputFile(inputFile).getPath());
		return rt.exec(command);
	}
	
	private void warnOfUglifyJs2Error(InputStream errorStream, String fileName) throws IOException {
		String error = IOUtils.toString(errorStream, "UTF-8");
		getLog().warn("Error while minifying " + fileName + ":" + error);
		getLog().warn(fileName + " was not minified. Continuing...");
	}

	private final File getOutputFile(File inputFile) throws IOException {
		String relativePath = jsSourceDir.toURI().relativize(inputFile.getParentFile().toURI()).getPath();
		File outputBaseDir = new File(jsOutputDir, relativePath);
		if (!outputBaseDir.exists())
			FileUtils.forceMkdir(outputBaseDir);
		return new File(outputBaseDir, inputFile.getName());
	}
}