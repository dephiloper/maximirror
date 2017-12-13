package config;

import com.google.gson.Gson;

import java.io.*;

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
    public transient static Config instance;
    public String NEWS_API_KEY;
    public String[] NEWS_SOURCES;

    public class TransportStation {
        public String ID;
        public String[] LINE_NAME_FILTER;
        public Long WALK_DURATION_MINUTES;
    }

    private static final String CONFIG_PATH = "/config.json";

    public static void create() {
        if (instance == null)
            instance = readConfig();
    }

    private static Config readConfig() {

        Config config = null;

        InputStream in =
                Config.class.getResourceAsStream(CONFIG_PATH);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            config = new Gson().fromJson(content.toString(), Config.class);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }


        return config;
    }
}
