package assistantmirror.maxiphil.de.mirrorconfigurator.config;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
    public String GOOGLE_ICAL_URL;

    public class TransportStation {
        public String ID;
        public String[] LINE_NAME_FILTER;
        public Long WALK_DURATION_MINUTES;
        public TransportStation(String id, String[] line_name_filter, long walk_duration_minutes){
            this.ID = id;
            this.LINE_NAME_FILTER = line_name_filter;
            this.WALK_DURATION_MINUTES = walk_duration_minutes;
        }
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

        itemList.add(new ConfigItem<>("Anwendungsname:", APPLICATION_NAME));
        itemList.add(new ConfigItem<>("Fenstergröße:", String.valueOf(WINDOW_HEIGHT)));
        itemList.add(new ConfigItem<>("Fensterbreite:", String.valueOf(WINDOW_WIDTH)));
        itemList.add(new ConfigItem<>("Standort Latitude:", String.valueOf(LOCATION_LAT)));
        itemList.add(new ConfigItem<>("Standort Lontitude:", String.valueOf(LOCATION_LON)));
        for (TransportStation station : TRANSPORT_STATIONS){
            itemList.add(new ConfigItem<>("Station:", new MarkAsCaption()));
            itemList.add(new ConfigItem<>("Station-ID:", station.ID));
            itemList.add(new ConfigItem<>("Bahnen:", Arrays.toString(station.LINE_NAME_FILTER)));
            itemList.add(new ConfigItem<>("Zeit zur Station (min):", String.valueOf(station.WALK_DURATION_MINUTES.toString())));
        }
        itemList.add(new ConfigItem<>("Wetter API Key:", String.valueOf(WEATHER_API_KEY)));
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
        itemList.add(new ConfigItem<>("Update News (s):", String.valueOf(NEWS_SLEEP_SECONDS)));
        itemList.add(new ConfigItem<>("News API Key:", String.valueOf(NEWS_API_KEY)));
        itemList.add(new ConfigItem<>("News Quellen:", Arrays.toString(NEWS_SOURCES)));
        itemList.add(new ConfigItem<>("Google ical URL:", GOOGLE_ICAL_URL));

        return itemList;
    }

    public Config generateConfigFromItemList(List<ConfigItem> itemList){
        Config config = new Config();
        config.APPLICATION_NAME = itemList.get(0).getValue().toString();
        config.WINDOW_HEIGHT = Integer.parseInt(itemList.get(1).getValue().toString());
        config.WINDOW_WIDTH = Integer.parseInt(itemList.get(2).getValue().toString());
        config.LOCATION_LAT = Double.parseDouble(itemList.get(3).getValue().toString());
        config.LOCATION_LON = Double.parseDouble(itemList.get(4).getValue().toString());
        config.TRANSPORT_STATIONS = new TransportStation[]{
                new TransportStation(itemList.get(6).getValue().toString(), new String[]{itemList.get(7).getValue().toString()}, Long.parseLong(itemList.get(8).getValue().toString())),
                new TransportStation(itemList.get(10).getValue().toString(), new String[]{itemList.get(11).getValue().toString()}, Long.parseLong(itemList.get(12).getValue().toString()))};
        config.WEATHER_API_KEY = itemList.get(13).getValue().toString();
        config.TIME_FORMAT = itemList.get(14).getValue().toString();
        config.CLOCK_SLEEP_SECONDS = Double.parseDouble(itemList.get(15).getValue().toString());
        config.TIMETABLE_SLEEP_SECONDS = Double.parseDouble(itemList.get(16).getValue().toString());
        config.CALENDAR_SLEEP_SECONDS = Double.parseDouble(itemList.get(17).getValue().toString());
        config.WEATHER_SLEEP_SECONDS = Long.parseLong(itemList.get(18).getValue().toString());
        config.CALENDAR_UPCOMING_EVENT_COUNT = Integer.parseInt(itemList.get(19).getValue().toString());
        config.TIMETABLE_UPCOMING_TRANSPORT_COUNT = Integer.parseInt(itemList.get(20).getValue().toString());
        config.SHOW_TIME = Boolean.parseBoolean(itemList.get(21).getValue().toString());
        config.SHOW_CALENDAR = Boolean.parseBoolean(itemList.get(22).getValue().toString());
        config.SHOW_DATE = Boolean.parseBoolean(itemList.get(23).getValue().toString());
        config.SHOW_TIMETABLE = Boolean.parseBoolean(itemList.get(24).getValue().toString());
        config.SHOW_FORECAST = Boolean.parseBoolean(itemList.get(25).getValue().toString());
        config.ENABLE_FULLSCREEN = Boolean.parseBoolean(itemList.get(26).getValue().toString());
        config.DATE_FORMAT = itemList.get(27).getValue().toString();
        config.NEWS_SLEEP_SECONDS = Double.parseDouble(itemList.get(28).getValue().toString());
        config.NEWS_API_KEY = itemList.get(29).getValue().toString();
        config.NEWS_SOURCES = new String[]{itemList.get(30).getValue().toString()};
        config.GOOGLE_ICAL_URL = itemList.get(31).getValue().toString();

        return config;
    }
}
