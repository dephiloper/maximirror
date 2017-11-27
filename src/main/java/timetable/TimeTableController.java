package timetable;

import config.Config;
import interfaces.Controller;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Duration;

public class TimeTableController implements Controller {
    @FXML
    public Label stationName;
    @FXML
    private ListView<String> transportsListView;

    private final StationProvider stationProvider = new StationProvider();
    private final StationDataHelper stationDataHelper = new StationDataHelper();
    private ScheduledService<StationDataHelper> timeTableService;

    @Override
    public void init() {
        createBindings();
    }
    @Override
    public void startUpdate() {
        if (!Config.instance.SHOW_TIMETABLE) return;

        timeTableService = new ScheduledService<StationDataHelper>() {
            @Override
            protected Task<StationDataHelper> createTask() {
                return new Task<StationDataHelper>() {
                    @Override
                    protected StationDataHelper call() throws Exception {
                        Station station = stationProvider.provideData();
                        StationDataHelper stationDataHelper = null;

                        if (station != null) {
                            stationDataHelper = new StationDataHelper(FXCollections.observableArrayList(
                                    station.getTransports()),
                                    station.getStationName());
                        } else {
                            stationDataHelper = stationProvider.getPlaceholderDataHelper();
                        }
                        updateValue(stationDataHelper);

                        return stationDataHelper;
                    }
                };
            }
        };
        init();
        timeTableService.setPeriod(Duration.seconds(Config.instance.TIMETABLE_SLEEP_SECONDS));
        timeTableService.start();

        timeTableService.setOnSucceeded(event -> stationDataHelper.reinitialize(
            timeTableService.getValue().getTransports(),
            timeTableService.getValue().getStationName()
        ));
    }

    public void stopRunning() {
        if (timeTableService != null)
            if (timeTableService.isRunning())
                timeTableService.cancel();
        stationProvider.setRunning(false);
    }

    @Override
    public void createBindings() {
        stationName.textProperty().bind(stationDataHelper.stationNameProperty());
        transportsListView.itemsProperty().bind(stationDataHelper.transportsProperty());
    }
}
