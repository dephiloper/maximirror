package forecast;


import config.Config;
import helper.BufferedImageConverter;
import interfaces.Provider;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Forecast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

class ForecastProvider implements Provider<Forecast> {
    private static final String DRAWABLES_PATH = "/drawables/";
    //private static final String SVG_FILE_ENDING = ".svg";
    private static final String PNG_FILE_ENDING = ".png";
    private static final String FALLBACK_IMG = "rainbow";

    /*Image convertSvg(String iconName) {
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

        if (!Objects.equals(iconName, FALLBACK_IMG))
            return convertSvg(FALLBACK_IMG);

        return null;
    }*/

    @Override
    public Forecast provideData() {
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(Config.instance.WEATHER_API_KEY))
                .location(new GeoCoordinates(new Longitude(Config.instance.LOCATION_LON), new Latitude(Config.instance.LOCATION_LAT))).build();

        DarkSkyJacksonClient client = new DarkSkyJacksonClient();
        Forecast forecast = null;
        try {
            forecast = client.forecast(request);
        } catch (ForecastException e) {
            System.err.println(e.getMessage());
        }

        return forecast;
    }

    public Image convertPng(String iconName) {
        try (InputStream inputStream = getClass().getResourceAsStream(DRAWABLES_PATH + iconName + PNG_FILE_ENDING)) {
            if (inputStream != null) {
                return new Image(inputStream);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        if (!Objects.equals(iconName, FALLBACK_IMG))
            return convertPng(FALLBACK_IMG);

        return null;
    }
}
