package forecast;


import config.Config;
import helper.BufferedImageConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Forecast;

import java.io.IOException;
import java.io.InputStream;

class ForecastProvider {
    private static final String DRAWABLES_PATH = "/drawables/";
    private static final String SVG_FILE_ENDING = ".svg";

    Forecast fetchWeather(){
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(Config.instance.API_KEY))
                .location(new GeoCoordinates(new Longitude(Config.instance.LOCATION_LON), new Latitude(Config.instance.LOCATION_LAT))).build();

        DarkSkyJacksonClient client = new DarkSkyJacksonClient();
        Forecast forecast = null;
        try {
            forecast = client.forecast(request);
        } catch (ForecastException e) {
            e.printStackTrace();
        }

        return forecast;
    }

    Image convertSvg(String iconName) {
        BufferedImageConverter trans = new BufferedImageConverter();

        try (InputStream file = getClass().getResourceAsStream(DRAWABLES_PATH + iconName + SVG_FILE_ENDING)) {
            TranscoderInput transIn = new TranscoderInput(file);
            try {
                trans.transcode(transIn, null);
                return SwingFXUtils.toFXImage(trans.getBufferedImage(), null);
            } catch (TranscoderException ex) {
                System.out.println(ex.getMessage());
            }
        }
        catch (IOException io) {
            System.out.println(io.getMessage());
        }

        return null;
    }

}
