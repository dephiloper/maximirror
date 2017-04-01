package overview;

import calendar.CalendarController;
import config.Config;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import timetable.TimeTableController;
import weather.WeatherController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class OverviewController {

    @FXML
    public Parent calendarWidget;
    @FXML
    public Parent timeTableWidget;
    @FXML
    public Parent weatherWidget;
    @FXML
    private WeatherController weatherWidgetController;
    @FXML
    private CalendarController calendarWidgetController;
    @FXML
    private TimeTableController timeTableWidgetController;
    @FXML
    private Label clock;
    @FXML
    private Button button;

    private boolean isRunning = true;

    void init(){
        if (Config.instance.SHOW_CLOCK)
            updateClock();

        if (Config.instance.SHOW_CALENDAR)
    	    calendarWidgetController.update();

        if (Config.instance.SHOW_TIMETABLE)
	        timeTableWidgetController.update();

        if (Config.instance.SHOW_WEATHER)
            weatherWidgetController.update();
        if (Config.instance.SHOW_FORECAST)
	        weatherWidgetController.updateForecast();

        createBindings();

        button.setOnAction(actionEvent -> System.exit(0));
    }

    private void createBindings() {
        calendarWidget.visibleProperty().bind(new SimpleBooleanProperty(Config.instance.SHOW_CALENDAR));
        timeTableWidget.visibleProperty().bind(new SimpleBooleanProperty(Config.instance.SHOW_TIMETABLE));
        clock.visibleProperty().bind(new SimpleBooleanProperty(Config.instance.SHOW_CLOCK));
        weatherWidgetController.createBindings();
    }

    private void updateClock() {

        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                while (isRunning) { //loop statusabfrage bis programmende
                    updateValue(LocalTime.now().format(DateTimeFormatter.ofPattern(Config.instance.CLOCK_FORMAT))); //aktualisiert Zeit und sagt jedem abbonenten bescheid
                    TimeUnit.MILLISECONDS.sleep((long) (Config.instance.CLOCK_SLEEP_SECONDS*1000));
                }

                return null;
            }
        };

        clock.textProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    void stopRunning() {
        isRunning = false;
        weatherWidgetController.stopRunning();
        calendarWidgetController.stopRunning();
        timeTableWidgetController.stopRunning();
    }

}
