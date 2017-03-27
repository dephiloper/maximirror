package config;

import com.google.gson.Gson;

import java.io.*;

/**
 * Created by phil on 27.03.17.
 */
public class Config {
    public int WINDOW_HEIGHT;
    public int WINDOW_WIDTH;
    public String LOCATION_LAT;
    public String LOCATION_LON;
    public String[] TRAIN_STATIONS;
    public String API_KEY;
    public transient static Config instance;

    private Config() {

    }

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
            String line = "";

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
