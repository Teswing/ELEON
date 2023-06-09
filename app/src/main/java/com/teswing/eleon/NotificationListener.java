package com.teswing.eleon;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "Notification Listener";

    NotificationListViewModel notificationListViewModel;


    // Пока на основном потоке
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if (!"ranker_group".equals(sbn.getTag())) {
            Notification notification = sbn.getNotification();
            Bundle extras = notification.extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            String text = extras.getString(Notification.EXTRA_TEXT);
            notificationListViewModel.insert(new com.teswing.eleon.Notification(title, text));
            Log.e(TAG, "Notification POSTED");
//            Log.i(TAG, "Title: \n" + title);
//            Log.i(TAG, "Message: \n " + text);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        notificationListViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(NotificationListViewModel.class);
        Log.i(TAG, "NOTIFICATION LISTENER CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Listener Destroyed");
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
}
