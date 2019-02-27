import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dylan on 2/26/2019.
 */
public class timeConvert {
    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(date);
        } catch (ParseException e) {
            System.out.println("error DATE invalid");
            return null;
        }
    }

    public static String dayStringFormat(long msecs) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(msecs);
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dow) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
        }
        return "Unknown";
    }

    public static String msToMonth(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        int mMonth = calendar.get(Calendar.MONTH);
        return getMonth(mMonth);
    }

    public static String time(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        int mHour = calendar.get(Calendar.HOUR);
        int amPM =calendar.get(Calendar.AM_PM);
        String amPmS = "AM";
        String padH = "";
        if(amPM ==1){
            amPmS = "PM";
        }
        if(amPM ==1 && mHour!=12){
            mHour+=12;
            amPmS = "PM";
        }
        if(mHour<10){
            padH = "0";
        }
        else{
            padH="";
        }
        return padH+mHour+":"+"00"+":"+"00"+""+amPmS;
    }

    public static String date(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        String pad = "";
        String padD = "";
        if(mMonth+1<10){pad="0";}
        if(mDay<10){padD="0";}
        return pad+(mMonth+1)+"/"+padD+mDay+"/"+mYear;
    }
}
