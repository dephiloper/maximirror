package timetable;

import java.time.LocalTime;

/**
 * Created by phil on 23.03.17.
 */
public class Train {
    private String name;
    private LocalTime time;
    private String direction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return this.time + " - " + this.name + " - " + this.direction;
    }
}
