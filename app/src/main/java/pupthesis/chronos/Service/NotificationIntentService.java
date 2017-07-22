package pupthesis.chronos.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Activity.Gantt;
import pupthesis.chronos.Util.Config;
import pupthesis.chronos.Activity.NavigationActivity;
import pupthesis.chronos.R;

/**
 * Created by ALFIE on 7/22/2017.
 */

public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {

                DataBaseHandler da=new DataBaseHandler(getApplicationContext());
                Cursor cursor=da.getLIST("select * from gant_task  _id desc");
                int i=0;
                try {
                    if (cursor.moveToFirst()) {
                        do {

                            String start_date = cursor.getString(cursor.getColumnIndex("start_date"));

                            if (start_date.replace(",", "/").equals(Config.Date())) {

                                processStartNotification(cursor.getCount());
                            }


                            i = cursor.getPosition() + 1;

                        } while (cursor.moveToNext());
                    }

                }catch (Exception xx){return;}

            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification(int i) {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("CHRONOS")
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.AppbarColor))
                .setContentText("You have "+1+" task need to process")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.circlelogo);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                new Intent(this, Gantt.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}