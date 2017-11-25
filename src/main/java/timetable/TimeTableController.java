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
    private final StationProvider stationProvider = new StationProvider();
    private final StationDataHelper stationDataHelper = new StationDataHelper();
    private ScheduledService<StationDataHelper> timeTableService;

    public void update() {
        timeTableService = new ScheduledService<StationDataHelper>() {
            @Override
            protected Task<StationDataHelper> createTask() {
                return new Task<StationDataHelper>() {
                    @Override
                    protected StationDataHelper call() throws Exception {
                        Station station = stationProvider.fetchStation();
                        StationDataHelper stationDataHelper = new StationDataHelper(
                                FXCollections.observableArrayList(station.getTransports()),
                                station.getStationName());

                        updateValue(stationDataHelper);

                        return stationDataHelper;
                    }
                };
            }
        };
        timeTableService.setPeriod(Duration.seconds(Config.instance.TIMETABLE_SLEEP_SECONDS));
        timeTableService.start();

        timeTableService.setOnSucceeded(event -> stationDataHelper.reinitialize(
            timeTableService.getValue().getTransports(),
            timeTableService.getValue().getStationName()
        ));

        stationName.textProperty().bind(stationDataHelper.stationNameProperty());
        transportsListView.itemsProperty().bind(stationDataHelper.transportsProperty());
    }

    public void stopRunning() {
        if (timeTableService.isRunning())
            timeTableService.cancel();

    }
}
