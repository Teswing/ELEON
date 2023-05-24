package com.teswing.eleon;

import android.app.Notification;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.i("Notification Listener", "Notification POSTED");
        Notification notification = sbn.getNotification();
        Icon icon = notification.getSmallIcon();
        Intent intent = new Intent(Constants.ACTION_NOTIFICATION_RECEIVER);
        intent.putExtra("Notification", sbn.getNotification());
        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
