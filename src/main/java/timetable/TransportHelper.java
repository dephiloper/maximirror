package timetable;

import com.google.common.base.Strings;
import config.Config;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class TransportHelper {

    private List<String> transports;

    List<String> getTransports() {
        if (Strings.isNullOrEmpty(Config.instance.TRAIN_STATIONS[0])) return null;

        try {

            Document doc = Jsoup.connect(String.format("http://fahrinfo.bvg.de/Fahrinfo/bin/stboard.bin/dox?ld=0.1&&input=%s&boardType=depRT&start=yes", Config.instance.TRAIN_STATIONS[0])).get();
            Element table = doc.select("table").get(0); //select the first table.
            Elements rows = table.select("tr");

            transports = new ArrayList<>();

            for (Element row : rows) {
                if (row.getElementsByTag("strong").size() > 0) {
                    Transport transport = new Transport();
                    transport.setTime(LocalTime.parse(row.getElementsByTag("strong").get(0).text().replace(" *","")));
                    transport.setName(row.getElementsByTag("strong").get(1).text());
                    transport.setDirection(row.getElementsByTag("td").get(2).text());
                    transports.add(transport.toString());
                }
            }

            System.out.println(transports.size());
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return transports.subList(0, Config.instance.TIMETABLE_UPCOMING_TRANSPORT_COUNT);
    }
}
