package news;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import config.Config;
import interfaces.Provider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NewsProvider implements Provider<News> {
    private List<News> newsList;
    private int providedNewsIndex = 0;

    NewsProvider() {
        newsList = Collections.synchronizedList(new ArrayList<News>());
        fetchNewsCyclical();
    }

    private void fetchNewsCyclical() {
        final int fetchingPeriod = 30;
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(() -> {
            for (String source : Config.instance.NEWS_SOURCES) {
                newsList.addAll(fetchFromNewsSource(String.format("https://newsapi.org/v2/top-headlines?sources=%s&apiKey=%s", source, Config.instance.NEWS_API_KEY)));
            }
            providedNewsIndex = 0;
        }, 0, fetchingPeriod, TimeUnit.MINUTES);
    }

    private List<News> fetchFromNewsSource(String url) {
        HttpResponse<JsonNode> node = null;
        try {
            node = Unirest.get(url).asJson();
        } catch (UnirestException e) {
            System.err.println(e.getMessage());
        }
        JSONArray jsonArray = null;

        if (node != null) {
            jsonArray = (JSONArray) node.getBody().getObject().get("articles");
        }

        List<News> newsListSource = new ArrayList<>();
        if (jsonArray != null) {
            for (Object object : jsonArray)
            {
                JSONObject jsonObject = (JSONObject)object;
                if (jsonObject == null)
                    continue;

                String name = "", title = "", description = "";

                if (jsonObject.get("source") != null)
                    name = (String) ((JSONObject) jsonObject.get("source")).get("name");
                if (jsonObject.get("title") != null)
                    title = (String) jsonObject.get("title");
                if (jsonObject.get("description") != null)
                    description = (String) jsonObject.get("description");

                newsListSource.add(new News(name, title, description));

            };
        }
        Collections.shuffle(newsListSource);
        return newsListSource;
    }

    @Override
    public synchronized News provideData() {
        News news = newsList.get(providedNewsIndex);
        providedNewsIndex = (providedNewsIndex + 1) % newsList.size();
        return news;
    }

    NewsDataHelper getPlaceholderDataHelper() {
        return new NewsDataHelper("Awesome News", "Awesome Title", "...");
    }
}
