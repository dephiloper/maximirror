package timetable;

import java.time.LocalTime;

public class Transport {
    private String lineName;
    private LocalTime time;
    private long arrivalTime;
    private String direction;

    void setLineName(String lineName) {
        this.lineName = lineName;
    }

    void setTime(LocalTime time) {
        this.time = time;
    }

    void setDirection(String direction) {
        this.direction = direction;
    }

    void setArrivalTime(long arrivalTime) {

        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - in %s min", lineName, direction, arrivalTime);
    }
}
