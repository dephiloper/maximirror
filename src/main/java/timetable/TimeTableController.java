package timetable;

import config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import java.time.LocalDateTime;

public class TimeTableController {
    @FXML
    public Label stationName;
    @FXML
    private ListView<String> transportsListView;
    private boolean isRunning = true;
    private final StationHelper stationHelper = new StationHelper();
    private final StationDataHelper stationDataHelper = new StationDataHelper();
    private Task<ObservableList<String>> task = null;

    public void update() {
        ScheduledService<StationDataHelper> service = new ScheduledService<StationDataHelper>() {
            @Override
            protected Task<StationDataHelper> createTask() {

                System.out.println(LocalDateTime.now());
                return new Task<StationDataHelper>() {
                    @Override
                    protected StationDataHelper call() throws Exception {
                        Station station = stationHelper.fetchStation();
                        StationDataHelper stationDataHelper = new StationDataHelper(
                                FXCollections.observableArrayList(station.getTransports()),
                                station.getStationName()
                                );

                        updateValue(stationDataHelper);

                        return stationDataHelper;
                    }
                };
            }
        };
        service.setPeriod(Duration.seconds(Config.instance.TIMETABLE_SLEEP_SECONDS));
        service.start();

        service.setOnSucceeded(event -> stationDataHelper.reinitialize(
            service.getValue().getTransports(),
            service.getValue().getStationName()
        ));

        stationName.textProperty().bind(stationDataHelper.stationNameProperty());
        transportsListView.itemsProperty().bind(stationDataHelper.transportsProperty());
    }

    public void stopRunning() {
        if (task != null)
            task.cancel();
    }
}
