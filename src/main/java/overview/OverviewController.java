package overview;

import calendar.CalendarController;
import config.Config;
import interfaces.Controller;
import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Duration;
import news.NewsController;
import timetable.TimeTableController;
import forecast.ForecastController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OverviewController implements Controller {

    @FXML
    public Parent calendarWidget;
    @FXML
    public Parent timeTableWidget;
    @FXML
    public Parent forecastWidget;
    @FXML
    public Parent newsWidget;
    @FXML
    private ForecastController forecastWidgetController;
    @FXML
    private CalendarController calendarWidgetController;
    @FXML
    private TimeTableController timeTableWidgetController;
    @FXML
    private NewsController newsWidgetController;
    @FXML
    private Label time;
    @FXML
    private Label date;

    private VisibilityDataHelper visibilityDataHelper = new VisibilityDataHelper();
    private StringProperty currentTime = new SimpleStringProperty();
    private StringProperty currentDate = new SimpleStringProperty();
    private Timeline timeline;
    private List<FadeTransition> transitions = new ArrayList<>();

    public OverviewController() {
    }

    @Override
    public void init(){
        startUpdate();
        forecastWidgetController.startUpdate();
        calendarWidgetController.startUpdate();
        timeTableWidgetController.startUpdate();
        newsWidgetController.startUpdate();
        createBindings();
        applyTransitions();
    }

    private void applyTransitions() {
        transitions.add(new FadeTransition(Duration.seconds(3), calendarWidget));
        transitions.add(new FadeTransition(Duration.seconds(3), timeTableWidget));
        transitions.add(new FadeTransition(Duration.seconds(3), forecastWidget));
        transitions.add(new FadeTransition(Duration.seconds(3), newsWidget));
        transitions.add(new FadeTransition(Duration.seconds(3), time));
        transitions.add(new FadeTransition(Duration.seconds(3), date));

        for (FadeTransition transition : transitions) {
            transition.setFromValue(0.0);
            transition.setToValue(1.0);
            transition.setCycleCount(1);
            transition.setAutoReverse(false);
            transition.playFromStart();
        }

        transitions.clear();
    }

    @Override
    public void startUpdate() {
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(Config.instance.CLOCK_SLEEP_SECONDS),
                        ae -> {
                            currentDate.setValue(LocalDate.now().format(DateTimeFormatter.ofPattern(Config.instance.DATE_FORMAT)));
                            currentTime.setValue(LocalTime.now().format(DateTimeFormatter.ofPattern(Config.instance.TIME_FORMAT)));
                        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }

    @Override
    public void createBindings() {
        forecastWidget.visibleProperty().bind(visibilityDataHelper.showForecastProperty());
        calendarWidget.visibleProperty().bind(visibilityDataHelper.showCalendarProperty());
        timeTableWidget.visibleProperty().bind(visibilityDataHelper.showTimeTableProperty());
        newsWidget.visibleProperty().bind(visibilityDataHelper.showTimeTableProperty());
        time.visibleProperty().bind(visibilityDataHelper.showTimeProperty());
        date.visibleProperty().bind(visibilityDataHelper.showDateProperty());
        time.textProperty().bind(currentTime);
        date.textProperty().bind(currentDate);
    }

    @Override
    public void stopRunning() {
        if (timeline != null)
          timeline.stop();
        forecastWidgetController.stopRunning();
        calendarWidgetController.stopRunning();
        timeTableWidgetController.stopRunning();
        newsWidgetController.stopRunning();
    }

}
