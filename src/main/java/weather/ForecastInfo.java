package weather;

/**
 * Created by maxig on 21.03.2017.
 */
public class ForecastInfo {
    private WeatherType[] weatherTypes = new WeatherType[16];
    private float[] temp = new float[16];

    public WeatherType[] getWeatherTypes() {
        return weatherTypes;
    }

    public void setWeatherTypes(WeatherType[] weatherTypes) {
        this.weatherTypes = weatherTypes;
    }

    public float[] getTemp() {
        return temp;
    }

    public void setTemp(float[] temp) {
        this.temp = temp;
    }
}
