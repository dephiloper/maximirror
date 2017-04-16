package calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by phil on 03.04.17.
 */
class CalProvider {
    List<String> getEvents() {
        List<String> list = new ArrayList<>();
        String str = null;
        try {
            str = new String(Files.readAllBytes(Paths.get(getClass().getResource("/cal.ics").toURI())));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        ICalendar ical = Biweekly.parse(str).first();

        List<VEvent> events = ical.getEvents();
        events.sort(Comparator.comparing(t -> t.getDateStart().getValue()));


        for (VEvent event : events) {
            Date date = event.getDateStart().getValue().getRawComponents().toDate();
            LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (dateTime.isAfter(LocalDateTime.now()))
                System.out.println(event.getSummary().getValue());
        }

        return null;
    }
}
