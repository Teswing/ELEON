package com.teswing.eleon;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NotificationListViewModel extends AndroidViewModel {

    NotificationRepository repository;

    private LiveData<List<Notification>> notificationsLiveData;

    public NotificationListViewModel(Application application) {
        super(application);
        repository = new NotificationRepository(application);
        notificationsLiveData = repository.getNotifications();
    }

    public void insert(Notification notification) {
        repository.insert(notification);
    }
    public void update(Notification notification) {
        repository.update(notification);
    }
    public void delete(Notification notification) {
        repository.delete(notification);
    }
    public void deleteAll() {
        repository.deleteAll();
    }

    void printLog() {
        Log.e("ViewModel", "Log PRINTED");
    }

    LiveData<List<Notification>> getNotificationsLiveData() {
        if (notificationsLiveData == null) {
            notificationsLiveData = new MutableLiveData<>();
        }
        return notificationsLiveData;
    }

}
