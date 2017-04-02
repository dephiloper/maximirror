package overview;

import config.Config;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class VisibilityDataHelper {
    private BooleanProperty showTime = new SimpleBooleanProperty(Config.instance.SHOW_TIME);
    private BooleanProperty showDate = new SimpleBooleanProperty(Config.instance.SHOW_DATE);
    private BooleanProperty showCalendar = new SimpleBooleanProperty(Config.instance.SHOW_CALENDAR);
    private BooleanProperty showTimeTable = new SimpleBooleanProperty(Config.instance.SHOW_TIMETABLE);
    private BooleanProperty showWeather = new SimpleBooleanProperty(Config.instance.SHOW_WEATHER);
    private BooleanProperty showForecast = new SimpleBooleanProperty(Config.instance.SHOW_FORECAST);

    BooleanProperty showTimeProperty() {
        return showTime;
    }

    void setShowTime(boolean showTime) {
        this.showTime.set(showTime);
    }

    boolean isShowTime() {
        return showTime.get();
    }

    boolean isShowDate() {
        return showDate.get();
    }

    boolean isShowCalendar() {
        return showCalendar.get();
    }

    boolean isShowTimeTable() {
        return showTimeTable.get();
    }

    boolean isShowWeather() {
        return showWeather.get();
    }

    boolean isShowForecast() {
        return showForecast.get();
    }

    BooleanProperty showCalendarProperty() {
        return showCalendar;
    }

    void setShowCalendar(boolean showCalendar) {
        this.showCalendar.set(showCalendar);
    }

    BooleanProperty showTimeTableProperty() {
        return showTimeTable;
    }

    void setShowTimeTable(boolean showTimeTable) {
        this.showTimeTable.set(showTimeTable);
    }

    public BooleanProperty showWeatherProperty() {
        return showWeather;
    }

    void setShowWeather(boolean showWeather) {
        this.showWeather.set(showWeather);
    }

    public BooleanProperty showForecastProperty() {
        return showForecast;
    }

    void setShowForecast(boolean showForecast) {
        this.showForecast.set(showForecast);
    }

    BooleanProperty showDateProperty() {
        return showDate;
    }

    void setShowDate(boolean showDate) {
        this.showDate.set(showDate);
    }
}
