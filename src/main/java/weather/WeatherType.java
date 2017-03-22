package weather;

/**
 * Created by phil on 20.03.17.
 */
public enum WeatherType {
    CLEAR_SKY("01","drawables/cloud.png"),
    FEW_CLOUDS("02","drawables/cloud.png"),
    SCATTERED_CLOUDS("03","drawables/cloud.png"),
    BROKEN_CLOUDS("04","drawables/cloud.png"),
    SHOWER_RAIN("09","drawables/cloud.png"),
    RAIN("10","drawables/cloud.png"),
    THUNDERSTORM("11","drawables/cloud.png"),
    SNOW("13","drawables/cloud.png"),
    MIST("50","drawables/cloud.png");

    private final String iconId;
    private final String fileName;

    private WeatherType(String iconId, String fileName) {
        this.iconId = iconId;
        this.fileName = fileName;
    }

    public String getFileName() { return fileName; }

    public static WeatherType findIconId(String type) {
        if (type.startsWith(CLEAR_SKY.iconId)) {
            return CLEAR_SKY;
        } else if (type.startsWith(FEW_CLOUDS.iconId)) {
            return FEW_CLOUDS;
        } else if (type.startsWith(SCATTERED_CLOUDS.iconId)) {
            return SCATTERED_CLOUDS;
        } else if (type.startsWith(BROKEN_CLOUDS.iconId)) {
            return BROKEN_CLOUDS;
        } else if (type.startsWith(SHOWER_RAIN.iconId)) {
            return SHOWER_RAIN;
        } else if (type.startsWith(RAIN.iconId)) {
            return RAIN;
        } else if (type.startsWith(THUNDERSTORM.iconId)) {
            return THUNDERSTORM;
        } else if (type.startsWith(SNOW.iconId)) {
            return SNOW;
        } else if (type.startsWith(MIST.iconId)) {
            return MIST;
        }

        return null;
    }
}
