package com.teswing.eleon;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

/*Note: There is normally no need to subclass Application. In most situations, static singletons can provide the same functionality
in a more modular way. If your singleton needs a global context (for example to register broadcast receivers),
 include Context.getApplicationContext() as a Context argument when invoking your singleton's getInstance() method.*/
// Прослушивает уведомления даже если его остановить, я создал монстра
public class PreLoaderApp extends Application {

    public static final String NF_CHANNEL_ID = "ServiceChannel";

//    NotificationReceiver notificationReceiver = new NotificationReceiver();


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
//        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_NOTIFICATION_RECEIVER);
//        Log.i("APP", "Receiver registered");
//        registerReceiver(notificationReceiver, intentFilter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        unregisterReceiver(notificationReceiver);
        Log.i("APP", "Receiver unregistered");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NF_CHANNEL_ID,
                    Constants.NF_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
