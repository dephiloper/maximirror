package forecast;

import config.Config;
import interfaces.Controller;
import javafx.beans.binding.Bindings;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import tk.plogitech.darksky.forecast.model.Currently;
import tk.plogitech.darksky.forecast.model.Forecast;
import tk.plogitech.darksky.forecast.model.Hourly;

public class ForecastController implements Controller {
    @FXML
    public VBox forecastPane;
    @FXML
    public Label futureSummary;
    @FXML
    private Label temperature;
    @FXML
    private Label windSpeed;
    @FXML
    private Label cloudCover;
    @FXML
    private Label summary;
    @FXML
    private Label humidity;
    @FXML
    private Label timeZone;
    @FXML
    private ImageView icon;

    private ScheduledService<ForecastDataHelper> forecastService;

    private final ForecastDataHelper forecastDataHelper = new ForecastDataHelper();
    private final ForecastProvider forecastProvider = new ForecastProvider();

    @Override
    public void init() {
        createBindings();
    }

    @Override
    public void startUpdate() {
        if (!Config.instance.SHOW_FORECAST)
            return;

        forecastService = new ScheduledService<ForecastDataHelper>() {
            @Override
            protected Task<ForecastDataHelper> createTask() {
                return new Task<ForecastDataHelper>() {
                    @Override
                    protected ForecastDataHelper call() throws Exception {
                        Forecast forecast = forecastProvider.provideData();
                        if (forecast == null) {
                            System.err.println("Error fetching Weather!");
                            return null;
                        }
                        Currently currently = forecast.getCurrently();
                        Hourly hourly = forecast.getHourly();
                        ForecastDataHelper forecastDataHelper = new ForecastDataHelper(
                                currently.getTemperature(),
                                currently.getWindSpeed(),
                                currently.getCloudCover(),
                                currently.getSummary(),
                                currently.getPrecipType(),
                                currently.getHumidity(),
                                forecast.getTimezone(),
                                forecastProvider.convertSvg(currently.getIcon()),
                                hourly.getSummary());

                        updateValue(forecastDataHelper);
                        return forecastDataHelper;
                    }
                };
            }
        };

        init();

        forecastService.setPeriod(Duration.seconds(Config.instance.WEATHER_SLEEP_SECONDS));
        forecastService.start();

        forecastService.setOnSucceeded(event ->
                forecastDataHelper.reinitialize(
                        forecastService.getValue().getTemperature(),
                        forecastService.getValue().getWindSpeed(),
                        forecastService.getValue().getCloudCover(),
                        forecastService.getValue().getSummary(),
                        forecastService.getValue().getPrecipType(),
                        forecastService.getValue().getHumidity(),
                        forecastService.getValue().getTimeZone(),
                        forecastService.getValue().getIcon(),
                        forecastService.getValue().getFutureSummary()));
    }

    @Override
    public void stopRunning() {
        if (forecastService != null)
            if (forecastService.isRunning())
                forecastService.cancel();
    }

    @Override
    public void createBindings() {
        temperature.textProperty().bind(forecastDataHelper.temperatureProperty().asString("%.2f"));
        windSpeed.textProperty().bind(forecastDataHelper.windSpeedProperty().asString("%.2f"));
        cloudCover.textProperty().bind(forecastDataHelper.cloudCoverProperty().asString("%.0f"));
        summary.textProperty().bind(forecastDataHelper.summaryProperty());
        humidity.textProperty().bind(forecastDataHelper.humidityProperty().asString("%.0f"));
        timeZone.textProperty().bind(forecastDataHelper.timeZoneProperty());
        futureSummary.textProperty().bind(forecastDataHelper.futureSummaryProperty());
        Bindings.bindBidirectional(this.icon.imageProperty(), forecastDataHelper.iconProperty());
    }
}


