package com.teswing.eleon;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Notification.class}, version = 1, exportSchema = false)
public abstract class NotificationDatabase extends RoomDatabase {

    private static NotificationDatabase instance;

    public abstract NotificationDAO notificationDAO();

    public static synchronized NotificationDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NotificationDatabase.class, "notification_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                instance.notificationDAO().insert(new Notification("Arti1", "Test message 1"));
                instance.notificationDAO().insert(new Notification("Arti2", "Test message 2"));
            });
        }
    };
}
