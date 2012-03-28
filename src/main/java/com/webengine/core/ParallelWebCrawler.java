package com.webengine.core;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.webengine.utils.URLStringUtils;


public class ParallelWebCrawler extends AbstractWebCrawler {

	private static ParallelWebCrawler instance;
	private ExecutorService executor = new ThreadPoolExecutor(0, 500,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());
	//Executors.newCachedThreadPool();
	private List<String> tested;
	private String dominio;
	private Map<String, String[]> map;
	
	private ParallelWebCrawler(String dominio,Map<String, String[]> map) {
		tested = new Vector<String>(); //must be Vector..synchronized
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
