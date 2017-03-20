package weather;

/**
 * Created by phil on 20.03.17.
 */
public enum WeatherType {
    CLEAR_SKY("clear sky"),
    FEW_CLOUDS("few clouds"),
    SCATTERED_CLOUDS("scattered clouds"),
    BROKEN_CLOUDS("broken clouds"),
    SHOWER_RAIN("shower rain"),
    RAIN("rain"),
    THUNDERSTORM("thunderstorm"),
    SNOW("snow"),
    MIST("mist");

    private final String identifier;

    private WeatherType(String identifier) {
        this.identifier = identifier;
    }

    public String toString() {
        return identifier;
    }
}
