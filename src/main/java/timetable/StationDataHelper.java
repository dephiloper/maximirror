package timetable;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

class StationDataHelper {
    private ListProperty<String> transports = new SimpleListProperty<>();
    private StringProperty stationName = new SimpleStringProperty();

    StationDataHelper() {
    }

    StationDataHelper(ObservableList<String> transports, String stationName) {
        this.transports.setValue(transports);
        this.stationName.setValue(stationName);
    }

    void reinitialize(ObservableList<String> transports, String stationName) {
        this.transports.setValue(transports);
        this.stationName.setValue(stationName);
    }

    ObservableList<String> getTransports() {
        return transports.get();
    }

    ListProperty<String> transportsProperty() {
        return transports;
    }

    String getStationName() {
        return stationName.get();
    }

    StringProperty stationNameProperty() {
        return stationName;
    }
}
