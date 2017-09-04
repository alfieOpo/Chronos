package pupthesis.chronos.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ALFIE on 7/20/2017.
 */

public class Config {
public static boolean islastpage=false;
    public static  String Date(){
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);
        String time = hour + ":" + minutes + ":" + seconds;


        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);
        String date = year + "/" + month + "/" +day ;
        return  date;
    }
    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
    public static  boolean ValidDate(String Start,String End) throws ParseException {
        Date startdate = null;
        Date enddate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        startdate = sdf.parse(Start);
        enddate = sdf.parse(End);
        if(startdate.compareTo(enddate)== -1){ //0 comes when two date are same,
            //1 comes when date1 is higher then date2
            //-1 comes when date1 is lower then date2
            return  true;
        }
        return  false;
    }
    public  static Date toDate(String strThatDay){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy,MM,dd");
        Date d = null;
        try {
            d = formatter.parse(strThatDay);//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  d;

    }
    public static  long days(String startDate,String endDate){

        Calendar startdate= Calendar.getInstance();
        startdate.setTime(toDate(startDate));
        Calendar enddate= Calendar.getInstance();
        enddate.setTime(toDate(endDate));
        long diff = enddate.getTimeInMillis() - startdate.getTimeInMillis();
        long day= diff / (24 * 60 * 60 * 1000);
        return day;
    }
    public static  String  TAG="ALFIEOPO";
    public  static  String Letters[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
}
