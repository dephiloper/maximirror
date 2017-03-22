package overview;

import calendar.CalendarController;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import weather.WeatherController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class OverviewController {
    @FXML
    WeatherController weatherWidgetController;
    @FXML
    CalendarController calendarWidgetController;
    @FXML
    Parent weatherWidget;
    @FXML
    Parent calendarWidget;
    @FXML
    Label clock;

    private boolean isRunning = true;

    public void init(){
        updateClock();
        weatherWidgetController.update();
        calendarWidgetController.update();
    }

    private void updateClock() {

        Task task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                while (isRunning) { //loop statusabfrage bis programmende
                    updateValue(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))); //aktualisiert Zeit und sagt jedem abbonenten bescheid
                    TimeUnit.MILLISECONDS.sleep(500);
                }

                return null;
            }
        };

        clock.textProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    public void stopRunning() {
        isRunning = false;
        weatherWidgetController.stopRunning();
        calendarWidgetController.stopRunning();
    }

}
