package com.webengine.core;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;

import com.webengine.utils.URLStringUtils;


public class ParallelWebCrawler extends AbstractWebCrawler {

	private static ParallelWebCrawler instance;
	private ExecutorService executor = Executors.newCachedThreadPool();
	private List<String> tested;
	private String dominio;
	private Map<String, String[]> map;
	
	private ParallelWebCrawler(String dominio,Map<String, String[]> map) {
		tested = new ArrayList<String>();
		this.dominio = dominio;
		this.map = map;
	}
	
	public static ParallelWebCrawler getInstance(String dominio,Map<String, String[]> map){
		if(instance==null)
			instance = new ParallelWebCrawler(dominio, map);
		return instance;
	}
	
	public void start(String url){
		executor.execute(new ParallelWebCrawlerRunnable(url,this));
	}
	

	@Override
	protected void deepRead(String href) throws Exception {
		executor.execute(new ParallelWebCrawlerRunnable(href, this));
	}

	@Override
	protected void copyFile(String url) throws Exception {
		FileUtils.copyURLToFile(new URL(url), new File(COPY_DIR + URLStringUtils.getFullPathFile(url)));
	}

	@Override
	protected void tested(String url) throws Exception {
		synchronized (this) {
			tested.add(url);
		}
	}

	@Override
	protected boolean wasTested(String url) throws Exception {
		synchronized (this) {
			return tested.contains(url);
		}
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

class ParallelWebCrawlerRunnable implements Runnable{
	
	private String url;
	
	private ParallelWebCrawler parallelWebCrawler;
	
	public ParallelWebCrawlerRunnable(String url, ParallelWebCrawler parallelWebCrawler){
		this.url = url;
		this.parallelWebCrawler =parallelWebCrawler;
	}

	@Override
	public void run() {
		parallelWebCrawler.read(url);
	}
}
