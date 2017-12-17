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
    public String GOOGLE_ICAL_URL;
    public double CLOCK_SLEEP_SECONDS;
    public double TIMETABLE_SLEEP_SECONDS;
    public double CALENDAR_SLEEP_SECONDS;
    public double WEATHER_SLEEP_SECONDS;
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

    private static final String CONFIG_PATH = "./config.json";
    private static final String DEFAULT_CONFIG_PATH = "/root/config.json";

    public static void create() throws IOException {
        if (instance == null)
            instance = readConfig();
    }

    private static Config readConfig() throws IOException {
        File f = new File(CONFIG_PATH);

        if(!f.exists()) {
            InputStream in = Config.class.getResourceAsStream(DEFAULT_CONFIG_PATH);
            String jsonString = inputStreamToString(in);
            PrintWriter printWriter = new PrintWriter(CONFIG_PATH, "UTF-8");
            printWriter.println(jsonString);
            printWriter.close();
        }
        InputStream in = new FileInputStream(CONFIG_PATH);
        String jsonString = inputStreamToString(in);
        return new Gson().fromJson(jsonString, Config.class);
    }

    private static String inputStreamToString(InputStream inputStream) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return content.toString();
    }
}
