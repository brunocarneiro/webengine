package com.webengine.main;

import com.webengine.core.ParallelWebCrawler;

public class Main {
	
	public static void main (String [] args){
		ParallelWebCrawler.getInstance().start("http://www.sydle.com/br/");
	}

}
