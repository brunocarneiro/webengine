package com.webengine.core;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;

import com.webengine.utils.URLStringUtils;

public class ParallelWebCrawler extends AbstractWebCrawler {

	private static ParallelWebCrawler instance;
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	private ParallelWebCrawler() {
	}
	
	public static ParallelWebCrawler getInstance(){
		if(instance==null)
			instance = new ParallelWebCrawler();
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
