package weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by maxigh on 01.03.17.
 */
public class WeatherController {
    @FXML
    Label minTemp;
    @FXML
    Label maxTemp;
    @FXML
    Label windSpeed;
    @FXML
    Label windDegree;
    @FXML
    Label clouds;
    @FXML
    Label description;
    @FXML
    Label type;
    @FXML
    Label humidity;
    @FXML
    Label currentLocation;
    @FXML
    Label temp;

    private boolean isRunning = true;
    private WeatherDataHelper weatherDataHelper = new WeatherDataHelper();
    private boolean isInitalServiceCall = true;

    private Weather fetchWeather(){
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
            return gson.fromJson(msg, Weather.class);
        } catch (Exception e) {
            System.out.println("Fehler");
            return null;}
    }

    public void  updateWeather(){
        Service<WeatherDataHelper> service = new Service<WeatherDataHelper>() {
            @Override
            protected Task<WeatherDataHelper> createTask() {
                return new Task<WeatherDataHelper>() {
                    @Override
                    protected WeatherDataHelper call() throws Exception {
                        Weather weather = fetchWeather();
                        WeatherDataHelper weatherDataHelper = new WeatherDataHelper(
                                weather.getMinTemp(),
                                weather.getMaxTemp(),
                                weather.getTemp(),
                                weather.getWindSpeed(),
                                weather.getWindDegree(),
                                weather.getClouds(),
                                weather.getDescription(),
                                weather.getType(),
                                weather.getHumidity(),
                                weather.getCurrentLocation());

                        updateValue(weatherDataHelper);
                        if (!isInitalServiceCall)
                            TimeUnit.HOURS.sleep(1);
                        isInitalServiceCall = false;
                        return weatherDataHelper;
                    }
                };
            }
        };
        service.restart();
        service.setOnSucceeded(event -> {
            weatherDataHelper.Reinitialize(
            service.getValue().getMinTemp(),
            service.getValue().getMaxTemp(),
            service.getValue().getTemp(),
            service.getValue().getWindSpeed(),
            service.getValue().getWindDegree(),
            service.getValue().getClouds(),
            service.getValue().getDescription(),
            service.getValue().getType(),
            service.getValue().getHumidity(),
            service.getValue().getCurrentLocation());

            if (service.isRunning())
                service.cancel();

            if (isRunning)
                service.restart();
        });

        temp.textProperty().bind(weatherDataHelper.tempProperty().asString());
        minTemp.textProperty().bind(weatherDataHelper.minTempProperty().asString());
        maxTemp.textProperty().bind(weatherDataHelper.maxTempProperty().asString());
        windSpeed.textProperty().bind(weatherDataHelper.windSpeedProperty().asString());
        windDegree.textProperty().bind(weatherDataHelper.windDegreeProperty().asString());
        clouds.textProperty().bind(weatherDataHelper.cloudsProperty().asString());
        description.textProperty().bind(weatherDataHelper.descriptionProperty());
        type.textProperty().bind(weatherDataHelper.typeProperty());
        humidity.textProperty().bind(weatherDataHelper.humidityProperty().asString());
        currentLocation.textProperty().bind(weatherDataHelper.currentLocationProperty());
    }

    public void stopRunning() {
        isRunning = false;
    }
}


