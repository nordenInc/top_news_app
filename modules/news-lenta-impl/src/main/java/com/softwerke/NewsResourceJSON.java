package com.softwerke;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.softwerke.api.NewsResource;
import com.softwerke.util.URLConnectionReader;
import org.osgi.service.component.annotations.Component;

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
public class NewsResourceJSON implements NewsResource {

	private static final String TYPE = "JSON";
	private static final String RESOURCE_NAME = "lenta.ru";
	private static final String RESOURCE_URL = "https://api.lenta.ru/lists/latest";

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

	private List<String> parse(String input) {
		List<String> titles = new ArrayList<>();
		JsonParser parser = new JsonParser();
		JsonArray headlines = parser.parse(input)
				.getAsJsonObject()
				.getAsJsonArray("headlines");

		for (JsonElement post : headlines) {

			String postType = post.getAsJsonObject().get("type").getAsString();

			if (postType.equals("news")) {
				String title = post.getAsJsonObject().getAsJsonObject("info").get("title").getAsString();
				titles.add(title);
			}
		}
		return titles;
	}

}