package assistantmirror.maxiphil.de.mirrorconfigurator.config;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public String APPLICATION_NAME;
    public int WINDOW_HEIGHT;
    public int WINDOW_WIDTH;
    public double LOCATION_LAT;
    public double LOCATION_LON;
    public TransportStation[] TRANSPORT_STATIONS;
    public String WEATHER_API_KEY;
    public String TIME_FORMAT;
    public double CLOCK_SLEEP_SECONDS;
    public double TIMETABLE_SLEEP_SECONDS;
    public double CALENDAR_SLEEP_SECONDS;
    public long WEATHER_SLEEP_SECONDS;
    public int CALENDAR_UPCOMING_EVENT_COUNT;
    public int TIMETABLE_UPCOMING_TRANSPORT_COUNT;
    public boolean SHOW_TIME;
    public boolean SHOW_CALENDAR;
    public boolean SHOW_DATE;
    public boolean SHOW_TIMETABLE;
    public boolean SHOW_FORECAST;
    public boolean ENABLE_FULLSCREEN;
    public String DATE_FORMAT;
    public double NEWS_SLEEP_SECONDS;
    public String NEWS_API_KEY;
    public String[] NEWS_SOURCES;

    public class TransportStation {
        public String ID;
        public String[] LINE_NAME_FILTER;
        public Long WALK_DURATION_MINUTES;
    }

    public static Config jsonToConfig(String configString) {
        return new Gson().fromJson(configString, Config.class);
    }

    public static String configToJson(Config config) {
        return new Gson().toJson(config);
    }

    // Todo remove $change and serialVersionUID
    // Todo add anotations for better names
    public List<ConfigItem> generateConfigItems() throws IllegalAccessException {
        List<ConfigItem> itemList = new ArrayList<>();
        return itemList;
    }
}
