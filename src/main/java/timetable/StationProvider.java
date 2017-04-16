package timetable;

import com.google.common.base.Strings;
import config.Config;
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

class StationProvider {

    private List<String> transports;
    private String stationName;
    private int counter = 0;

    Station fetchStation() {
        if (counter == Config.instance.TRANSPORT_STATIONS.length)
            counter = 0;
        else
            counter++;

        if (Strings.isNullOrEmpty(Config.instance.TRANSPORT_STATIONS[counter].ID)) return null;

        try {

                Document doc = Jsoup.connect(String.format("http://fahrinfo.bvg.de/Fahrinfo/bin/stboard.bin/dox?ld=0.1&&input=%s&boardType=depRT&start=yes", Config.instance.TRANSPORT_STATIONS[counter].ID)).get();
            stationName = doc.getElementsByTag("strong").first().text();

            if (Strings.isNullOrEmpty(stationName))
            {
                System.err.println(String.format("Station %s does not exist.",  Config.instance.TRANSPORT_STATIONS[counter].ID));
                return null;
            }

            Element table = doc.select("table").get(0); //select the first table.
            Elements rows = table.select("tr");

            transports = new ArrayList<>();

            for (Element row : rows) {
                if (row.getElementsByTag("strong").size() > 0) {
                    Transport transport = new Transport();
                    LocalTime time = LocalTime.parse(row.getElementsByTag("strong").get(0).text().replace(" *",""));
                    long arrivalTime = ChronoUnit.MINUTES.between(LocalTime.now(),time);
                    String lineName = row.getElementsByTag("strong").get(1).text().replace("Tra ", "");
                    boolean isLineNameMatching = Arrays.asList(Config.instance.TRANSPORT_STATIONS[counter].LINE_NAME_FILTER).contains(lineName);

                    if (Config.instance.TRANSPORT_STATIONS[counter].LINE_NAME_FILTER.length == 0 ||
                            Config.instance.TRANSPORT_STATIONS[counter].LINE_NAME_FILTER[0].equals("ALL") ||
                            isLineNameMatching) {
                        if (arrivalTime > Config.instance.TRANSPORT_STATIONS[counter].WALK_DURATION_MINUTES) {
                            transport.setArrivalTime(arrivalTime);
                            transport.setTime(time);
                            transport.setLineName(lineName);
                            transport.setDirection(row.getElementsByTag("td").get(2).text());
                            transports.add(transport.toString());
                        }
                    }
                }
            }

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new Station(stationName, transports.subList(0, Config.instance.TIMETABLE_UPCOMING_TRANSPORT_COUNT));
    }
}
