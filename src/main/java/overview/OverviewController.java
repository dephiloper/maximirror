package overview;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class OverviewController {

    @FXML
    Label clock;

    private boolean isRunning = true;


    public void updateClock() {
        Task task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                while (isRunning) {
                    updateValue(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                    Thread.sleep(10000);
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
