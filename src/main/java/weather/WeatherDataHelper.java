package weather;

import javafx.beans.property.*;
import javafx.scene.image.Image;

    class WeatherDataHelper {
    private FloatProperty minTemp = new SimpleFloatProperty();
    private FloatProperty maxTemp = new SimpleFloatProperty();
    private FloatProperty temp = new SimpleFloatProperty();
    private FloatProperty windSpeed = new SimpleFloatProperty();
    private FloatProperty windDegree = new SimpleFloatProperty();
    private IntegerProperty clouds = new SimpleIntegerProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty type = new SimpleStringProperty();
    private IntegerProperty humidity = new SimpleIntegerProperty();
    private StringProperty currentLocation = new SimpleStringProperty();
    private ObjectProperty<Image> weatherIcon = new SimpleObjectProperty<>();

    WeatherDataHelper() {
    }

    WeatherDataHelper(float minTemp, float maxTemp, float temp, float windSpeed, float windDegree, int clouds, String description, String type, int humidity, String currentLocation) {
        this.minTemp.setValue(minTemp);
        this.maxTemp.setValue(maxTemp);
        this.temp.setValue(temp);
        this.windSpeed.setValue(windSpeed);
        this.windDegree.setValue(windDegree);
        this.clouds.setValue(clouds);
        this.description.setValue(description);
        this.type.setValue(type);
        this.humidity.setValue(humidity);
        this.currentLocation.setValue(currentLocation);
        this.weatherIcon.setValue(resolveCurrentIcon());
    }

    private Image resolveCurrentIcon() {
        WeatherType type = WeatherType.findIconId(this.type.getValue());
        if (type != null) {
            return new Image(type.getFileName());
        }

        System.err.println("Error can't read icon name.");
        return null;
    }

    float getMinTemp() {
        return minTemp.get();
    }

    float getMaxTemp() {
        return maxTemp.get();
    }

    float getTemp() {
        return temp.get();
    }

    FloatProperty tempProperty() {
        return temp;
    }

    float getWindSpeed() {
        return windSpeed.get();
    }

    FloatProperty windSpeedProperty() {
        return windSpeed;
    }

    float getWindDegree() {
        return windDegree.get();
    }

    int getClouds() {
        return clouds.get();
    }

    IntegerProperty cloudsProperty() {
        return clouds;
    }

    String getDescription() {
        return description.get();
    }

    StringProperty descriptionProperty() {
        return description;
    }

    String getType() {
        return type.get();
    }

    int getHumidity() {
        return humidity.get();
    }

    IntegerProperty humidityProperty() {
        return humidity;
    }

    String getCurrentLocation() {
        return currentLocation.get();
    }

    StringProperty currentLocationProperty() {
        return currentLocation;
    }

    ObjectProperty<Image> weatherIconProperty() {
        return weatherIcon;
    }

    void reinitialize(float minTemp, float maxTemp, float temp, float windSpeed, float windDegree, int clouds, String description, String type, int humidity, String currentLocation) {
        this.minTemp.setValue(minTemp);
        this.maxTemp.setValue(maxTemp);
        this.temp.setValue(temp);
        this.windSpeed.setValue(windSpeed);
        this.windDegree.setValue(windDegree);
        this.clouds.setValue(clouds);
        this.description.setValue(description);
        this.type.setValue(type);
        this.humidity.setValue(humidity);
        this.currentLocation.setValue(currentLocation);
        this.weatherIcon.setValue(resolveCurrentIcon());
    }
}
