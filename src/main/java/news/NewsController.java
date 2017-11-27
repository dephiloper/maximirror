package news;

import config.Config;
import interfaces.Controller;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class NewsController implements Controller {
    @FXML
    public Label news;

    private NewsProvider provider = new NewsProvider();
    private ScheduledService<NewsDataHelper> newsService;
    private final NewsDataHelper newsDataHelper = new NewsDataHelper();

    @Override
    public void init() {
        createBindings();
    }

    @Override
    public void startUpdate() {
        if (!Config.instance.SHOW_TIMETABLE) return;

        newsService = new ScheduledService<NewsDataHelper>() {
            @Override
            protected Task<NewsDataHelper> createTask() {
                return new Task<NewsDataHelper>() {
                    @Override
                    protected NewsDataHelper call() throws Exception {
                        News news = provider.provideData();
                        NewsDataHelper newsDataHelper = null;

                        if (news != null) {
                            newsDataHelper = new NewsDataHelper(
                                    news.getSource(),
                                    news.getTitle(),
                                    news.getDescription());
                        }
                        if (newsDataHelper != null) {
                            updateValue(newsDataHelper);
                        }
                        return newsDataHelper;
                    }
                };
            }
        };
        init();
        newsService.setPeriod(Duration.seconds(Config.instance.TIMETABLE_SLEEP_SECONDS));
        newsService.start();

        newsService.setOnSucceeded(event -> newsDataHelper.reinitialize(
                newsService.getValue().getSource(),
                newsService.getValue().getTitle(),
                newsService.getValue().getDescription()
        ));
    }

    @Override
    public void stopRunning() {

    }

    @Override
    public void createBindings() {
        news.textProperty().bind(newsDataHelper.descriptionProperty());
    }
}
