package com.aa.notice.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cc on 2017/3/20.
 */

public class DateUtil {
    private static final String TAG = "DateUtil";
    public static String getTime(long time) {
        return getTime(time, "M.d H:mm");
    }

    public static String getTime(long time, String pattern) {
        SimpleDateFormat sdg = new SimpleDateFormat(pattern);
        return sdg.format(new Date(time));
    }

//    public static String getFullTimeNoSec(long time) {
//        return getTime(time, "yyyy.MM.dd H:mm");
//    }
//
//    public static String getFullTime(long time) {
//        return getTime(time, "yyyy.MM.dd H:mm");
//    }
//
//    public static String getFullDay(long time) {
//        return getTime(time, "yyyy.MM.dd");
//    }

    public static String getDay(long time) {
        Log.d(TAG,"时间戳"+time);
        if (getTargetYear(time) == getCurrentYear()){
            return getTime(time,"MM.dd");
        }else {
            return getTime(time,"yyyy.MM.dd");
        }
    }
    public static String getMin(long time) {
        Log.d(TAG,"时间戳"+time);
            return getTime(time,"yyyyMMddHHmmss");
    }
    private static int getTargetYear(long time){
        int year = 0;
        try {
           Date date =  new SimpleDateFormat("yyyy.MM.dd").parse(getTime(time,"yyyy.MM.dd"));
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(date);
           year = calendar.get(Calendar.YEAR);
            Log.d(TAG,"指定年份:"+year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return year;
    }
    private static int getCurrentYear() {
        Date date = new Date();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Log.d(TAG,"当前年份:"+year);
        return year;
    }
//
//    public static String getSimpleTime(long time) {
//        SimpleDateFormat sdg = new SimpleDateFormat("yyyyMMdd");
//        return sdg.format(new Date(time));
//    }
//
//    public static String getPublishTime(long time) {
//
//        String dataString = "";
//
//        Date currentTime = new Date(time);
//        Calendar todayStart = Calendar.getInstance();
//        todayStart.set(Calendar.HOUR_OF_DAY, 0);
//        todayStart.set(Calendar.MINUTE, 0);
//        todayStart.set(Calendar.SECOND, 0);
//        todayStart.set(Calendar.MILLISECOND, 0);
//        Date todaybegin = todayStart.getTime();
//        Date yesterdaybegin = new Date(todaybegin.getTime() - 3600 * 24 * 1000);
//        Date preyesterday = new Date(yesterdaybegin.getTime() - 3600 * 24 * 1000);
//
//        if (!currentTime.before(todaybegin)) {
//            dataString = "今天";
//        } else if (!currentTime.before(yesterdaybegin)) {
//            dataString = "昨天";
//        } else if (!currentTime.before(preyesterday)) {
//            dataString = "前天";
//        } else {
//            SimpleDateFormat dateformatter = new SimpleDateFormat("M月d日", Locale.getDefault());
//            dataString = dateformatter.format(currentTime);
//        }
//
//
//        return dataString;
//    }
//
//    public static String getPublishTime(long time, String formatter) {
//
//        String dataString = "";
//
//        Date currentTime = new Date(time);
//        Calendar todayStart = Calendar.getInstance();
//        todayStart.set(Calendar.HOUR_OF_DAY, 0);
//        todayStart.set(Calendar.MINUTE, 0);
//        todayStart.set(Calendar.SECOND, 0);
//        todayStart.set(Calendar.MILLISECOND, 0);
//        Date todaybegin = todayStart.getTime();
//        Date yesterdaybegin = new Date(todaybegin.getTime() - 3600 * 24 * 1000);
//        Date preyesterday = new Date(yesterdaybegin.getTime() - 3600 * 24 * 1000);
//
//        if (!currentTime.before(todaybegin)) {
//            dataString = "今天";
//        } else if (!currentTime.before(yesterdaybegin)) {
//            dataString = "昨天";
//        } else if (!currentTime.before(preyesterday)) {
//            dataString = "前天";
//        } else {
//            SimpleDateFormat dateformatter = new SimpleDateFormat(formatter, Locale.getDefault());
//            dataString = dateformatter.format(currentTime);
//        }
//
//
//        return dataString;
//    }
//
//    public static String getMDTime(long time) {
//        Date currentTime = new Date(time);
//        SimpleDateFormat dateformatter = new SimpleDateFormat("M月d日", Locale.getDefault());
//        return dateformatter.format(currentTime);
//    }
//
//    public static String getYTime(long time) {
//        Date currentTime = new Date(time);
//        SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy年", Locale.getDefault());
//        return dateformatter.format(currentTime);
//    }

}
