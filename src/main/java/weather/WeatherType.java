package weather;

/**
 * Created by phil on 20.03.17.
 */
public enum WeatherType {
    THUNDERSTORM("2","drawables/cloud.png"),
    DRIZZLE("3","drawables/drizzle.png"),
    RAIN("5","drawables/rain.png"),
    SNOW("6","drawables/snow.png"),
    ATMOSPHERE("7","drawables/cloud.png"),
    CLEAR("800","drawables/clear.png"),
    CLOUDS("80","drawables/clouds.png"),
    EXTREME("90","drawables/cloud.png"),
    ADDITIONAL("9","drawables/cloud.png");

    private final String weatherId;
    private final String fileName;

    private WeatherType(String weatherId, String fileName) {
        this.weatherId = weatherId;
        this.fileName = fileName;
    }

    public String getFileName() { return fileName; }

    public static WeatherType findIconId(String type) {
        if (type.startsWith(THUNDERSTORM.weatherId)) {
            return THUNDERSTORM;
        } else if (type.startsWith(DRIZZLE.weatherId)) {
            return DRIZZLE;
        } else if (type.startsWith(RAIN.weatherId)) {
            return RAIN;
        } else if (type.startsWith(SNOW.weatherId)) {
            return SNOW;
        } else if (type.startsWith(ATMOSPHERE.weatherId)) {
            return ATMOSPHERE;
        } else if (type.startsWith(CLEAR.weatherId)) {
            return CLEAR;
        } else if (type.startsWith(CLOUDS.weatherId)) {
            return CLOUDS;
        } else if (type.startsWith(EXTREME.weatherId)) {
            return EXTREME;
        } else if (type.startsWith(ADDITIONAL.weatherId)) {
            return ADDITIONAL;
        }
        return null;
    }
}
