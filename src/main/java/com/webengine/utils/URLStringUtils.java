package com.webengine.utils;


public class URLStringUtils {
	
	
	public static String getLocalPath(String u){
		String [] array = u.split("/");
		return u.replace(array[array.length-1], "").replace("http://", "");
	}
	
	public static String getLocalPathFile(String u){
		String [] array = u.split("/");
		return array[array.length-1];
	}
	
	public static String getFullPathFile(String u){
		String [] array = u.split("/");
		//pagina inicial
		if(array.length==3){
			return array[2];
		}
		return u.replace(array[array.length-1], "").replace("http://", "")+array[array.length-1];
	}

}
