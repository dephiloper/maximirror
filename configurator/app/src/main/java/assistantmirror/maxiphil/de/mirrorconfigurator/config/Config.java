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
        ConfigItem item = null;
        for (Field field : Config.class.getDeclaredFields()) {
            Object x = field.get(this);
            if (x instanceof Boolean) {
                item = new ConfigItem<>(field.getName(), (Boolean) x);
            } else if (x instanceof Array){
                // Todo recursive call (Transport Stations, Line Name Filter and News Sources)
            } else {
                item = new ConfigItem<>(field.getName(), String.valueOf(x));
            }
            System.out.println(x);

            itemList.add(item);
        }

        /*itemList.add(new ConfigItem<>("Anwendungsname:", APPLICATION_NAME));
        itemList.add(new ConfigItem<>("Fenstergröße:", String.valueOf(WINDOW_HEIGHT)));
        itemList.add(new ConfigItem<>("Fensterbreite:", String.valueOf(WINDOW_WIDTH)));
        itemList.add(new ConfigItem<>("Standort Latitude:", String.valueOf(LOCATION_LAT)));
        itemList.add(new ConfigItem<>("Standort Lontitude", String.valueOf(LOCATION_LON)));
        //itemList.add(new ConfigItemString("Anwendungsname:", String.valueOf(TRANSPORT_STATIONS)));
        itemList.add(new ConfigItem<>("Api Key:", String.valueOf(API_KEY)));
        itemList.add(new ConfigItem<>("Zeit Format:", String.valueOf(TIME_FORMAT)));
        itemList.add(new ConfigItem<>("Update Uhr (s):", String.valueOf(CLOCK_SLEEP_SECONDS)));
        itemList.add(new ConfigItem<>("Update Fahrzeiten (s):", String.valueOf(TIMETABLE_SLEEP_SECONDS)));
        itemList.add(new ConfigItem<>("Update Kalender (s):", String.valueOf(CALENDAR_SLEEP_SECONDS)));
        itemList.add(new ConfigItem<>("Update Weather (s):", String.valueOf(WEATHER_SLEEP_SECONDS)));
        itemList.add(new ConfigItem<>("Anzahl Kalender Events:", String.valueOf(CALENDAR_UPCOMING_EVENT_COUNT)));
        itemList.add(new ConfigItem<>("Anzahl Stationen Events:", String.valueOf(TIMETABLE_UPCOMING_TRANSPORT_COUNT)));
        itemList.add(new ConfigItem<>("Zeit anzeigen:", SHOW_TIME));
        itemList.add(new ConfigItem<>("Kalender anzeigen:", SHOW_CALENDAR));
        itemList.add(new ConfigItem<>("Datum anzeigen:", SHOW_DATE));
        itemList.add(new ConfigItem<>("Fahrzeiten anzeigen:", SHOW_TIMETABLE));
        itemList.add(new ConfigItem<>("Wetter anzeigen:", SHOW_FORECAST));
        itemList.add(new ConfigItem<>("Vollbild:", ENABLE_FULLSCREEN));
        itemList.add(new ConfigItem<>("Datum Format:", String.valueOf(DATE_FORMAT)));
*/
        return itemList;
    }
}
