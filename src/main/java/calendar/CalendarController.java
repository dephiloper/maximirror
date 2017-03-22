package calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
                    TimeUnit.HOURS.sleep(1);
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
