package com.teswing.eleon;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificationRepository {

    private NotificationDAO notificationDAO;
    private LiveData<List<Notification>> notifications;
    // последовательно выполняет запросы в одном потоке
    private Executor executor = Executors.newSingleThreadExecutor();

    public NotificationRepository(Application application) {
        NotificationDatabase database = NotificationDatabase.getInstance(application);
        notificationDAO = database.notificationDAO();
        notifications = notificationDAO.getAll();
    }
    public void insert(Notification notification) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notificationDAO.insert(notification);
            }
        });
    }

    public void update(Notification notification) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notificationDAO.update(notification);
            }
        });
    }

    public void delete(Notification notification) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notificationDAO.delete(notification);
            }
        });
    }

    public void deleteAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notificationDAO.deleteAll();
            }
        });
    }

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

}
