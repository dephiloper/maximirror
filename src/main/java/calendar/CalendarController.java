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
import java.util.concurrent.TimeUnit;

public class CalendarController implements Controller {
    @FXML
    private ListView<String> listView = new ListView<>();

    private List<String> list = new ArrayList<>();
    private boolean isRunning = true;
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

        calendarTask = new Task<ObservableList<String>>() {
            protected ObservableList<String> call() throws InterruptedException, IOException {
                while (isRunning) {
                    list.clear();
                    list = calendarProvider.getEvents();
                    updateValue(FXCollections.observableArrayList(list));
                    TimeUnit.HOURS.sleep((long) Config.instance.CALENDAR_SLEEP_SECONDS);
                }

                return FXCollections.observableArrayList(list);
            }
        };

        init();
        new Thread(calendarTask).start();
    }

    @Override
    public void stopRunning() {
        if (calendarTask != null)
            if (calendarTask.isRunning())
                calendarTask.cancel();
    }

    @Override
    public void createBindings() {
        listView.itemsProperty().bind(calendarTask.valueProperty());
    }
}
