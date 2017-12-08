package calendar;

import config.Config;
import interfaces.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CalendarController implements Controller {
    @FXML
    private ListView<String> listView = new ListView<>();

    private List<String> list = new ArrayList<>();
    private CalendarProvider calendarProvider = new CalendarProvider();
    private Task<ObservableList<String>> calendarTask;

    @Override
    public void init() {
        createBindings();
    }

    @Override
    public void startUpdate() {
        if (!Config.instance.SHOW_CALENDAR)
            return;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        calendarTask = new Task<ObservableList<String>>() {
            protected ObservableList<String> call() throws InterruptedException, IOException {
                    list.clear();
                    list = calendarProvider.getEvents();
                    updateValue(FXCollections.observableArrayList(list));
                return FXCollections.observableArrayList(list);
            }
        };

        executor.scheduleAtFixedRate(calendarTask, 0, (long) Config.instance.CALENDAR_SLEEP_SECONDS, TimeUnit.HOURS);

        init();
    }

    @Override
    public void stopRunning() {
        if (calendarTask != null) {
            if (calendarTask.isRunning()) {
                calendarTask.cancel();
            }

        }
    }

    @Override
    public void createBindings() {
        listView.itemsProperty().bind(calendarTask.valueProperty());
    }
}
