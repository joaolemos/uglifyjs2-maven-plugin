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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.ArrayList;

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
	
		getLog().info(jsSourceDir.getAbsolutePath());
		
		getLog().info(jsOutputDir.getAbsolutePath());
		
		for(String jsExclude : jsExcludes) {
			getLog().info(jsExclude);
		}
	}
}
