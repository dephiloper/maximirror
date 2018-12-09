package timetable;

import config.Config;
import interfaces.Controller;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.util.Duration;
import overview.AssistantMirror;

public class TimeTableController implements Controller {
    @FXML
    public Label stationName;
    @FXML
    public Label stationNames;
    @FXML
    private ListView<String> transportsListView;

    private final StationProvider stationProvider = new StationProvider();
    private final StationDataHelper stationDataHelper = new StationDataHelper();
    private ScheduledService<StationDataHelper> timeTableService;

    @Override
    public void init() {
        createBindings();
        setCustomFont();
    }

    private void setCustomFont() {
        stationNames.setFont(new Font(AssistantMirror.FONT_NAME, 30));
        stationName.setFont(new Font(AssistantMirror.FONT_NAME, 25));
        transportsListView.setCellFactory(cell -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setFont(Font.font(AssistantMirror.FONT_NAME, 25));
                    setPrefWidth(0);
                }
            }
        });
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

    public void

    stopRunning() {
        if (timeTableService != null)
            if (timeTableService.isRunning())
                timeTableService.cancel();
    }

    @Override
    public void createBindings() {
        stationName.textProperty().bind(stationDataHelper.stationNameProperty());
        transportsListView.itemsProperty().bind(stationDataHelper.transportsProperty());
        stationDataHelper.reinitialize(stationProvider.getPlaceholderDataHelper().getTransports(),
                stationProvider.getPlaceholderDataHelper().getStationName());
    }
}
