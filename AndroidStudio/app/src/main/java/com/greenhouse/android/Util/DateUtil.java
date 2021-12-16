package com.greenhouse.android.Util;

import com.greenhouse.android.ViewModel.available_times;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date calculateStart(Date end, available_times interval, int noOfPoints) {
        Calendar c = Calendar.getInstance();
        c.setTime(end);
        switch (interval) {
            case minutes15:
                c.add(Calendar.MINUTE,-noOfPoints*15);
                break;
            case hours4:
                c.add(Calendar.HOUR,-noOfPoints*4);
                break;
            case days1:
                c.add(Calendar.DAY_OF_MONTH,-noOfPoints);
                break;
            case days7:
                c.add(Calendar.DAY_OF_MONTH,-noOfPoints*7);
                break;
            case months1:
                c.add(Calendar.MONTH,-noOfPoints);
                break;
            case months6:
                c.add(Calendar.MONTH,-noOfPoints*6);
                break;
            case years1:
                c.add(Calendar.YEAR,-noOfPoints);
                break;
        }

        return c.getTime();
    }
    public static Date calculateEnd(Date start, available_times interval, int noOfPoints) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        switch (interval) {
            case minutes15:
                c.add(Calendar.MINUTE,+noOfPoints*15);
                break;
            case hours4:
                c.add(Calendar.HOUR,+noOfPoints*4);
                break;
            case days1:
                c.add(Calendar.DAY_OF_MONTH,+noOfPoints);
                break;
            case days7:
                c.add(Calendar.DAY_OF_MONTH,+noOfPoints*7);
                break;
            case months1:
                c.add(Calendar.MONTH,+noOfPoints);
                break;
            case months6:
                c.add(Calendar.MONTH,+noOfPoints*6);
                break;
            case years1:
                c.add(Calendar.YEAR,+noOfPoints);
                break;
        }

        return c.getTime();
    }
}
