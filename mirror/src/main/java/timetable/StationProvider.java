package timetable;

import com.google.common.base.Strings;
import config.Config;
import interfaces.Provider;
import javafx.collections.FXCollections;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

class StationProvider implements Provider<Station> {
    private static final String BVG_URI = "http://fahrinfo.bvg.de/Fahrinfo/bin/stboard.bin/dox?ld=0.1&&input=%s&boardType=depRT&start=yes";
    private final Station[] stations;
    private String stationName;
    private int currentFetchedStationIndex = 0;
    private int providedStationIndex = 0;
    private int stationCount = Config.instance.TRANSPORT_STATIONS.length;
    private boolean isRunning = true;

    void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    StationProvider() {
        stations = new Station[stationCount];
        fetchStationsCyclical();
    }

    void fetchStationsCyclical() {
        new Thread(() -> {
            while (isRunning) {
                String currentStationId = Config.instance.TRANSPORT_STATIONS[currentFetchedStationIndex].ID;
                if (Strings.isNullOrEmpty(currentStationId)) return;

                try {
                    Document doc = Jsoup.connect(String.format(BVG_URI, currentStationId)).get();
                    stationName = doc.getElementsByTag("strong").first().text();

                    if (Strings.isNullOrEmpty(stationName))
                    {
                        System.err.println(String.format("Station %s does not exist.",  currentStationId));
                        return;
                    }

                    Element table = doc.select("table").get(0); //select the first table.
                    Elements rows = table.select("tr");

                    List<String> transports = new ArrayList<>();
                    for (Element row : rows) {
                        if (row.getElementsByTag("strong").size() > 0) {
                            Transport transport = new Transport();
                            LocalTime time = LocalTime.parse(row.getElementsByTag("strong").get(0).text().replace(" *",""));
                            long arrivalTime = ChronoUnit.MINUTES.between(LocalTime.now(),time);
                            String lineName = row.getElementsByTag("strong").get(1).text().replace("Tram ", "");
                            String[] lineNameFilters = Config.instance.TRANSPORT_STATIONS[currentFetchedStationIndex].LINE_NAME_FILTER;
                            boolean isLineNameMatching = Arrays.asList(lineNameFilters).contains(lineName);

                            if (lineNameFilters.length == 0 ||
                                    Arrays.asList(lineNameFilters).contains("ALL") ||
                                    isLineNameMatching) {
                                if (arrivalTime > Config.instance.TRANSPORT_STATIONS[currentFetchedStationIndex].WALK_DURATION_MINUTES) {
                                    transport.setTime(time);
                                    transport.setLineName(lineName);
                                    transport.setDirection(row.getElementsByTag("td").get(2).text());
                                    transports.add(transport.toString());
                                }
                            }
                        }
                    }
                    synchronized(stations) {
                        if (transports.size() > Config.instance.TIMETABLE_UPCOMING_TRANSPORT_COUNT)
                            transports = transports.subList(0, Config.instance.TIMETABLE_UPCOMING_TRANSPORT_COUNT);
                        stations[currentFetchedStationIndex] = new Station(stationName, transports);
                    }
                }
                catch (IOException e) {
                    System.err.println(e.getMessage());
                }

                if (currentFetchedStationIndex == stationCount-1) {
                    try {
                        TimeUnit.MINUTES.sleep(1);
                    }
                    catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                }

                currentFetchedStationIndex = (currentFetchedStationIndex+1) % stationCount;
            }
        }).start();
    }

    StationDataHelper getPlaceholderDataHelper() {
        return new StationDataHelper(FXCollections.observableArrayList(
                "Fetching Data","Fetching Data", "Fetching Data"),"Please Stand By");
    }

    @Override
    synchronized public Station provideData() {
        Station providedStation = stations[providedStationIndex];
        providedStationIndex = (providedStationIndex+1) % stationCount;
        return providedStation;
    }
}
