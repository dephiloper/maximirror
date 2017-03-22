package weather;
import com.google.gson.*;
import java.lang.reflect.Type;
/**
 * Created by maxig on 21.03.2017.
 */
public class ForecastDeserializer implements JsonDeserializer<ForecastInfo> {
    @Override
    public ForecastInfo deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ForecastInfo f = new ForecastInfo();
        float[] rainsToday = new float[3];

        for (int i = 0; i < rainsToday.length; i++) {
            JsonObject rainObject = jsonElement
                    .getAsJsonObject()
                    .get("list")
                    .getAsJsonArray()
                    .get(i)
                    .getAsJsonObject()
                    .get("rain")
                    .getAsJsonObject();

            if (rainObject.size() != 0)
                rainsToday[i] = rainObject.getAsJsonPrimitive("3h").getAsFloat();
        }
        f.setRainToday1(rainsToday[0]);
        f.setRainToday2(rainsToday[1]);
        f.setRainToday3(rainsToday[2]);

        return f;
    }
}
