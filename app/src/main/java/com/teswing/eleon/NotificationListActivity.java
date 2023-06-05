package com.teswing.eleon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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

public class NotificationListActivity extends AppCompatActivity {

    private final String TAG = "RecycleView";

    RecyclerView recyclerView;
//    NotificationReceiver notificationReceiver;


    NotificationListViewModel notificationListViewModel;

    private final NotificationAdapter adapter = new NotificationAdapter();

    ActivityRecycleViewBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "ACTIVITY (RE-)CREATED");
        binding = ActivityRecycleViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notificationListViewModel = new ViewModelProvider(this).get(NotificationListViewModel.class);

        initRecycleView();
        setTitle("Notifications");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ico_arrow_back_ios_sharp_24);

//        notificationReceiver = new NotificationReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constants.ACTION_NOTIFICATION_RECEIVER);
//        registerReceiver(notificationReceiver,intentFilter);

        notificationListViewModel.getNotificationsLiveData().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notificationList) {
                    Log.i("NOTIFICATION LD", "I'm CHANGED");
                    adapter.submitList(notificationList);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(notificationReceiver);
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
                Intent intent = new Intent(NotificationListActivity.this, NotificationInfoActivity.class);
                intent.putExtra(Constants.EXTRA_TITLE, notification.getTitle());
                intent.putExtra(Constants.EXTRA_MESSAGE, notification.getMessage());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    // Необходимо сохранять в переменную, иначе не работает ..what
    final ActivityResultLauncher<Intent> addNotification = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("ACTIVITY_Result", String.valueOf(result.getResultCode()));
                        if(result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.i(TAG, "onContextItemSelected: " + menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.open_notification):
                Log.i(TAG, "onContextItemSelected: " + item.getMenuInfo());
                return true;
            case (R.id.send_notification):
                return true;
            case (R.id.delete_notification):
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    //    class NotificationReceiver extends BroadcastReceiver {
//        // Малый спектр поддерживаемых устройств, дополнить
//        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            android.app.Notification notification = intent.getParcelableExtra("Notification", android.app.Notification.class);
//            Bundle extras = notification.extras;
//            String title = extras.getString(android.app.Notification.EXTRA_TITLE);
//            String text = extras.getString(android.app.Notification.EXTRA_TEXT);
//            Notification itemNotification = new Notification(title, text);
//            notifications.add(itemNotification);
//            adapter.notifyItemInserted(notifications.size() - 1);
//            Log.e("Notification Receiver", "Message received");
//        }
//    }

    // Может переработать на тестовую кнопку
    // Привязан в layout XML
/*    public void addNotification(View view) {
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
    }*/
}
