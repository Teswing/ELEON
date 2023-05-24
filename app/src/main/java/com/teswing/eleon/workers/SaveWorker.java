package com.teswing.eleon.workers;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class SaveWorker extends Worker {
    public SaveWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();
        Gson gson = new Gson();
        Uri repositoryUri = WorkUtils.getRepository(applicationContext);
        try {
            FileWriter writer = new FileWriter(repositoryUri.getPath());
//            writer.write(gson.toJson(notifications));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}
