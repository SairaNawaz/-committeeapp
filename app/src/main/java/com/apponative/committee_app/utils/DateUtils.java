package com.apponative.committee_app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Muhammad Waqas on 5/24/2017.
 */

public class DateUtils {

    public static String getDateInString(Date date) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        String strDt = simpleDate.format(date);
        return strDt;
    }

    public static Date getStringInDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertedDate;
    }

    public static String getStringInFormatedDateString(String date){
        return getDateInString(getStringInDate(date));
    }
    public static String getDateInFormatedString(Date date) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
        String strDt = simpleDate.format(date);
        return strDt;
    }

    public static String addDaysToDate(Date date, int days, int turnCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (days == 1) {
            calendar.add(Calendar.DATE, turnCount);
        } else if (days == 7) {
            calendar.add(Calendar.WEEK_OF_MONTH, turnCount);
        } else if (days == 30) {
            calendar.add(Calendar.MONTH, turnCount);
        } else if (days == 365) {
            calendar.add(Calendar.YEAR, turnCount);
        }
        String str_date = getDateInString(calendar.getTime());
        return str_date;
    }

    public static Date getDateInFormat(Date date) {
        return getStringInDate(getDateInString(date));
    }

    public static int getDaysTillNow(String date) {
        Date datestart = getStringInDate(date);
        Date curDate = getDateInFormat(new Date());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(curDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(datestart);

        long msDiff = calendar1.getTimeInMillis() - calendar2.getTimeInMillis();

        return (int) TimeUnit.MILLISECONDS.toDays(msDiff);
    }

    public static int turnStatus(String cDate, int interval) {
        Date turnDate = getStringInDate(cDate);
        Date curDate = getDateInFormat(new Date());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(curDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(turnDate);

        if (turnDate.before(curDate)) {
            return Constants.PAST;
        } else {
            if (interval == 1) {
                if (calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE)) {
                    return Constants.CURRENT;
                } else {
                    return Constants.FUTURE;
                }
            } else if (interval == 7) {
                if (calendar1.get(Calendar.WEEK_OF_MONTH) == calendar2.get(Calendar.WEEK_OF_MONTH)) {
                    return Constants.CURRENT;
                } else {
                    return Constants.FUTURE;
                }
            } else {
                if (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
                    return Constants.CURRENT;
                } else {
                    return Constants.FUTURE;
                }
            }
        }

    }
}
