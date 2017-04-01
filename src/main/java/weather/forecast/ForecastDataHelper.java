package weather.forecast;

import javafx.beans.property.*;
import weather.WeatherType;

import java.util.Calendar;

public class ForecastDataHelper {
    private StringProperty rainStatusToday = new SimpleStringProperty();
    private StringProperty rainStatusTomorrow = new SimpleStringProperty();
    private FloatProperty tempForecastMax = new SimpleFloatProperty();
    private FloatProperty tempForecastMin = new SimpleFloatProperty();

    public ForecastDataHelper() {

    }

    public ForecastDataHelper(WeatherType[] weatherTypes, float[] temps) {
        int nextDayIndex = prepareIndex();
        prepareRain(weatherTypes, nextDayIndex);
        prepareTemp(temps, nextDayIndex);
    }

    private void prepareRain(WeatherType[] weatherTypes, int index) {
        String rainStatusToday = null;
        String rainStatusTomorrow = null;
        for (int i = 0; i <= index + 8; i++) {
            if (i < index && weatherTypes[i].toString().toLowerCase().contains("rain"))
                rainStatusToday = "Regen";
            if (i >= index && weatherTypes[i].toString().toLowerCase().contains("rain")) {
                rainStatusTomorrow = "Regen";
            }
        }

        if (rainStatusTomorrow == null)
            rainStatusTomorrow = "Kein Regen";
        if (rainStatusToday == null)
            rainStatusToday = "Kein Regen";

        this.rainStatusToday.setValue(rainStatusToday);
        this.rainStatusTomorrow.setValue(rainStatusTomorrow);
    }

    private void prepareTemp(float[] temps, int index) {
        float max = temps[0];
        float min = temps[0];
        for (int i = index; i < index + 7; i++) {
            if (temps[i + 1] > max)
                max = temps[i + 1];
            if (temps[i + 1] < min)
                min = temps[i + 1];
        }
        setTempForecastMax(max);
        setTempForecastMin(min);
    }

    private int prepareIndex() {
        int index = 1;
        int hour = Calendar.HOUR_OF_DAY;
        boolean nextDay = false;
        while (!nextDay) {
            if ((hour + 3) <= 24) {
                hour += 3;
                index++;
            } else {
                index++;
                nextDay = true;
            }
        }
        return index;
    }

    public void reinitialize(String rainStatusToday, String rainStatusTomorrow, float tempForecastMax, float tempForecastMin) {
        this.rainStatusTomorrow.setValue(rainStatusTomorrow);
        this.rainStatusToday.setValue(rainStatusToday);
        this.tempForecastMin.setValue(tempForecastMin);
        this.tempForecastMax.setValue(tempForecastMax);
    }

    public String getRainStatusToday() {
        return rainStatusToday.get();
    }

    public StringProperty rainStatusTodayProperty() {
        return rainStatusToday;
    }

    public String getRainStatusTomorrow() {
        return rainStatusTomorrow.get();
    }

    public StringProperty rainStatusTomorrowProperty() {
        return rainStatusTomorrow;
    }

    public float getTempForecastMax() {
        return tempForecastMax.get();
    }

    private void setTempForecastMax(float tempForecastMax) {
        this.tempForecastMax.set(tempForecastMax);
    }

    public FloatProperty tempForecastMaxProperty() {
        return tempForecastMax;
    }

    public float getTempForecastMin() {
        return tempForecastMin.get();
    }

    private void setTempForecastMin(float tempForecastMin) {
        this.tempForecastMin.set(tempForecastMin);
    }

    public FloatProperty tempForecastMinProperty() {
        return tempForecastMin;
    }
}