package com.teswing.eleon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

class NotificationReceiver extends BroadcastReceiver {
        // Малый спектр поддерживаемых устройств, дополнить
        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        @Override
        public void onReceive(Context context, Intent intent) {
            android.app.Notification notification = intent.getParcelableExtra("Notification", android.app.Notification.class);
            Bundle extras = notification.extras;
            String title = extras.getString(android.app.Notification.EXTRA_TITLE);
            String text = extras.getString(android.app.Notification.EXTRA_TEXT);
            Notification itemNotification = new Notification(title, text);
            Log.e("Notification Receiver", "Message received:\n" + title + "\n" + text);
        }
    }
