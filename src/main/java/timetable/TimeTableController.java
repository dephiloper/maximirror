package timetable;

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
 * Created by phil on 23.03.17.
 */
public class TimeTableController {
    @FXML
    ListView listView;

    private List<String> list = new ArrayList<>();
    private boolean isRunning = true;
    private TrainHelper trainHelper = new TrainHelper();
    private Task task = null;

    public void update() {
        task = new Task() {
            protected ObservableList<String> call() throws InterruptedException, IOException {
                while (isRunning) {
                    list.clear();
                    list = trainHelper.getTrains(8);
                    //noinspection unchecked
                    updateValue(FXCollections.observableArrayList(list));
                    TimeUnit.SECONDS.sleep(5);
                }

                return null;
            }
        };

        //noinspection unchecked
        listView.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    public void stopRunning() {
        if (task != null)
            task.cancel();
    }
}
