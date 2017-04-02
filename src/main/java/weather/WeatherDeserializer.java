package weather;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by maxigh on 01.03.17.
 */
public class WeatherDeserializer implements JsonDeserializer<Weather> {
    @Override
    public Weather deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Weather w = new Weather();

        w.setDescription(jsonElement
                .getAsJsonObject()
                .get("weather")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject()
                .get("description")
                .getAsString());

        w.setClouds(jsonElement
                .getAsJsonObject()
                .get("clouds")
                .getAsJsonObject()
                .get("all")
                .getAsInt());

        w.setCurrentLocation(jsonElement
                .getAsJsonObject()
                .get("name")
                .getAsString());

        w.setMaxTemp(jsonElement
                .getAsJsonObject()
                .get("main")
                .getAsJsonObject()
                .get("temp_max")
                .getAsFloat());

        w.setMinTemp(jsonElement
                .getAsJsonObject()
                .get("main")
                .getAsJsonObject()
                .get("temp_min")
                .getAsFloat());

        w.setTemp(jsonElement
                .getAsJsonObject()
                .get("main")
                .getAsJsonObject()
                .get("temp")
                .getAsFloat());

        w.setHumidity(jsonElement
                .getAsJsonObject()
                .get("main")
                .getAsJsonObject()
                .get("humidity")
                .getAsInt());

        w.setType(jsonElement
                .getAsJsonObject()
                .get("weather")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject()
                .get("id")
                .getAsString());

        JsonElement windDegElement = jsonElement
        .getAsJsonObject()
        .get("wind")
        .getAsJsonObject()
        .get("deg");

        if (windDegElement != null)
            w.setWindDegree(windDegElement.getAsInt());

        w.setWindSpeed(jsonElement
                .getAsJsonObject()
                .get("wind")
                .getAsJsonObject()
                .get("speed")
                .getAsFloat());

        return w;
    }
}
