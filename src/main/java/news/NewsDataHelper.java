package news;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

    void reinitialize(NewsDataHelper dataHelper) {
        this.source.setValue(dataHelper.source.getValue());
        this.title.setValue(dataHelper.title.getValue());
        this.description.setValue(dataHelper.description.getValue());
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
        return title;
    }

    StringProperty sourceProperty() {
        return source;
    }
}
