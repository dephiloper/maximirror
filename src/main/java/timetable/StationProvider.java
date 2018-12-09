package timetable;

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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class StationProvider implements Provider<Station> {
    private static final String BVG_URI = "http://fahrinfo.bvg.de/Fahrinfo/bin/stboard.bin/dox?ld=0.1&&input=%s&boardType=depRT&start=yes";
    private final Station[] stations;
    private String stationName;
    private int providedStationIndex = 0;
    private String ignoredStationName = "Insert ID";
    private int stationCount = 0;

    StationProvider() {
        stationCount = getValidStationCount(Config.instance.TRANSPORT_STATIONS);
        stations = new Station[stationCount];
        fetchStationsCyclical();
    }

    private void fetchStationsCyclical() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(() -> {
            for (int i = 0; i < stationCount; i++) {
                String currentStationId = Config.instance.TRANSPORT_STATIONS[i].ID;
                if (currentStationId.equals("") || currentStationId.equals(ignoredStationName)) return;

                try {
                    Document doc = Jsoup.connect(String.format(BVG_URI, currentStationId)).get();
                    stationName = doc.getElementsByTag("strong").first().text();

                    if (stationName.equals("")) {
                        System.err.println(String.format("Station %s does not exist.", currentStationId));
                        return;
                    }

                    Element table = doc.select("table").get(0); //select the first table.
                    Elements rows = table.select("tr");

                    List<String> transports = new ArrayList<>();
                    for (Element row : rows) {
                        if (row.getElementsByTag("strong").size() > 0) {
                            Transport transport = new Transport();

                            LocalTime time = LocalTime.parse(row.getElementsByAttributeValueEnding("class", "Deptime")
                                    .get(0)
                                    .text()
                                    .replace(" *", ""));

                            long arrivalTime = ChronoUnit.MINUTES.between(LocalTime.now(), time);

                            String lineName = row.getElementsByAttributeValueContaining("class", "ivu-route")
                                    .get(0)
                                    .text()
                                    .replace("Tram ", "");

                            String[] lineNameFilters = Config.instance.TRANSPORT_STATIONS[i].LINE_NAME_FILTER;

                            boolean isLineNameMatching = Arrays.asList(lineNameFilters).contains(lineName);

                            if (lineNameFilters.length == 0 ||
                                    Arrays.asList(lineNameFilters).contains("ALL") ||
                                    isLineNameMatching) {
                                if (arrivalTime > Config.instance.TRANSPORT_STATIONS[i].WALK_DURATION_MINUTES) {
                                    transport.setTime(time);
                                    transport.setLineName(lineName);
                                    transport.setDirection(row.getElementsByTag("td").get(2).text());
                                    transports.add(transport.toString());
                                }
                            }
                        }
                    }

                    synchronized (stations) {
                        if (transports.size() > Config.instance.TIMETABLE_UPCOMING_TRANSPORT_COUNT)
                            transports = transports.subList(0, Config.instance.TIMETABLE_UPCOMING_TRANSPORT_COUNT);
                        stations[i] = new Station(stationName, transports);

                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }, 0,1, TimeUnit.MINUTES);
    }

    @Override
    synchronized public Station provideData() {
        Station providedStation = stations[providedStationIndex];
        providedStationIndex = (providedStationIndex+1) % stationCount;
        return providedStation;
    }

    private int getValidStationCount(Config.TransportStation[] transport_stations) {
        int validStationCount = 0;
        for (Config.TransportStation station : transport_stations) {
            if (!station.ID.equals("") && !station.ID.equals(ignoredStationName)) {
                validStationCount++;
            }
        }

        return validStationCount;
    }
}
