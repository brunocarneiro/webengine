package com.webengine.core;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.webengine.utils.URLStringUtils;

public class SimpleWebCrawler extends AbstractWebCrawler {

	@Override
	protected void deepRead(String href) throws Exception{
		read(href);
	}

	@Override
	protected void copyFile(String url) throws Exception{
		FileUtils.copyURLToFile(new URL(url), new File(COPY_DIR + URLStringUtils.getFullPathFile(url)));
	}

}
