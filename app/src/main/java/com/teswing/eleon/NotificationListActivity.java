package com.teswing.eleon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teswing.eleon.databinding.ActivityRecycleViewBinding;

import java.util.List;
import java.util.Random;

public class NotificationListActivity extends AppCompatActivity {

    private final String TAG = "RecycleView";

    RecyclerView recyclerView;

    NotificationListViewModel notificationListViewModel;

    private final NotificationAdapter adapter = new NotificationAdapter();

    ActivityRecycleViewBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.e(TAG, "ACTIVITY (RE-)CREATED");
        binding = ActivityRecycleViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notificationListViewModel = new ViewModelProvider(this).get(NotificationListViewModel.class);

        initRecycleView();
        setTitle(R.string.notificationList_title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ico_arrow_back_ios_sharp_24);


        notificationListViewModel.getNotificationsLiveData().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notificationList) {
//                    Log.i("NOTIFICATION LD", "I'm CHANGED");
                    adapter.submitList(notificationList);
//                    Log.i("ON_UPDATE", String.valueOf(adapter.getItemCount()));
                    recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

    }

    void initRecycleView(){
        recyclerView = findViewById(R.id.rvNotification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Notification notification) {
                openNotification_card(notification);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        registerForContextMenu(recyclerView);
    }

    // Необходимо сохранять в переменную, иначе не работает ..what
    final ActivityResultLauncher<Intent> addNotification = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    Log.i("ACTIVITY_Result", String.valueOf(result.getResultCode()));
                        if(result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null) {
                                Toast.makeText(NotificationListActivity.this,
                                        R.string.notificationList_dataIsNull, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String title = data.getStringExtra(Constants.EXTRA_TITLE);
                            String message = data.getStringExtra(Constants.EXTRA_MESSAGE);

                            Notification notification = new Notification(title, message);
                            notificationListViewModel.insert(notification);

                        }
                }
            });

    // Открыть форму создания уведомлений
    void addNotification_form() {
        Intent intent = new Intent(this, AddNotificationActivity.class);
        addNotification.launch(intent);
    }

    void openNotification_card(Notification notification) {
        Intent intent = new Intent(NotificationListActivity.this, NotificationInfoActivity.class);
        intent.putExtra(Constants.EXTRA_ID, notification.getId());
        intent.putExtra(Constants.EXTRA_TITLE, notification.getTitle());
        intent.putExtra(Constants.EXTRA_MESSAGE, notification.getMessage());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.add_notification):
                addNotification_form();
                return true;
            case (R.id.delete_all):
                notificationListViewModel.deleteAll();
                return true;
            case (R.id.create_notification_debug):
                createNotification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.open_notification):
                openNotification_card(adapter.getNotificationByPosition(item.getGroupId()));
                return true;
            case (R.id.send_notification):
                Toast.makeText(this, R.string.notificationSent, Toast.LENGTH_SHORT).show();
                return true;
            // TODO: Краш после удаления на реальном устройстве
            case (R.id.delete_notification):
                notificationListViewModel.delete(adapter.getNotificationByPosition(item.getGroupId()));
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public void createNotification() {
        for (int i = 0 ; i < 1; i++) {
            Random rand = new Random();
            String randTitle = rand.ints(5, 97, 123)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            String randMessage = rand.ints(10, 97, 123)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            notificationListViewModel.insert(new Notification(
                    getText(R.string.notificationList_debugTitle) + " " + randTitle,
                    getText(R.string.notificationList_debugMessage) + " " + randMessage
            ));
        }
    }
}
