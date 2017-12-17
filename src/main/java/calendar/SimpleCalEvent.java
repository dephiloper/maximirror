package calendar;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class SimpleCalEvent {
    private LocalDateTime time;
    private String name;

    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yy");
    public static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

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
        String s = "";
        if (time.toLocalDate().isEqual(LocalDate.now())) { // heute
            s = time.format(timeFormat) + " Uhr - " + name;
        } else if (time.toLocalDate().isEqual(LocalDate.now().plusDays(1))) {
            s = "Morgen " + time.format(timeFormat) + " Uhr - " + name;
        }
        else
            {
            s = time.format(dateFormat) + " " + time.format(timeFormat) + " Uhr - " + name;
        }
        return s;
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
