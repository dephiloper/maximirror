package weather;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by maxigh on 01.03.17.
 */
public class WheatherController {
    public WheatherController() {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=52.5527728&lon=13.424989&appid=19ef84c997c7a3491e789422242ebcc1");
            //private URL url = (configureURL());
            JSONObject json = new JSONObject(url);
        } catch (Exception e) {}
    }

    private String configureURL(){
    //TODO
        return null;
    }
}


