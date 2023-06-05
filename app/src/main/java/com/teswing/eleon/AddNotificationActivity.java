package com.teswing.eleon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddNotificationActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);
        etTitle = findViewById(R.id.etTitle);
        etMessage = findViewById(R.id.etMessage);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnBack = findViewById(R.id.btnBack);
        btnSave.setOnClickListener(v -> saveNotification());
        btnBack.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
       setTitle("Add Notification");

    }

    private void saveNotification() {
        String title = etTitle.getText().toString();
        String message = etMessage.getText().toString();
        if (title.trim().isEmpty() || message.trim().isEmpty()) {
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(Constants.EXTRA_TITLE, title);
        data.putExtra(Constants.EXTRA_MESSAGE, message);
        setResult(RESULT_OK, data);
        finish();
    }
}