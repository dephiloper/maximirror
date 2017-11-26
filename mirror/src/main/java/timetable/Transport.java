package timetable;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Transport {
    private String lineName;
    private long arrivalTime;
    private String direction;
    private LocalTime time;

    void setLineName(String lineName) {
        this.lineName = lineName;
    }

    void setDirection(String direction) {
        this.direction = direction;
    }

    void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - in %s min", lineName, direction, ChronoUnit.MINUTES.between(LocalTime.now(),time));
    }
}
