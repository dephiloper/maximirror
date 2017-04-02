package weather.forecast;
import com.google.gson.*;
import weather.WeatherType;

import java.lang.reflect.Type;

public class ForecastDeserializer implements JsonDeserializer<ForecastInfo> {
    @Override
    public ForecastInfo deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ForecastInfo f = new ForecastInfo();
        WeatherType[] weatherTypes = new WeatherType[16];
        float[] temps = new float[16];
        for (int i = 0; i < weatherTypes.length; i++){
            weatherTypes[i]= WeatherType.findIconId(jsonElement
                    .getAsJsonObject()
                    .get("list")
                    .getAsJsonArray()
                    .get(i)
                    .getAsJsonObject()
                    .get("weather")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("id")
                    .getAsString());

            temps[i]= jsonElement.
                    getAsJsonObject()
                    .get("list")
                    .getAsJsonArray()
                    .get(i)
                    .getAsJsonObject()
                    .get("main")
                    .getAsJsonObject()
                    .get("temp")
                    .getAsFloat();
        }
        f.setWeatherTypes(weatherTypes);
        f.setTemp(temps);
        return f;
    }
}
