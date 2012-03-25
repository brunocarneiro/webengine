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

import utils.URLStringUtils;

public class Engine {

	public static final Map<String, String[]> map = new HashMap<String, String[]>() {
		{
			put("src", new String[] { "http://", "https://" });
			put("href", new String[] { "http://", "https://" });
		}
	};

	public static String dominio;

	public static List<String> tested = new ArrayList<String>();
	
	public static final String COPY_DIR = "work/";

	public static void test(String url) {
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
								if (href.contains(dominio) && !tested.contains(url))
									test(href);

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

			new PrettyHtmlSerializer(props).writeToFile(dom,COPY_DIR+ dominio+ "/index.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String rewrite(String toReplace, String startsWith,
			String replaceBy) {
		// tem que ser configurado no mapa
		// if(toReplace.startsWith("/")){
		// }
		// else
		if (toReplace.startsWith(startsWith))
			toReplace = toReplace.substring(startsWith.length());
		else
			return toReplace;

		return replaceBy += toReplace;
	}

	private static void copyFile(String u) throws Exception {
		FileUtils.copyURLToFile(new URL(u), new File(COPY_DIR + URLStringUtils.getFullPathFile(u)));
	}
	
	public static void main(String[] args) {
		dominio = "anapaulalobato.com";
		test("http://anapaulalobato.com");
	}

}
