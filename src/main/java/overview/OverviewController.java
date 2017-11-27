package overview;

import calendar.CalendarController;
import config.Config;
import interfaces.Controller;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import news.NewsController;
import timetable.TimeTableController;
import forecast.ForecastController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

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
    private Task<String> dateTimeTask;
    private boolean isRunning;

    @Override
    public void init(){
        isRunning = true;
        startUpdate();

        forecastWidgetController.startUpdate();
        calendarWidgetController.startUpdate();
        timeTableWidgetController.startUpdate();
        newsWidgetController.startUpdate();
        createBindings();
    }

    @Override
    public void startUpdate() {
        if (!(Config.instance.SHOW_TIME || Config.instance.SHOW_DATE))
            return;

        dateTimeTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
            while (isRunning) {
                if (Config.instance.SHOW_TIME)
                    updateValue(LocalTime.now().format(DateTimeFormatter.ofPattern(Config.instance.TIME_FORMAT)));
                if (Config.instance.SHOW_DATE)
                    updateTitle(LocalDate.now().format(DateTimeFormatter.ofPattern(Config.instance.DATE_FORMAT)));

                TimeUnit.SECONDS.sleep((long) (Config.instance.CLOCK_SLEEP_SECONDS));
            }

            return LocalTime.now().format(DateTimeFormatter.ofPattern(Config.instance.TIME_FORMAT));
            }
        };

        new Thread(dateTimeTask).start();
    }

    @Override
    public void createBindings() {
        forecastWidget.visibleProperty().bind(visibilityDataHelper.showForecastProperty());
        calendarWidget.visibleProperty().bind(visibilityDataHelper.showCalendarProperty());
        timeTableWidget.visibleProperty().bind(visibilityDataHelper.showTimeTableProperty());
        time.visibleProperty().bind(visibilityDataHelper.showTimeProperty());
        date.visibleProperty().bind(visibilityDataHelper.showDateProperty());
        date.textProperty().bind(dateTimeTask.titleProperty());
        time.textProperty().bind(dateTimeTask.valueProperty());
    }

    @Override
    public void stopRunning() {
        isRunning = false ;
        if (dateTimeTask != null)
            if (dateTimeTask.isRunning())
                dateTimeTask.cancel();

        forecastWidgetController.stopRunning();
        calendarWidgetController.stopRunning();
        timeTableWidgetController.stopRunning();
    }

}
