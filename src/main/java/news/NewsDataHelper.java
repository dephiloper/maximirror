package news;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

class NewsDataHelper {
    private StringProperty source = new SimpleStringProperty();
    private StringProperty title = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();

    NewsDataHelper(String source, String title, String description) {
        reinitialize(source,title,description);
    }

    NewsDataHelper() {

    }

    void reinitialize(String source, String title, String description) {
        this.source.setValue(source);
        this.title.setValue(title);
        this.description.setValue(description);
    }

    String getSource() {
        return source.get();
    }

    String getTitle() {
        return title.get();
    }

    String getDescription() {
        return description.get();
    }

    StringProperty descriptionProperty() {
        return description;
    }

    StringProperty titleProperty() {
        return description;
    }

    StringProperty sourceProperty() {
        return description;
    }
}
