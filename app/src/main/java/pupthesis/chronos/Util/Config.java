package pupthesis.chronos.Util;

import java.util.Calendar;

/**
 * Created by ALFIE on 7/20/2017.
 */

public class Config {
    public static  String PROJECTID="0";
    public static  String PROJECTNAME="N/A";
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



}
