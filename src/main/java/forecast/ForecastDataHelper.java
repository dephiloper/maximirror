package forecast;

import javafx.beans.property.*;
import javafx.scene.image.Image;

    class ForecastDataHelper {
    private DoubleProperty temperature = new SimpleDoubleProperty();
    private DoubleProperty windSpeed = new SimpleDoubleProperty();
    private IntegerProperty windBearing = new SimpleIntegerProperty();
    private DoubleProperty cloudCover = new SimpleDoubleProperty();
    private StringProperty summary = new SimpleStringProperty();
    private StringProperty precipType = new SimpleStringProperty();
    private DoubleProperty humidity = new SimpleDoubleProperty();
    private StringProperty timeZone = new SimpleStringProperty();
    private ObjectProperty<Image> icon = new SimpleObjectProperty<>();

    ForecastDataHelper() {
    }

    ForecastDataHelper(double temperature, double windSpeed, int windBearing, double cloudCover, String summary, String precipType, double humidity, String timeZone, Image icon) {
        this.temperature.setValue(temperature);
        this.windSpeed.setValue(windSpeed);
        this.windBearing.setValue(windBearing);
        this.cloudCover.setValue(cloudCover);
        this.summary.setValue(summary);
        this.precipType.setValue(precipType);
        this.humidity.setValue(humidity);
        this.timeZone.setValue(timeZone);
        this.icon.setValue(icon);
    }

    double getTemperature() {
        return temperature.get();
    }

    DoubleProperty temperatureProperty() {
        return temperature;
    }

    double getWindSpeed() {
        return windSpeed.get() * 3.6;
    }

    DoubleProperty windSpeedProperty() {
        return windSpeed;
    }

    int getWindBearing() {
        return windBearing.get();
    }

    DoubleProperty cloudCoverProperty() {
            return cloudCover;
        }

    double getCloudCover() {
        return cloudCover.get() * 100;
    }

    String getSummary() {
        return summary.get();
    }

    StringProperty summaryProperty() {
        return summary;
    }

    ObjectProperty<Image> iconProperty() {
            return icon;
        }

    String getPrecipType() {
        return precipType.get();
    }

    double getHumidity() {
        return humidity.get() * 100;
    }

    DoubleProperty humidityProperty() {
        return humidity;
    }

    String getTimeZone() {
        return timeZone.get();
    }

    StringProperty timeZoneProperty() {
        return timeZone;
    }

    Image getIcon() { return icon.get(); }

        void reinitialize(double temperature, double windSpeed, int windBearing, double cloudCover, String summary, String precipType, double humidity, String timeZone, Image weatherIcon) {
        this.temperature.setValue(temperature);
        this.windSpeed.setValue(windSpeed);
        this.windBearing.setValue(windBearing);
        this.cloudCover.setValue(cloudCover);
        this.summary.setValue(summary);
        this.precipType.setValue(precipType);
        this.humidity.setValue(humidity);
        this.timeZone.setValue(timeZone);
        this.icon.setValue(weatherIcon);
    }
}
