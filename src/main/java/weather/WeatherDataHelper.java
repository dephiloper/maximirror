package weather;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Created by maxigh on 04.03.17.
 */
public class WeatherDataHelper {
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

    public WeatherDataHelper() {
    }

    public WeatherDataHelper(float minTemp, float maxTemp, float temp, float windSpeed, float windDegree, int clouds, String description, String type, int humidity, String currentLocation) {
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
        return new Image(type.getFileName());
        }

    public float getMinTemp() {
        return minTemp.get();
    }

    public FloatProperty minTempProperty() {
        return minTemp;
    }

    public float getMaxTemp() {
        return maxTemp.get();
    }

    public FloatProperty maxTempProperty() {
        return maxTemp;
    }

    public float getTemp() {
        return temp.get();
    }

    public FloatProperty tempProperty() {
        return temp;
    }

    public float getWindSpeed() {
        return windSpeed.get();
    }

    public FloatProperty windSpeedProperty() {
        return windSpeed;
    }

    public float getWindDegree() {
        return windDegree.get();
    }

    public FloatProperty windDegreeProperty() {
        return windDegree;
    }

    public int getClouds() {
        return clouds.get();
    }

    public IntegerProperty cloudsProperty() {
        return clouds;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int getHumidity() {
        return humidity.get();
    }

    public IntegerProperty humidityProperty() {
        return humidity;
    }

    public String getCurrentLocation() {
        return currentLocation.get();
    }

    public StringProperty currentLocationProperty() {
        return currentLocation;
    }

    public Image getWeatherIcon() {
        return weatherIcon.get();
    }

    public ObjectProperty<Image> weatherIconProperty() {
        return weatherIcon;
    }

    public void Reinitialize(float minTemp, float maxTemp, float temp, float windSpeed, float windDegree, int clouds, String description, String type, int humidity, String currentLocation) {
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
