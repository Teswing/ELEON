package com.teswing.eleon.workers;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teswing.eleon.Notification;
import com.teswing.eleon.NotificationListViewModel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoadWorker extends Worker {

    public LoadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private final String TAG = "LoadWorker";

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "I'm STARTED");
        Context applicationContext = getApplicationContext();
        Uri repUri = WorkUtils.getRepository(applicationContext);
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(repUri.getPath());
            Type notificationType = new TypeToken<ArrayList<Notification>>(){}.getType();
            List<Notification> deserializeJSON = gson.fromJson(reader, notificationType);
            NotificationListViewModel.notificationsLiveData.postValue(deserializeJSON);
            Log.e(TAG, "I'm SUCCEEDED");
            return Result.success();
        } catch (Throwable throwable) {
            Log.e(TAG, "Error at loading data");
            return Result.failure();
        }

    }
}
