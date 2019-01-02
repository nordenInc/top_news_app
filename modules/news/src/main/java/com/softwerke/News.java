package com.softwerke;

import com.softwerke.api.NewsResource;
import com.softwerke.util.URLConnectionReader;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nrv
 */

/**
 * Main class that provides a list of available news sources by command {news:stats} where you can choose one or all
 * sources and get top ten news titles for them. Command {news:stats <param>} where param is your own web resource, can
 * be also used to get top news.
 */

@Component(
        immediate = true,
        property = {"osgi.command.function=stats", "osgi.command.scope=news"},
        service = News.class
)
public class News {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            bind = "bindNewsResource",
            unbind = "unbindNewsResource"
    )
    private List<NewsResource> newsResources = new ArrayList<>();

    private static final int NUMBER_OF_WORDS_TO_DISPLAY = 10;

    /**
     * Print top news titles {NUMBER_OF_WORDS_TO_DISPLAY} for given array of strings.
     *
     * @param titles provided by NewsResource impl
     */
    private void printWordFrequency(String[] titles) {
        Map<String,Integer> wordPosition = new HashMap<>();

        for (String title: titles) {
            String[] words = title.replaceAll("([^a-zA-Zа-яА-Я]{2,}|\\s)", " ")
                    .replaceAll("«", "").toLowerCase().split("\\s");

            for (String word: words) {
                wordPosition.put(word, wordPosition.getOrDefault(word,0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordPosition.entrySet());
        sortedWords.sort((o1, o2) -> (o2.getValue().compareTo(o1.getValue())));

        int wordsToDisplay = NUMBER_OF_WORDS_TO_DISPLAY;
        if (NUMBER_OF_WORDS_TO_DISPLAY > sortedWords.size()) {
            wordsToDisplay = sortedWords.size();
        }
        for (int i = 0; i < wordsToDisplay; i++) {
            System.out.println(i + 1 + ". Tile: " + sortedWords.get(i).getKey() + ", number of doubling: " + sortedWords.get(i).getValue());
        }

    }

    /**
     * Prints all available news resources, where you can choose one to or all to display to news titles.
     */
    public void stats() {
        if (!this.newsResources.isEmpty()) {
            System.out.println("Choose resource to get top news.");
            HashMap<Integer, NewsResource> newsResourceHashMap = new HashMap<>();
            int i = 1;
            for (NewsResource resource: this.newsResources) {
                System.out.println(i + ". " + resource.getName());
                newsResourceHashMap.put(i,resource);
                ++i;
            }
            System.out.println(i + ". Get from all.");

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String newsChoice = br.readLine();
                switch (newsChoice){
                    case "1":
                        printWordFrequency(newsResourceHashMap.get(1).getNewsTitles());
                        break;
                    case "2":
                        printWordFrequency(newsResourceHashMap.get(2).getNewsTitles());
                        break;
                    case "3":
                        printWordFrequency(newsResourceHashMap.get(1).getNewsTitles());
                        printWordFrequency(newsResourceHashMap.get(2).getNewsTitles());
                        break;
                }

            } catch (Exception e) {
                System.out.println("Resource wasn't found, error message: " + e.getMessage());
            }

        } else {
            System.out.println("Today news are not available.");
        }
    }

    /**
     * Prints top news titles for client's news resource.
     * @param clientSource
     */
    public void stats(String clientSource) {
        try {
            if (URLConnectionReader.getRequestType(clientSource)) {
                for (NewsResource resource: this.newsResources) {
                    if (resource.getType().equals("JSON")) {
                        printWordFrequency(resource.getNewsTitles(clientSource));
                    }
                }
            } else {
                for (NewsResource resource: this.newsResources) {
                    if (resource.getType().equals("XML")) {
                        printWordFrequency(resource.getNewsTitles(clientSource));
                    }
                }
            }
        } catch (Exception e ) {
            System.out.println("Wrong type of input URL " + e.getMessage());
        }
    }

    /**
     *Bind {NewsResource}
     */
    public void bindNewsResource(NewsResource newsResource) {
        newsResources.add(newsResource);
    }

    /**
     *Unbind {NewsResource}
     */
    public void unbindNewsResource(NewsResource newsResource) { newsResources.remove(newsResource);
    }

}