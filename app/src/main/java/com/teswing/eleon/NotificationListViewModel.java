package com.teswing.eleon;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.teswing.eleon.workers.LoadWorker;

import java.util.List;

public class NotificationListViewModel extends AndroidViewModel {

    WorkManager workManager;

    public static MutableLiveData<List<Notification>> notificationsLiveData;

    private LiveData<List<WorkInfo>> savedWorkInfo;

    public NotificationListViewModel(Application application) {
        super(application);
        workManager = WorkManager.getInstance(application);
//        savedWorkInfo = workManager.getWorkInfosByTagLiveData(Constants.TAG_REPOSITORY);
    }

    void loadData() {
//        Log.e("Load DATA", "LOAD DATA CALLED");
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoadWorker.class)
                .addTag(Constants.TAG_REPOSITORY)
                .build();
        WorkContinuation requestUnique
                = workManager.beginUniqueWork("Loading Data", ExistingWorkPolicy.REPLACE, request);
        requestUnique.enqueue();
    }

    void printLog() {
        Log.e("ViewModel", "Log PRINTED");
    }


    LiveData<List<WorkInfo>> getSavedWorkInfo() {
        return savedWorkInfo;
    }

    LiveData<List<Notification>> getNotificationsLiveData() {
        if (notificationsLiveData == null) {
            notificationsLiveData = new MutableLiveData<>();
        }
        return notificationsLiveData;
    }

}
