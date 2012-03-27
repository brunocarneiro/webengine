package com.webengine.core;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;

import com.webengine.utils.URLStringUtils;

public abstract class AbstractWebCrawler {
	
	public final String COPY_DIR = "/home/bruno/Java/Powerlogic/Jaguar601/servers/tomcat/webapps/slideshow/work/";
	
	public void read(String url) {
		try {
			System.out.println(url);
			CleanerProperties props = new CleanerProperties();
			props.setTranslateSpecialEntities(true);
			props.setTransResCharsToNCR(true);
			props.setOmitComments(true);
			final HtmlCleaner cleaner = new HtmlCleaner(props);

			TagNode dom = cleaner.clean(new URL(url));
			tested(url);
			for (String attr : getMap().keySet()) {
				Object[] tags = dom.evaluateXPath("//*[@" + attr + "]");
				for (Object o : tags) {
					try {
						if (o instanceof TagNode) {
							TagNode tag = (TagNode) o;
							if (tag.getName().equals("a")) {
								String href = tag.getAttributeByName("href");
								if (href.startsWith("/") && href.length() == 1)
									continue;
								if (href.startsWith("/"))
									href = url + href;
								else if(!href.startsWith("http://")){
									href = url +"/"+ href;
								}
								if (href.contains(getDominio()) && !wasTested(href))
									deepRead(href);

							}
							else {
								String src = tag.getAttributeByName("src");
								if(src==null || src.equals("")){
									//link stylesheet
									src = tag.getAttributeByName("href");
								}
								if(src !=null && src.contains(getDominio())){
									if (src.startsWith("/") && src.length() == 1)
										continue;
									if (src.startsWith("/"))
										src = url + src;
									copyFile(src);
								}
							}
							tag.getAttributes().put(attr, rewrite(tag.getAttributes().get(attr), getMap().get(attr)[0],getMap().get(attr)[1]));
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
			new File(COPY_DIR+ URLStringUtils.getFullPathFile(url)).mkdirs();
			new PrettyHtmlSerializer(props).writeToFile(dom,COPY_DIR+ URLStringUtils.getFullPathFile(url)+ "/index.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	protected static String rewrite(String toReplace, String startsWith,
			String replaceBy) {
		// tem que ser configurado no mapa
		if(toReplace.startsWith("/")){
		}
		else
		if (toReplace.startsWith(startsWith))
			toReplace = toReplace.substring(startsWith.length());
		else
			return toReplace;

		return replaceBy += toReplace;
	}
	
	protected abstract void deepRead(String href) throws Exception;
	
	protected abstract void copyFile(String url) throws Exception;
	
	protected abstract void tested(String url) throws Exception;
	
	protected abstract boolean wasTested(String url) throws Exception;
	
	protected abstract String getDominio() throws Exception;
	
	protected abstract  Map<String, String[]> getMap() throws Exception;
}
