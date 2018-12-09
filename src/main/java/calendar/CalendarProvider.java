package calendar;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import config.Config;
import javafx.collections.FXCollections;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;

import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.CalendarComponent;

import static java.lang.StrictMath.min;

class CalendarProvider {
    private Calendar calendar;
    private List<SimpleCalEvent> sortedCalEvents;
    private static final int DEFAULT_DAYS_TO_PREDICT = 365*3;
    private static final long MILLIS_TO_DAYS = 1000*3600*24;

    private boolean loaded = false;

    private void loadCalendar() {
        String myCalendarString = "";

        try {
            myCalendarString = Unirest.get(Config.instance.GOOGLE_ICAL_URL).asString().getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        StringReader sin = new StringReader(myCalendarString);

        CalendarBuilder builder = new CalendarBuilder();

        try {
            calendar = builder.build(sin);
        } catch (IOException | ParserException e) {
            e.printStackTrace();
        }
        loaded = true;
    }

    private String getSummary(Component component) {
        Property summaryProperty = component.getProperties().getProperty("SUMMARY");
        if (summaryProperty != null) {
            return summaryProperty.getValue();
        } else {
            return "";
        }
    }

    private List<SimpleCalEvent> getAllEventsOf(Component component, Period period) {
        List<SimpleCalEvent> eventList = new ArrayList<>();

        String summary = getSummary(component);

        // add recurrent Dates - Current Dates are with in
        PeriodList list = component.calculateRecurrenceSet(period);
        for (Period eventPeriod : list) {
            eventList.add(new SimpleCalEvent(summary, eventPeriod.getStart().getTime()));
        }

        return eventList;
    }

    void loadEvents() {
        loadEvents(DEFAULT_DAYS_TO_PREDICT);
    }

    private void loadEvents(int daysToPredict) {
        loadCalendar();

        List<SimpleCalEvent> simpleCalEvents = new ArrayList<>();

        for (CalendarComponent calendarComponent : calendar.getComponents()) {
            simpleCalEvents.addAll(getAllEventsOf(calendarComponent,
                    new Period(new DateTime(System.currentTimeMillis()),
                    new DateTime(System.currentTimeMillis() + daysToPredict * MILLIS_TO_DAYS))));
        }

        this.sortedCalEvents = simpleCalEvents.stream().sorted(new EventComparator()).filter(e -> e.after(LocalDateTime.now())).collect(Collectors.toList());
    }

    List<String> getEvents() {
        return getEvents(Config.instance.CALENDAR_UPCOMING_EVENT_COUNT);
    }

    private List<String> getEvents(int numEvents) {

        // wait until Events are loaded
        while (!loaded) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<String> eventList = new ArrayList<>();
        int length = min(numEvents, sortedCalEvents.size());
        if (numEvents == -1) {
            length = sortedCalEvents.size();
        }
        for (int i = 0; i < length; i++) {
            eventList.add(this.sortedCalEvents.get(i).toString());
        }
        return eventList;
    }

    CalendarDataHelper getPlaceholderDataHelper() {
        List<String> list = new ArrayList<>();
        list.add("Please Stand By");
        list.add("Fetching Data");
        return new CalendarDataHelper(FXCollections.observableArrayList(list));
    }
}