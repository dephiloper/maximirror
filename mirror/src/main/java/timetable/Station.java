package timetable;

import java.util.List;

class Station {
    private String stationName;
    private List<String> transports;

    Station(String stationName, List<String> transports) {
        this.stationName = stationName;
        this.transports = transports;
    }

    String getStationName() {
        return stationName;
    }

    List<String> getTransports() {
        return transports;
    }
}
