package com.mohamedashraf.soundguardpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("soundguardpro");
    }

    public native String stringFromJNI();
    public native void maximizeVolumeNative(AudioManager audioManager);

    private static final int PERMISSION_REQUEST_CODE = 100;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("SoundGuardPrefs", MODE_PRIVATE);

        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        setupUI();
        checkPermissions();
        requestDoNotDisturbPermission();
    }

    private void setupUI() {
        Switch guardSwitch = findViewById(R.id.guard_switch);
        guardSwitch.setChecked(sharedPreferences.getBoolean("is_active", true));
        guardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean("is_active", isChecked).apply();
                Toast.makeText(MainActivity.this, isChecked ? "Guard Activated" : "Guard Deactivated", Toast.LENGTH_SHORT).show();
            }
        });

        Button aboutButton = findViewById(R.id.btn_about);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        ImageButton fbButton = findViewById(R.id.btn_facebook);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/share/1EQP87fcmW/"));
                startActivity(intent);
            }
        });
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About SoundGuard Pro");
        builder.setMessage("Founder: Mohamed Ashraf\n\nSoundGuard Pro ensures you never miss critical calls by automatically maximizing volume for incoming calls even in silent mode.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
                break;
            }
        }
    }

    private void requestDoNotDisturbPermission() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "Please grant Do Not Disturb access for SoundGuard Pro to work in silent mode", Toast.LENGTH_LONG).show();
        }
    }
}
