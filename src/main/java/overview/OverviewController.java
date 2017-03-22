package overview;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import weather.WeatherController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class OverviewController {
    @FXML
    WeatherController weatherWidgetController;
    @FXML
    Parent weatherWidget;
    @FXML
    Label clock;

    private boolean isRunning = true;

    public void init(){
        updateClock();
        weatherWidgetController.updateWeather();
        weatherWidgetController.updateForecast();
    }

    private void updateClock() {
        Task task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                while (isRunning) { //loop statusabfrage bis programmende
                    updateValue(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))); //aktualisiert Zeit und sagt jedem abbonenten bescheid
                    Thread.sleep(5000);
                }

                return null;
            }
        };

        clock.textProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    public void stopRunning() {
        isRunning = false;
    }

}
