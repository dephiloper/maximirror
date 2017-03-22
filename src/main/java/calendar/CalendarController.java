package calendar;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by phil on 21.03.17.
 */
public class CalendarController {
    @FXML
    ListView listView;

    private List<String> list = new ArrayList<>();
    private boolean isRunning = true;
    private Quickstart quickstart = new Quickstart();

    public void update() {
        final Task usersListTask = new Task() {
            protected ObservableList<String> call() throws InterruptedException, IOException {
                while (isRunning) {
                    list.clear();
                    list = quickstart.getEvents();
                    updateValue(FXCollections.observableArrayList(list));
                    Thread.sleep(10000);
                }

                return null;
            }
        };

        listView.itemsProperty().bind(usersListTask.valueProperty());

        new Thread(usersListTask).start();
    }

    public void stopRunning() {
        isRunning = false;
    }
}
