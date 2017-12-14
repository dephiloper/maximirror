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
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;

import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.Calendar;

import static java.lang.StrictMath.min;

public class jCalendarProvider {
    private Calendar calendar;
    private List<SimpleCalEvent> sortedCalEvents;
    public static final int DEFAULT_DAYS_TO_PREDICT = 7;
    public static final int DEFAULT_NUM_OF_EVENTS = 5;
    public static final long MILLIS_TO_DAYS = 1000*3600*24;

    private boolean loaded = false;

    public void loadCalendar() {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
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
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Period eventPeriod = (Period) iter.next();
            eventList.add(new SimpleCalEvent(summary, eventPeriod.getStart().getTime()));
        }

        return eventList;
    }

    public void loadEvents() {
        loadEvents(DEFAULT_DAYS_TO_PREDICT);
    }

    public void loadEvents(int daysToPredict) {
        loadCalendar();

        List<SimpleCalEvent> simpleCalEvents = new ArrayList<>();

        for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
            Component component = (Component) i.next();
            simpleCalEvents.addAll(getAllEventsOf(component, new Period(new DateTime(System.currentTimeMillis()), new DateTime(System.currentTimeMillis() + daysToPredict * MILLIS_TO_DAYS))));
        }

        this.sortedCalEvents = simpleCalEvents.stream().sorted(new EventComparator()).filter(e -> e.after(LocalDateTime.now())).collect(Collectors.toList());
    }

    public List<String> getEvents() {
        return getEvents(DEFAULT_NUM_OF_EVENTS);
    }

    public List<String> getEvents(int numEvents) {

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
}