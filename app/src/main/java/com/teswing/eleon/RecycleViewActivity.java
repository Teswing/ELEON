package com.teswing.eleon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    Gson gson = new Gson();

    RecyclerView recyclerView;

    List<Notification> notifications = new ArrayList<>();
    private final RecyclerView.Adapter<NotificationAdapter.ViewHolder> adapter = new NotificationAdapter(notifications);

    ActivityRecycleViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecycleViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.rvNotification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        Uri repUri = getRepository();
        try {
            Reader reader = new FileReader(repUri.getPath());
            Type notificationType = new TypeToken<ArrayList<Notification>>(){}.getType();
            notifications.addAll(gson.fromJson(reader, notificationType)) ;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



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

    // Привязан в layout XML
    public void addNotification(View view) {
        for (int i = 0 ; i < 10; i++) {
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
