package weather;
import javafx.beans.property.*;
/**
 * Created by maxig on 21.03.2017.
 */
public class ForecastDataHelper {
    private FloatProperty rainToday1 = new SimpleFloatProperty();
    private FloatProperty rainToday2 = new SimpleFloatProperty();
    private FloatProperty rainToday3 = new SimpleFloatProperty();

    public ForecastDataHelper(){}

    public ForecastDataHelper(float rainToday1, float rainToday2, float rainToday3){
        this.rainToday1.setValue(rainToday1);
        this.rainToday2.setValue(rainToday2);
        this.rainToday3.setValue(rainToday3);
    }

    public float getRainToday1() {
        return rainToday1.get();
    }

    public FloatProperty rainToday1Property() {
        return rainToday1;
    }

    public float getRainToday2() {
        return rainToday2.get();
    }

    public FloatProperty rainToday2Property() {
        return rainToday2;
    }

    public float getRainToday3() {
        return rainToday3.get();
    }

    public FloatProperty rainToday3Property() {
        return rainToday3;
    }

    public void reinitialize(float rainToday1, float rainToday2, float rainToday3){
        this.rainToday1.setValue(rainToday1);
        this.rainToday2.setValue(rainToday2);
        this.rainToday3.setValue(rainToday3);
    }
}
