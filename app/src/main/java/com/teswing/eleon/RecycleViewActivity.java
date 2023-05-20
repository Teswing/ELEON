package com.teswing.eleon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teswing.eleon.databinding.ActivityRecycleViewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecycleViewActivity extends AppCompatActivity {

    List<Notification> notifications = new ArrayList<>();
    private final RecyclerView.Adapter<NotificationAdapter.ViewHolder> adapter = new NotificationAdapter(notifications);

    ActivityRecycleViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecycleViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.rvNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Привязан в layout XML
    public void addNotification(View view) {
        Random rand = new Random();
        notifications.add(new Notification(
                "APP: " + rand.ints(5, 97,123)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString(),
                "MESSAGE: " + rand.ints(10, 97,123)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString()
        ));
        adapter.notifyItemInserted(notifications.size() - 1);
    }
}
