package calendar;

public class EventComparator implements java.util.Comparator<SimpleCalEvent>
{
    @Override
    public int compare(SimpleCalEvent e0, SimpleCalEvent e1) {
        if (e0.before(e1))
            return -1;
        if (e1.before(e0))
            return 1;
        return 0;
    }
}