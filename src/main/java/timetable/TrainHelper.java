package timetable;

import com.google.common.base.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phil on 23.03.17.
 */
public class TrainHelper {

    public static String TRAIN_LOCATION_ID;
    private List<String> trains;

    List<String> getTrains(int length) {
        if (Strings.isNullOrEmpty(TRAIN_LOCATION_ID)) return null;

        try {

            Document doc = Jsoup.connect("http://fahrinfo.bvg.de/Fahrinfo/bin/stboard.bin/dox?ld=0.1&&input=" + TRAIN_LOCATION_ID + "&boardType=depRT&start=yes").get();
            Element table = doc.select("table").get(0); //select the first table.
            Elements rows = table.select("tr");

            trains = new ArrayList<>();

            for (Element row : rows) {
                if (row.getElementsByTag("strong").size() > 0) {
                    Train train = new Train();
                    train.setTime(LocalTime.parse(row.getElementsByTag("strong").get(0).text().replace(" *","")));
                    train.setName(row.getElementsByTag("strong").get(1).text());
                    train.setDirection(row.getElementsByTag("td").get(2).text());
                    trains.add(train.toString());
                }
            }

            System.out.println(trains.size());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return trains.subList(0, length);
    }
}
