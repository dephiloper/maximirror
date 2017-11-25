package overview;

import calendar.CalendarController;
import config.Config;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import timetable.TimeTableController;
import forecast.ForecastController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class OverviewController {

    @FXML
    public Parent calendarWidget;
    @FXML
    public Parent timeTableWidget;
    @FXML
    public Parent forecastWidget;
    @FXML
    private ForecastController forecastWidgetController;
    @FXML
    private CalendarController calendarWidgetController;
    @FXML
    private TimeTableController timeTableWidgetController;
    @FXML
    private Label time;
    @FXML
    private Label date;
    @FXML
    private Button button;

    private VisibilityDataHelper visibilityDataHelper = new VisibilityDataHelper();
    private Task<String> dateTimeTask;
    private boolean isRunning;

    void init(){
        isRunning = true;
        if (Config.instance.SHOW_TIME || Config.instance.SHOW_DATE) updateDateTime();
        if (Config.instance.SHOW_CALENDAR) calendarWidgetController.update();
        if (Config.instance.SHOW_TIMETABLE) timeTableWidgetController.update();
        if (Config.instance.SHOW_WEATHER) forecastWidgetController.update();

        createBindings();

        button.setOnAction(actionEvent -> {
            if (isRunning)
                stopRunning();
            else
                init();

            visibilityDataHelper.setShowTime(!visibilityDataHelper.isShowTime());
            visibilityDataHelper.setShowDate(!visibilityDataHelper.isShowDate());
            visibilityDataHelper.setShowCalendar(!visibilityDataHelper.isShowCalendar());
            visibilityDataHelper.setShowForecast(!visibilityDataHelper.isShowForecast());
            visibilityDataHelper.setShowWeather(!visibilityDataHelper.isShowWeather());
            visibilityDataHelper.setShowTimeTable(!visibilityDataHelper.isShowTimeTable());
        });
    }

    private void createBindings() {
        calendarWidget.visibleProperty().bind(visibilityDataHelper.showCalendarProperty());
        timeTableWidget.visibleProperty().bind(visibilityDataHelper.showTimeTableProperty());
        time.visibleProperty().bind(visibilityDataHelper.showTimeProperty());
        date.visibleProperty().bind(visibilityDataHelper.showDateProperty());
        forecastWidgetController.createBindings(visibilityDataHelper);
    }

    private void updateDateTime() {

        dateTimeTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
            while (isRunning) {
                if (Config.instance.SHOW_TIME)
                    updateValue(LocalTime.now().format(DateTimeFormatter.ofPattern(Config.instance.TIME_FORMAT)));
                if (Config.instance.SHOW_DATE)
                    updateTitle(LocalDate.now().format(DateTimeFormatter.ofPattern(Config.instance.DATE_FORMAT)));

                TimeUnit.MILLISECONDS.sleep((long) (Config.instance.CLOCK_SLEEP_SECONDS*1000));
            }

            return null;
            }
        };

        date.textProperty().bind(dateTimeTask.titleProperty());
        time.textProperty().bind(dateTimeTask.valueProperty());
        new Thread(dateTimeTask).start();
    }

    void stopRunning() {
        isRunning = false ;
        if (dateTimeTask.isRunning())
            dateTimeTask.cancel();

        forecastWidgetController.stopRunning();
        calendarWidgetController.stopRunning();
        timeTableWidgetController.stopRunning();
    }

}
