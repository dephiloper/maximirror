package weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.Config;
import javafx.beans.binding.Bindings;
import javafx.concurrent.ScheduledService;
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
    @FXML
    Label rainStatusToday;
    @FXML
    Label tempForecastMax;
    @FXML
    Label tempForecastMin;
    @FXML
    Label rainStatusTomorrow;


    private boolean isRunning = true;
    private WeatherDataHelper weatherDataHelper = new WeatherDataHelper();
    private ForecastDataHelper forecastDataHelper = new ForecastDataHelper();
    private Weather fetchWeather(){
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
                String msg = "";

                while ((data = buff.readLine()) != null) {
                    msg += data;
                }

                Gson gson = new GsonBuilder().registerTypeAdapter(Weather.class, new WeatherDeserializer())
                        .create();
                System.out.println("Weather fetched");
                return gson.fromJson(msg, Weather.class);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    private ForecastInfo fetchForecastWeather (){
        URL url = null;
        try {
            url = new URL(String.format("http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&lang=de&units=metric&appid=%s",
                    Config.instance.LOCATION_LAT, Config.instance.LOCATION_LON, Config.instance.API_KEY));
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
        if (url != null) {
            try (BufferedReader buff = new BufferedReader(new InputStreamReader(url.openStream()))) {

                String data;
                String msg = "";

                while ((data = buff.readLine()) != null) {
                    msg += data;
                }

                Gson gson = new GsonBuilder().registerTypeAdapter(ForecastInfo.class, new ForecastDeserializer()).create();
                System.out.println("Weatherforecast fetched");
                return gson.fromJson(msg, ForecastInfo.class);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    public void  update(){
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
    public void updateForecast() {
        ScheduledService<ForecastDataHelper> service = new ScheduledService<ForecastDataHelper>() {
            @Override
            protected Task<ForecastDataHelper> createTask() {
                return new Task<ForecastDataHelper>() {
                    @Override
                    protected ForecastDataHelper call() throws Exception {

                        ForecastInfo forecastInfo = fetchForecastWeather();
                        ForecastDataHelper forecastDataHelper = new ForecastDataHelper(
                                forecastInfo.getWeatherTypes(), forecastInfo.getTemp());
                        System.out.println("Forecast Success!");
                        updateValue(forecastDataHelper);
                        return forecastDataHelper;
                    }
                };
            }
        };

        service.setPeriod(Duration.minutes(60));
        service.start();

        service.setOnSucceeded(event -> {
            forecastDataHelper.reinitialize(
                    service.getValue().getRainStatusToday(),
                    service.getValue().getRainStatusTomorrow(),
                    service.getValue().getTempForecastMax(),
                    service.getValue().getTempForecastMin());

            if (!isRunning)
                service.cancel();

        });

        rainStatusToday.textProperty().bind(forecastDataHelper.rainStatusTodayProperty());
        rainStatusTomorrow.textProperty().bind(forecastDataHelper.rainStatusTomorrowProperty());
        tempForecastMax.textProperty().bind(forecastDataHelper.tempForecastMaxProperty().asString());
        tempForecastMin.textProperty().bind(forecastDataHelper.tempForecastMinProperty().asString());
    }
    public void stopRunning() {
        isRunning = false;
    }
}


