package weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.Config;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import overview.VisibilityDataHelper;
import weather.forecast.ForecastDataHelper;
import weather.forecast.ForecastDeserializer;
import weather.forecast.ForecastInfo;
import weather.forecast.ForecastProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherController {
    @FXML
    public VBox weatherPane;
    @FXML
    public VBox forecastPane;
    @FXML
    private Label windSpeed;
    @FXML
    private Label clouds;
    @FXML
    private Label description;
    @FXML
    private Label humidity;
    @FXML
    private Label currentLocation;
    @FXML
    private Label temp;
    @FXML
    private ImageView weatherIcon;
    @FXML
    private Label rainStatusToday;
    @FXML
    private Label tempForecastMax;
    @FXML
    private Label tempForecastMin;
    @FXML
    private Label rainStatusTomorrow;

    private ScheduledService<ForecastDataHelper> forecastService;
    private ScheduledService<WeatherDataHelper> weatherService;

    private final WeatherDataHelper weatherDataHelper = new WeatherDataHelper();
    private final ForecastDataHelper forecastDataHelper = new ForecastDataHelper();
    private final WeatherProvider weatherProvider = new WeatherProvider();
    private final ForecastProvider forecastProvider = new ForecastProvider();

    public void  update(){
        weatherService = new ScheduledService<WeatherDataHelper>() {
            @Override
            protected Task<WeatherDataHelper> createTask() {
                return new Task<WeatherDataHelper>() {
                    @Override
                    protected WeatherDataHelper call() throws Exception {
                        Weather weather = weatherProvider.fetchWeather();
                        if (weather == null) {
                            System.err.println("Error fetching Weather!");
                            return null;
                        }
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

        weatherService.setPeriod(Duration.seconds(Config.instance.WEATHER_SLEEP_SECONDS));
        weatherService.start();

        weatherService.setOnSucceeded(event -> weatherDataHelper.reinitialize(
        weatherService.getValue().getMinTemp(),
        weatherService.getValue().getMaxTemp(),
        weatherService.getValue().getTemp(),
        weatherService.getValue().getWindSpeed(),
        weatherService.getValue().getWindDegree(),
        weatherService.getValue().getClouds(),
        weatherService.getValue().getDescription(),
        weatherService.getValue().getType(),
        weatherService.getValue().getHumidity(),
        weatherService.getValue().getCurrentLocation()));

        temp.textProperty().bind(weatherDataHelper.tempProperty().asString());
        windSpeed.textProperty().bind(weatherDataHelper.windSpeedProperty().asString());
        clouds.textProperty().bind(weatherDataHelper.cloudsProperty().asString());
        description.textProperty().bind(weatherDataHelper.descriptionProperty());
        humidity.textProperty().bind(weatherDataHelper.humidityProperty().asString());
        currentLocation.textProperty().bind(weatherDataHelper.currentLocationProperty());
        Bindings.bindBidirectional(this.weatherIcon.imageProperty(), weatherDataHelper.weatherIconProperty());

    }

    public void updateForecast() {
        forecastService = new ScheduledService<ForecastDataHelper>() {
            @Override
            protected Task<ForecastDataHelper> createTask() {

                return new Task<ForecastDataHelper>() {
                    @Override
                    protected ForecastDataHelper call() throws Exception {

                        ForecastInfo forecastInfo = forecastProvider.fetchForecastWeather();
                        if (forecastInfo == null) {
                            System.err.println("Error fetching Weather!");
                            return null;
                        }
                        ForecastDataHelper forecastDataHelper = new ForecastDataHelper(
                                forecastInfo.getWeatherTypes(), forecastInfo.getTemp());
                        updateValue(forecastDataHelper);
                        return forecastDataHelper;
                    }
                };
            }
        };

        forecastService.setPeriod(Duration.minutes(Config.instance.WEATHER_SLEEP_SECONDS));
        forecastService.start();

        forecastService.setOnSucceeded(event -> forecastDataHelper.reinitialize(
                forecastService.getValue().getRainStatusToday(),
                forecastService.getValue().getRainStatusTomorrow(),
                forecastService.getValue().getTempForecastMax(),
                forecastService.getValue().getTempForecastMin()));

        rainStatusToday.textProperty().bind(forecastDataHelper.rainStatusTodayProperty());
        rainStatusTomorrow.textProperty().bind(forecastDataHelper.rainStatusTomorrowProperty());
        tempForecastMax.textProperty().bind(forecastDataHelper.tempForecastMaxProperty().asString());
        tempForecastMin.textProperty().bind(forecastDataHelper.tempForecastMinProperty().asString());
    }

    public void stopRunning() {
        if (weatherService.isRunning())
            weatherService.cancel();
        if (forecastService.isRunning())
            forecastService.cancel();

    }

    public void createBindings(VisibilityDataHelper visibilityDataHelper) {
        weatherPane.visibleProperty().bind(visibilityDataHelper.showWeatherProperty());
        forecastPane.visibleProperty().bind(visibilityDataHelper.showForecastProperty());
    }
}


