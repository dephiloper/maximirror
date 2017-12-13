package assistantmirror.maxiphil.de.mirrorconfigurator.config;

import com.google.gson.Gson;

import java.io.*;
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
    public String API_KEY;
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

    public List<ConfigItem> generateConfigItems() throws IllegalAccessException {
        List<ConfigItem> itemList = new ArrayList<>();
        itemList.add(new ConfigItemString("Anwendungsname:", APPLICATION_NAME));
        itemList.add(new ConfigItemString("Fenstergröße:", String.valueOf(WINDOW_HEIGHT)));
        itemList.add(new ConfigItemString("Fensterbreite:", String.valueOf(WINDOW_WIDTH)));
        itemList.add(new ConfigItemString("Standort Latitude:", String.valueOf(LOCATION_LAT)));
        itemList.add(new ConfigItemString("Standort Lontitude", String.valueOf(LOCATION_LON)));
        //itemList.add(new ConfigItemString("Anwendungsname:", String.valueOf(TRANSPORT_STATIONS)));
        itemList.add(new ConfigItemString("Api Key:", String.valueOf(API_KEY)));
        itemList.add(new ConfigItemString("Zeit Format:", String.valueOf(TIME_FORMAT)));
        itemList.add(new ConfigItemString("Update Uhr (s):", String.valueOf(CLOCK_SLEEP_SECONDS)));
        itemList.add(new ConfigItemString("Update Fahrzeiten (s):", String.valueOf(TIMETABLE_SLEEP_SECONDS)));
        itemList.add(new ConfigItemString("Update Kalender (s):", String.valueOf(CALENDAR_SLEEP_SECONDS)));
        itemList.add(new ConfigItemString("Update Weather (s):", String.valueOf(WEATHER_SLEEP_SECONDS)));
        itemList.add(new ConfigItemString("Anzahl Kalender Events:", String.valueOf(CALENDAR_UPCOMING_EVENT_COUNT)));
        itemList.add(new ConfigItemString("Anzahl Stationen Events:", String.valueOf(TIMETABLE_UPCOMING_TRANSPORT_COUNT)));
        itemList.add(new ConfigItemBool("Zeit anzeigen:", SHOW_TIME));
        itemList.add(new ConfigItemBool("Kalender anzeigen:", SHOW_CALENDAR));
        itemList.add(new ConfigItemBool("Datum anzeigen:", SHOW_DATE));
        itemList.add(new ConfigItemBool("Fahrzeiten anzeigen:", SHOW_TIMETABLE));
        itemList.add(new ConfigItemBool("Wetter anzeigen:", SHOW_FORECAST));
        itemList.add(new ConfigItemBool("Vollbild:", ENABLE_FULLSCREEN));
        itemList.add(new ConfigItemString("Datum Format:", String.valueOf(DATE_FORMAT)));

        return itemList;
    }
}
