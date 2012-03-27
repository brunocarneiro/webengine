package com.webengine.core;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.webengine.utils.URLStringUtils;

public class SimpleWebCrawler extends AbstractWebCrawler {

	private List<String> tested = new ArrayList<String>();
	
	private String dominio;
	private Map<String, String[]> map;
	
	
	public SimpleWebCrawler(String dominio, Map<String, String[]> map) {
		super();
		this.dominio = dominio;
		this.map = map;
	}

	@Override
	protected void deepRead(String href) throws Exception{
		read(href);
	}

	@Override
	protected void copyFile(String url) throws Exception{
		FileUtils.copyURLToFile(new URL(url), new File(COPY_DIR + URLStringUtils.getFullPathFile(url)));
	}

	@Override
	protected void tested(String url) throws Exception {
		tested.add(url);
	}

	@Override
	protected boolean wasTested(String url) throws Exception {
		return tested.contains(url);
	}

	@Override
	protected String getDominio() throws Exception {
		return dominio;
	}

	@Override
	protected Map<String, String[]> getMap() throws Exception {
		return map;
	}
	
	
}
