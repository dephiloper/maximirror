package calendar;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

class CalendarDataHelper {
    private ListProperty<String> events = new SimpleListProperty<>();

    CalendarDataHelper(ObservableList<String> events) {
        reinitialize(events);
    }

    CalendarDataHelper() {

    }

    void reinitialize(ObservableList<String> events) {
        this.events.setValue(events);
    }

    public ObservableList<String> getEvents() {
        return events.get();
    }

    public ListProperty<String> eventsProperty() {
        return events;
    }

    public void setEvents(ObservableList<String> events) {
        this.events.set(events);
    }
}
