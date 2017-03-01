package weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by maxigh on 01.03.17.
 */
public class WeatherController {
    public WeatherController() {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=52.5527728&lon=13.424989&appid=19ef84c997c7a3491e789422242ebcc1");
            //private URL url = (configureURL());
            BufferedReader buff = new BufferedReader(new InputStreamReader(url.openStream()));
            String data;
            String msg="";
            while((data=buff.readLine())!=null){
                msg+=data;
            }
            Gson gson = new GsonBuilder().registerTypeAdapter(Weather.class, new WeatherDeserializer()).create();
            System.out.println(msg);
            Weather weather = gson.fromJson(msg, Weather.class);
        } catch (Exception e) {}
    }

    private String configureURL(){
    //TODO
        return null;
    }
}


