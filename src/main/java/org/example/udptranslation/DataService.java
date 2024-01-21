package org.example.udptranslation;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Класс сервиса, который заполняет объект данных требуемой информацией
 */

@Component
public class DataService {

    /**
     * Метод заполняет объект модели данных
     *
     * @param json объект модели данных
     *
     * @return String с описанием данных в заданном формате
     */
    public static String fillData(JSONObject json) throws JSONException {

        json.put("volume",Audio.getMasterOutputVolume());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ssXXX");
        json.put("time", OffsetDateTime.now().format(dtf));

        Locale locale = Locale.getDefault();
        String lang = locale.getISO3Language().substring(0,2).toUpperCase();
        json.put("language",lang);

        return json.toString();
    }
}
