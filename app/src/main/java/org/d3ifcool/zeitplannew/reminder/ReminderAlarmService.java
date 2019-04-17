package org.d3ifcool.zeitplannew.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import org.d3ifcool.zeitplannew.MainActivity;
import org.d3ifcool.zeitplannew.R;
import org.d3ifcool.zeitplannew.data.JadwalContract;

import static org.d3ifcool.zeitplannew.App.CHANNEL_1_ID;
import static org.d3ifcool.zeitplannew.FragmentSetting.SHARED_PREFS;
import static org.d3ifcool.zeitplannew.FragmentSetting.SWITCH_NOTIFICATION;

public class ReminderAlarmService extends IntentService {

    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private boolean switchOnOff;

    private static final int NOTIFICATION_ID = 42;
    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        Uri uri = intent.getData();

        //Display a notification to view the task details
        Intent action = new Intent(this, MainActivity.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SWITCH_NOTIFICATION,false);

        //Grab the task description
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = "Saatnya Kuliah "+JadwalContract.getColumnString(cursor, JadwalContract.JadwalEntry.COLUMN_MATAKULIAH);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Notification note = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setContentTitle(description)
                .setContentText(getString(R.string.notification_title))
                .setSmallIcon(R.drawable.ic_insert_invitation_black_24dp)
                .setContentIntent(operation)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();
//        manager.notify(NOTIFICATION_ID, note);
        if (switchOnOff == true){
            manager.notify(NOTIFICATION_ID, note);
        }
    }
}