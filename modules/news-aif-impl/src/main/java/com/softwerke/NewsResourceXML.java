package com.softwerke;

import com.softwerke.api.NewsResource;
import com.softwerke.util.URLConnectionReader;
import org.osgi.service.component.annotations.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for {@code NewsResource}. Uses RESOURCE_URL as default param to get News titles and also have
 * overloaded method to get titles for client's resource.
 */

@Component(
	immediate = true,
	service = NewsResource.class
)
public class NewsResourceXML implements NewsResource {
	private static final String TYPE = "XML";
	private static final String RESOURCE_NAME = "aif.ru";
	private static final String RESOURCE_URL = "http://www.aif.ru/rss/news.php";

	@Override
	public String getName() {
		return RESOURCE_NAME;
	}

	@Override
	public  String getType() {
		return TYPE;
	}

	@Override
	public String[] getNewsTitles() throws Exception {

		String input = URLConnectionReader.getInfo(RESOURCE_URL);
		return parse(input).toArray(new String[0]);
	}

	@Override
	public String[] getNewsTitles(String clientUrl) throws Exception {

		String input = URLConnectionReader.getInfo(clientUrl);
		return parse(input).toArray(new String[0]);
	}

	private List<String> parse(String input) throws Exception {

		List<String> titles = new ArrayList<>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(new InputSource(new StringReader(input)));
		document.getDocumentElement().normalize();

		NodeList posts = document.getElementsByTagName("item");
		for (int i = 0; i < posts.getLength(); i++) {
			Element post = (Element) posts.item(i);
			String title = post.getElementsByTagName("title").item(0).getTextContent();
			titles.add(title);
		}
		return titles;
	}
}