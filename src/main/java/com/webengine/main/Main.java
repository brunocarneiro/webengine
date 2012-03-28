package com.webengine.main;

import java.util.HashMap;

import com.webengine.core.ParallelWebCrawler;

public class Main {
	
	public static void main (String [] args){
		ParallelWebCrawler.getInstance("sambatech.com.br", new HashMap<String, String[]>() {
			{
				put("src", new String[] { "http://www.sambatech.com", "http://localhost:8080/slideshow/work/www.sambatech.com" });
				put("href", new String[] { "http://www.sambatech.com", "http://localhost:8080/slideshow/work/www.sambatech.com" });
			}
		}).start("http://www.sambatech.com.br/");
	}

}
