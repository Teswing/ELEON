package com.teswing.eleon;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "Notification Listener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        Bundle extras = notification.extras;
        String title = extras.getString(android.app.Notification.EXTRA_TITLE);
        String text = extras.getString(android.app.Notification.EXTRA_TEXT);
        String icon = extras.getString(Notification.EXTRA_SMALL_ICON);
        Log.e(TAG, "Notification POSTED");
        try {
            final PackageManager pm = getApplicationContext().getPackageManager();
            Log.i(TAG, "App: \n" + (pm.getApplicationLabel(pm.getApplicationInfo(sbn.getPackageName(), 0))));
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "App: \n" + sbn.getPackageName());
        }
        Log.i(TAG, "Title: \n" + title);
        Log.i(TAG, "Message: \n " + text);
        Log.i(TAG, "Icon: \n " + icon);
        Log.i(TAG, "Time: \n " + sbn.getNotification().when);
//        Icon icon = notification.getSmallIcon();
//        Intent intent = new Intent(Constants.ACTION_NOTIFICATION_RECEIVER);
//        intent.putExtra("Notification", sbn.getNotification());
//        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

/*    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Listener STARTED not connected ?");

        Intent switchIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                switchIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, NF_CHANNEL_ID)
                .setContentTitle("Notification Listener")
                .setContentText("I'm listening")
                .setSmallIcon(R.drawable.baseline_remove_red_eye_24)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.baseline_remove_red_eye_24, "Butoon", pendingIntent)
                .setOngoing(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        notificationManager.notify(1, notification);
        startForeground(1, notification);

        return START_NOT_STICKY;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "NOTIFICATION LISTENER CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Listener Destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
