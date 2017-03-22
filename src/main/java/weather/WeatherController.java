package weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.binding.Bindings;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by maxigh on 01.03.17.
 */
public class WeatherController {
    @FXML
    Label windSpeed;
    @FXML
    Label clouds;
    @FXML
    Label description;
    @FXML
    Label humidity;
    @FXML
    Label currentLocation;
    @FXML
    Label temp;
    @FXML
    ImageView weatherIcon;

    private boolean isRunning = true;
    private WeatherDataHelper weatherDataHelper = new WeatherDataHelper();

    private Weather fetchWeather(){
        URL url = null;
        try {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=52.5527728&lon=13.424989&lang=de&units=metric&appid=19ef84c997c7a3491e789422242ebcc1");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        if (url != null) {
            try (BufferedReader buff = new BufferedReader(new InputStreamReader(url.openStream()))) {

                String data;
                String msg = "";

                while ((data = buff.readLine()) != null) {
                    msg += data;
                }

                Gson gson = new GsonBuilder().registerTypeAdapter(Weather.class, new WeatherDeserializer())
                        .create();
                System.out.println("Weather fetched");
                return gson.fromJson(msg, Weather.class);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public void update(){
        ScheduledService<WeatherDataHelper> service = new ScheduledService<WeatherDataHelper>() {
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
                        return weatherDataHelper;
                    }
                };
            }
        };

        service.setPeriod(Duration.minutes(30));
        service.start();

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

            if (!isRunning)
                service.cancel();

        });

        temp.textProperty().bind(weatherDataHelper.tempProperty().asString());
        windSpeed.textProperty().bind(weatherDataHelper.windSpeedProperty().asString());
        clouds.textProperty().bind(weatherDataHelper.cloudsProperty().asString());
        description.textProperty().bind(weatherDataHelper.descriptionProperty());
        humidity.textProperty().bind(weatherDataHelper.humidityProperty().asString());
        currentLocation.textProperty().bind(weatherDataHelper.currentLocationProperty());
        Bindings.bindBidirectional(this.weatherIcon.imageProperty(), weatherDataHelper.weatherIconProperty());

    }

    public void stopRunning() {
        isRunning = false;
    }
}


