package weather;

/**
 * Created by phil on 20.03.17.
 */
public enum WeatherType {
    THUNDERSTORM("thunderstorm", "drawables/cloud.png"),
    DRIZZLE("drizzle", "drawables/cloud.png"),
    RAIN("rain", "drawables/cloud.png"),
    SNOW("snow", "drawables/cloud.png"),
    ATMOSPHERE("atmoshere", "drawables/cloud.png"),
    CLEAR("clear", "drawables/cloud.png"),
    EXTREME("extreme", "drawables/cloud.png"),
    ADDITIONAL("additional", "drawables/cloud.png"),
    CLOUDS("clouds","drawables/cloud.png"),
    FOG("fog", "drawables/cloud.png");

    private final String identifier;
    private final String fileName;

    private WeatherType(String identifier, String fileName) {
        this.identifier = identifier;
        this.fileName = fileName;

    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFileName() { return fileName; }
}
