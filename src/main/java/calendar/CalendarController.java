package calendar;

import config.Config;
import interfaces.Controller;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.util.Duration;
import overview.AssistantMirror;

import java.util.ArrayList;
import java.util.List;

public class CalendarController implements Controller {
    @FXML
    public Label calendar;
    @FXML
    private ListView<String> listView = new ListView<>();

    private List<String> list = new ArrayList<>();
    private CalendarProvider provider = new CalendarProvider();
    private final CalendarDataHelper calendarDataHelper = new CalendarDataHelper();
    private ScheduledService<CalendarDataHelper> service;

    @Override
    public void init() {
        createBindings();
        setCustomFont();
    }

    private void setCustomFont() {
        calendar.setFont(Font.font(AssistantMirror.FONT_NAME, 30));
        listView.setCellFactory(cell -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setFont(Font.font(AssistantMirror.FONT_NAME, 25));
                    setPrefWidth(0);
                }
            }
        });
    }



    @Override
    public void startUpdate() {
        if (!Config.instance.SHOW_CALENDAR)
            return;
        service = new ScheduledService<CalendarDataHelper>() {
            @Override
            protected Task<CalendarDataHelper> createTask() {
                return new Task<CalendarDataHelper>() {

                    @Override
                    protected CalendarDataHelper call() {
                        provider.loadEvents();
                        list.clear();
                        list = provider.getEvents();
                        CalendarDataHelper calendarDataHelper;

                        if (!list.isEmpty())
                            calendarDataHelper = new CalendarDataHelper((FXCollections.observableArrayList(list)));
                        else
                            calendarDataHelper = provider.getPlaceholderDataHelper();

                        updateValue(calendarDataHelper);
                        return calendarDataHelper;
                    }
                };
            }
        };
        service.setPeriod(Duration.seconds(Config.instance.CALENDAR_SLEEP_SECONDS));
        service.start();

        service.setOnSucceeded(event -> {
            calendarDataHelper.reinitialize(service.getValue().getEvents());
        });

        init();
    }

    @Override
    public void stopRunning() {
        if (service != null) {
            if (service.isRunning()) {
                service.cancel();
            }
        }
    }

    @Override
    public void createBindings() {
        listView.itemsProperty().bind(calendarDataHelper.eventsProperty());
        calendarDataHelper.reinitialize(provider.getPlaceholderDataHelper().getEvents());
    }
}
