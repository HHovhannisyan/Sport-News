package com.example.sportNewsAPI;


import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {


    public static String DateToTimeFormat(String oldStringDate) {
        PrettyTime p = new PrettyTime(new Locale(getCountry()));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.getDefault());
            Date date = sdf.parse(oldStringDate);

            Calendar calendar = Calendar.getInstance();
            assert date != null;
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 4);
            isTime = p.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isTime;
    }

    public static String getCountry() {
        Locale locale = Locale.getDefault();
        return locale.getCountry();
    }
}