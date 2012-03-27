package com.webengine.main;

import java.util.HashMap;

import com.webengine.core.ParallelWebCrawler;

public class Main {
	
	public static void main (String [] args){
		ParallelWebCrawler.getInstance("powerlogic.org", new HashMap<String, String[]>() {
			{
				put("src", new String[] { "http://www.powerlogic.org", "http://localhost:8080/slideshow/work/www.powerlogic.org" });
				put("href", new String[] { "http://www.powerlogic.org", "http://localhost:8080/slideshow/work/www.powerlogic.org" });
			}
		}).start("http://www.powerlogic.org");
	}

}
