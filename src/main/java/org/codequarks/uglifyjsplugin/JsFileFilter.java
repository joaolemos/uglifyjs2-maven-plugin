package org.codequarks.uglifyjsplugin;

import java.io.File;
import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class JsFileFilter implements IOFileFilter {
	
	private static final String[] JS_EXTENSIONS = {"*.js"};
	
	private List<String> jsExcludes;

	public JsFileFilter(List<String> jsExcludes) {
		this.jsExcludes = jsExcludes;
	}

	@Override
	public boolean accept(File file) {
		WildcardFileFilter jsIncludesFilter = new WildcardFileFilter(JS_EXTENSIONS);
		WildcardFileFilter jsExcludesFilter = new WildcardFileFilter(jsExcludes);
		return jsIncludesFilter.accept(file) && !jsExcludesFilter.accept(file);
	}

	@Override
	public boolean accept(File dir, String name) {
		return true;
	}
}