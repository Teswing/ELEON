package com.teswing.eleon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
// TODO:2 И навести порядок
public class RecycleViewActivity extends AppCompatActivity {

    private final String TAG = "RecycleView";
    Gson gson = new Gson();

    RecyclerView recyclerView;
    NotificationReceiver notificationReceiver;


    NotificationListViewModel notificationListViewModel;

    List<Notification> notifications = new ArrayList<>();
    private final RecyclerView.Adapter<NotificationAdapter.ViewHolder> adapter = new NotificationAdapter(notifications);

    ActivityRecycleViewBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecycleViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notificationListViewModel = new ViewModelProvider(this).get(NotificationListViewModel.class);

        initRecycleView();

        Uri repUri = getRepository();
        notificationListViewModel.loadData();
        notificationReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_NOTIFICATION_RECEIVER);
        registerReceiver(notificationReceiver,intentFilter);

        notificationListViewModel.getNotificationsLiveData().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notificationList) {
                if(getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    Log.i("NOTIFICATION LD", "I'm CHANGED");
                    Log.e("NotificationLD", String.valueOf(notifications.size()));
                    notifications.addAll(notificationList);
                    adapter.notifyItemInserted(notifications.size() - 1);
                }
            }
        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReceiver);
    }

    void initRecycleView(){
        recyclerView = findViewById(R.id.rvNotification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    class NotificationReceiver extends BroadcastReceiver {
        // Малый спект поддерживаемых устройств, дополнить
        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        @Override
        public void onReceive(Context context, Intent intent) {
            android.app.Notification notification = intent.getParcelableExtra("Notification", android.app.Notification.class);
            Bundle extras = notification.extras;
            String title = extras.getString(android.app.Notification.EXTRA_TITLE);
            String text = extras.getString(android.app.Notification.EXTRA_TEXT);
            Notification itemNotification = new Notification(title, text);
            notifications.add(itemNotification);
            adapter.notifyItemInserted(notifications.size() - 1);
            Log.e("Notification Receiver", "Message received");
        }
    }

    // Привязан в layout XML
    public void addNotification(View view) {
        for (int i = 0 ; i < 1; i++) {
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
