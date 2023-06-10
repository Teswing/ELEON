package com.teswing.eleon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationInfoActivity extends AppCompatActivity {

    TextView tvInfoTitle, tvInfoMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        tvInfoTitle = findViewById(R.id.tvNotificationInfoTitle);
        tvInfoMessage = findViewById(R.id.tvNotificationInfoMessage);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ico_arrow_back_ios_sharp_24);


        Intent intent = getIntent();
        if(intent != null) {
            setTitle(getText(R.string.notificationInfo_title) + " " /*+ intent.getStringExtra(Constants.EXTRA_ID)*/);
            tvInfoTitle.setText(intent.getStringExtra(Constants.EXTRA_TITLE));
            tvInfoMessage.setText(intent.getStringExtra(Constants.EXTRA_MESSAGE));
        }
    }
}
