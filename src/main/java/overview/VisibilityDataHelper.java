package overview;

import config.Config;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class VisibilityDataHelper {
    private BooleanProperty showTime = new SimpleBooleanProperty(Config.instance.SHOW_TIME);
    private BooleanProperty showDate = new SimpleBooleanProperty(Config.instance.SHOW_DATE);
    private BooleanProperty showCalendar = new SimpleBooleanProperty(Config.instance.SHOW_CALENDAR);
    private BooleanProperty showTimeTable = new SimpleBooleanProperty(Config.instance.SHOW_TIMETABLE);
    private BooleanProperty showForecast = new SimpleBooleanProperty(Config.instance.SHOW_FORECAST);

    BooleanProperty showTimeProperty() {
        return showTime;
    }

    BooleanProperty showCalendarProperty() {
        return showCalendar;
    }

    BooleanProperty showTimeTableProperty() {
        return showTimeTable;
    }

    public BooleanProperty showForecastProperty() {
        return showForecast;
    }

    BooleanProperty showDateProperty() {
        return showDate;
    }

}
