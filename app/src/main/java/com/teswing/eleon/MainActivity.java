package com.teswing.eleon;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.teswing.eleon.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private final int POST_NOTIFICATION_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnSwitchToRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NotificationListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startServ(View view) { // XML -> btnStartService
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager.areNotificationsEnabled()) {
            if (NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {
                Toast.makeText(this, "READ NOTIF ACCESS GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
//            Toast.makeText(this, "Notifications Enabled!", Toast.LENGTH_SHORT).show();
        } else {
            requestSendNotificationPermission();
//            Toast.makeText(this, "Notifications Disabled :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopServ(View view) { // XML -> btnStopService
        if (NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }
    }

    void requestSendNotificationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title)
                    .setMessage(R.string.dialog_message_POST_NOTIFICATION)
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chooseAPIPermission();
                        }
                    })
                    .setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            chooseAPIPermission();
        }
    }

    void chooseAPIPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    POST_NOTIFICATION_PERMISSION_CODE);
        } else {
            Toast.makeText(this, "PERMISSION ERROR: \nNot yet implemented", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == POST_NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "|POST VERIFICATION| \nPERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "|POST VERIFICATION| \nWAS NOT PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}