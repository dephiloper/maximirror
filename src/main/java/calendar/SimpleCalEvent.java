package calendar;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class SimpleCalEvent {
    private LocalDateTime time;
    private String name;

    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public SimpleCalEvent(String name, LocalDateTime time) {
        this.name = name;
        this.time = time;
    }

    public SimpleCalEvent(String name, long time) {
        this.name = name;
        this.time = LocalDateTime.ofInstant(Instant.ofEpochSecond(time/1000), TimeZone.getDefault().toZoneId());
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return time.format(dateFormat) + " : " + name;
    }

    public boolean after(SimpleCalEvent ev) {
        return this.time.isAfter(ev.time);
    }

    public boolean before(SimpleCalEvent ev) {
        return this.time.isBefore(ev.time);
    }

    public boolean after(LocalDateTime time) {
        return this.time.isAfter(time);
    }
}
