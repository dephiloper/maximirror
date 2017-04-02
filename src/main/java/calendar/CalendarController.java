package calendar;

import config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CalendarController {
    @FXML
    private ListView<String> listView;

    private List<String> list = new ArrayList<>();
    private boolean isRunning = true;
    private CalendarProvider calendarProvider = new CalendarProvider();
    private Task<ObservableList<String>> calendarTask = null;
    public void update() {
                calendarTask = new Task<ObservableList<String>>() {
            protected ObservableList<String> call() throws InterruptedException, IOException {
                while (isRunning) {
                    list.clear();
                    list = calendarProvider.getEvents();
                    updateValue(FXCollections.observableArrayList(list));
                    TimeUnit.HOURS.sleep((long) Config.instance.CALENDAR_SLEEP_SECONDS);
                }

                return null;
            }
        };

        listView.itemsProperty().bind(calendarTask.valueProperty());
        new Thread(calendarTask).start();
    }

    public void stopRunning() {
        if (calendarTask.isRunning())
            calendarTask.cancel();
    }
}
