package pupthesis.chronos.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Util.Config;
import pupthesis.chronos.Activity.NavigationActivity;
import pupthesis.chronos.R;

/**
 * Created by ALFIE on 7/22/2017.
 */

public class RSSPullService extends  Service {
    int maxcount=0;



    public void showNotification() {


        Intent intent = new Intent(this, NavigationActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle("Update Your Task")
                .setContentText("Chronos")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pIntent)
                .setAutoCancel(true) .build();
        NotificationManager notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        DataBaseHandler da=new DataBaseHandler(getApplicationContext());
        Cursor cursor=da.getLIST("select * from gant_task order by _id desk");
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                String task_name = cursor.getString(cursor.getColumnIndex("task_name"));
                String percent_complete = cursor.getString(cursor.getColumnIndex("percent_complete"));
                String end_date = cursor.getString(cursor.getColumnIndex("end_date"));
                String start_date = cursor.getString(cursor.getColumnIndex("start_date"));
                String date= Config.Date();
                if(start_date.replace(",","/").equals(Config.Date())){

                    showNotification();
                }


                i = cursor.getPosition() + 1;

            } while (cursor.moveToNext());
        }



        return null;
    }
}
