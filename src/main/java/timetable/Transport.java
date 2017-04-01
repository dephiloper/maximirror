package timetable;

import java.time.LocalTime;

public class Transport {
    private String name;
    private LocalTime time;
    private String direction;

    void setName(String name) {
        this.name = name;
    }

    void setTime(LocalTime time) {
        this.time = time;
    }

    void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return this.time + " - " + this.name + " - " + this.direction;
    }
}
