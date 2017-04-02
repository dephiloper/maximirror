package weather.forecast;

import weather.WeatherType;

public class ForecastInfo {
    private WeatherType[] weatherTypes = new WeatherType[16];
    private float[] temp = new float[16];

    public WeatherType[] getWeatherTypes() {
        return weatherTypes;
    }

    void setWeatherTypes(WeatherType[] weatherTypes) {
        this.weatherTypes = weatherTypes;
    }

    public float[] getTemp() {
        return temp;
    }

    void setTemp(float[] temp) {
        this.temp = temp;
    }
}
