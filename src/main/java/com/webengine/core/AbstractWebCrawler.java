package com.webengine.core;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;

import com.webengine.utils.URLStringUtils;

public abstract class AbstractWebCrawler {
	
	public String dominio = "sydle.com";

	public List<String> tested = new ArrayList<String>();
	
	public final String COPY_DIR = "work/";
	
	public static final Map<String, String[]> map = new HashMap<String, String[]>() {
		{
			put("src", new String[] { "http://www.sydle.com/br/", "/home/bruno/Java/Powerlogic/Jaguar601/workspace/webengine/work/www.sydle.com/br/" });
			put("href", new String[] { "http://www.sydle.com/br/", "/home/bruno/Java/Powerlogic/Jaguar601/workspace/webengine/work/www.sydle.com/br/" });
		}
	};
	
	public void read(String url) {
		try {
			System.out.println(url);
			CleanerProperties props = new CleanerProperties();
			props.setTranslateSpecialEntities(true);
			props.setTransResCharsToNCR(true);
			props.setOmitComments(true);
			final HtmlCleaner cleaner = new HtmlCleaner(props);

			TagNode dom = cleaner.clean(new URL(url));
			tested.add(url);
			for (String attr : map.keySet()) {
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
								if (href.contains(dominio) && !tested.contains(href))
									deepRead(href);

							} else {
								String src = tag.getAttributeByName("src");
								if(src !=null && src.contains(dominio)){
									if (src.startsWith("/") && src.length() == 1)
										continue;
									if (src.startsWith("/"))
										src = url + src;
									copyFile(src);
								}
							}
							tag.getAttributes().put(attr, rewrite(tag.getAttributes().get(attr), map.get(attr)[0],map.get(attr)[1]));
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
			FileUtils.forceMkdir(new File(COPY_DIR+ URLStringUtils.getFullPathFile(url)));
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
	
	
}
