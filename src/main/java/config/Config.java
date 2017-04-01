package config;

import com.google.gson.Gson;

import java.io.*;

public class Config {
    public String APPLICATION_NAME;
    public int WINDOW_HEIGHT;
    public int WINDOW_WIDTH;
    public String LOCATION_LAT;
    public String LOCATION_LON;
    public String[] TRAIN_STATIONS;
    public String API_KEY;
    public String CLOCK_FORMAT;
    public double CLOCK_SLEEP_SECONDS;
    public double TIMETABLE_SLEEP_SECONDS;
    public double CALENDAR_SLEEP_SECONDS;
    public long WEATHER_SLEEP_SECONDS;
    public int CALENDAR_UPCOMING_EVENT_COUNT;
    public int TIMETABLE_UPCOMING_TRANSPORT_COUNT;
    public boolean SHOW_CLOCK;
    public boolean SHOW_CALENDAR;
    public boolean SHOW_DATE;
    public boolean SHOW_TIMETABLE;
    public transient static Config instance;
    public boolean SHOW_WEATHER;
    public boolean SHOW_FORECAST;

    public static void create() {
        if (instance == null)
            instance = readConfig();
    }

    private static Config readConfig() {

        Config config = null;

        InputStream in =
                Config.class.getResourceAsStream("/config.json");

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
