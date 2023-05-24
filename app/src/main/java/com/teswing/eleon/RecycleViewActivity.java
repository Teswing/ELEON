package com.teswing.eleon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teswing.eleon.databinding.ActivityRecycleViewBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO: Разделить на ViewModel и Activity
public class RecycleViewActivity extends AppCompatActivity {

    private final String TAG = "RecycleView";
    boolean updateNeeded = true;
    Gson gson = new Gson();

    RecyclerView recyclerView;

    NotificationListViewModel notificationListViewModel;

    List<Notification> notifications = new ArrayList<>();
    private final RecyclerView.Adapter<NotificationAdapter.ViewHolder> adapter = new NotificationAdapter(notifications);

    ActivityRecycleViewBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecycleViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notificationListViewModel = new NotificationListViewModel(getApplication());

        initRecycleView();

        Uri repUri = getRepository();
        notificationListViewModel.loadData();

        notificationListViewModel.getNotificationsLiveData().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notificationList) {
                if(updateNeeded) {
                    Log.i("NOTIFICATION LD", "I'm CHANGED");
                    Log.e("NotificationLD", String.valueOf(notifications.size()));
                    notifications.addAll(notificationList);
                    adapter.notifyItemInserted(notifications.size() - 1);
                    updateNeeded = false;
                }
            }
        });

        /*
            Баг с пустым выводом LoadWorker, пропал после переустановки приложения
        */
//        notificationListViewModel.getSavedWorkInfo().observe(this, new Observer<List<WorkInfo>>() {
//            @Override
//            public void onChanged(List<WorkInfo> workInfos) {
//                if(workInfos == null || workInfos.isEmpty()) {
//                    return;
//                }
//                WorkInfo workInfo = workInfos.get(0);
////                Log.i("Worker", workInfo.getOutputData().toString());
//                if(workInfo.getState().isFinished()) {
//                    Data outputData = workInfo.getOutputData();
//                    String outputString = outputData.getString("TestValue");
//                    Toast.makeText(getApplicationContext(), "DATA LOADED \n" + outputString, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToRepository(repUri);
            }
        });
    }

    void initRecycleView(){
        recyclerView = findViewById(R.id.rvNotification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    // Привязан в layout XML
    public void addNotification(View view) {
        for (int i = 0 ; i < 5; i++) {
            Random rand = new Random();
            notifications.add(new Notification(
                    "APP: " + rand.ints(5, 97, 123)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString(),
                    "MESSAGE: " + rand.ints(10, 97, 123)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString()
            ));
            adapter.notifyItemInserted(notifications.size() - 1);
            recyclerView.smoothScrollToPosition(notifications.size() - 1);
            updateNeeded = true;
        }
    }

    // TODO: Разрузить нагрузку на воркеров

    private Uri getRepository() {
        Context applicationContext = getApplicationContext();
        File repDir = new File(applicationContext.getFilesDir(), Constants.JSON_REPOSITORY_PATH);
        if (!repDir.exists()) {
            repDir.mkdir();
        }
        File repFile = new File(repDir, Constants.JSON_REPOSITORY_FILE);
        if(!repFile.exists()){
            try (FileWriter writer = new FileWriter(repFile)){
                writer.write("[]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Uri.fromFile(repFile);
    }

    private void saveToRepository(Uri repositoryUri) {
        try {
            FileWriter writer = new FileWriter(repositoryUri.getPath());
            writer.write(gson.toJson(notifications));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
