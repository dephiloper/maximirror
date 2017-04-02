package weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.Config;
import weather.forecast.ForecastDeserializer;
import weather.forecast.ForecastInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

class WeatherProvider {
    Weather fetchWeather(){
        URL url = null;
        try {

            url = new URL(String.format("http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&lang=de&units=metric&appid=%s",
                    Config.instance.LOCATION_LAT, Config.instance.LOCATION_LON, Config.instance.API_KEY));
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
        if (url != null) {
            try (BufferedReader buff = new BufferedReader(new InputStreamReader(url.openStream()))) {

                String data;
                StringBuilder msg = new StringBuilder();

                while ((data = buff.readLine()) != null) {
                    msg.append(data);
                }

                Gson gson = new GsonBuilder().registerTypeAdapter(Weather.class, new WeatherDeserializer())
                        .create();
                return gson.fromJson(msg.toString(), Weather.class);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}
