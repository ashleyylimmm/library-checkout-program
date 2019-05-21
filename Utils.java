package com.deanza.mstrclib;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static String GetDt13() {
        Date now =  Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("YYMMdd_HHmmss");
        String dt13 = dateFormat.format(now);
        return dt13;
    }

    public static String GetDt6() {
        String dt13 = GetDt13();
        return dt13.substring(0, 6);
    }

    public static void toastit(Context c, String str) {
        Toast.makeText(c, str, Toast.LENGTH_SHORT).show();
    }

    public static String GetStudentID(String str)
    {
        if (Is8Digits(str))
            return str;
        str = ExtracDASBID(str);
        return str;
    }

    private static String ExtracDASBID(String str)
    {
        if (!str.toLowerCase().contains("dasb"))
            return "";

        String extracedID = str.substring(1, 9);
        if(Is8Digits(extracedID))
            return extracedID;
        else
            return "";
    }

    public static boolean Is8Digits(String str) {
        str = str.trim();
        if(str.length() != 8)
            return false;
        return str.matches("\\d+");
    }

    public static String GetTerm(String dt6) {
        return GetTerm_Early(dt6, 0);
    }

    public static String GetTerm_Early(String dt6, int daysEarly)
    {
        int yr = Integer.parseInt(dt6.substring(0, 2));
        int monthday = Integer.parseInt(dt6.substring(2, 6));

        int winterEnd = 331 - daysEarly;
        int springEnd = 630 - daysEarly;
        int summerEnd = 831 - daysEarly;
        int fallEnd = 1231 - daysEarly;

        String str = "";
        if (monthday <= winterEnd)
            str = "Winter";
        else if (monthday <= springEnd)
            str = "Spring";
        else if (monthday <= summerEnd)
            str = "Summer";
        else if (monthday <= fallEnd)
            str = "Fall";
        else
        {
            str = "Winter";     // for the last few days of the year
            yr += 1;
        }
        return str + yr;
    }
}
