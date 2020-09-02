package JavaSE;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {
    public static final int MILLISECONDS_PER_SECONDE = 1000;
    public static final int MILLISECONDS_PER_MINUTE = 60000;
    public static final int MILLISECONDS_PER_HOUR = 3600000;
    public static final long MILLISECONDS_PER_DAY = 86400000L;
    public static final String DATE_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MILLS_TIME = "yyyy-MM-dd HH:mm:ss.SSS";

    public DateUtils() {
    }

    public static int getDelayToNextMinute(long rightnow) {
        return (int)(60000L - rightnow % 60000L);
    }

    public static long getPreMinuteMills(long rightnow) {
        return rightnow - rightnow % 60000L - 1L;
    }

    public static String dateToStr(Date date) {
        return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String dateToStr(Date date, String format) {
        return (new SimpleDateFormat(format)).format(date);
    }

    public static Date strToDate(String dateStr) throws ParseException {
        return strToDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date strToDate(String dateStr, String format) throws ParseException {
        return (new SimpleDateFormat(format)).parse(dateStr);
    }

    public static String dateToMillisStr(Date date) {
        return dateToStr(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static Date millisStrToDate(String millisDateStr) throws ParseException {
        return strToDate(millisDateStr, "yyyy-MM-dd HH:mm:ss.SSS");
    }
}
