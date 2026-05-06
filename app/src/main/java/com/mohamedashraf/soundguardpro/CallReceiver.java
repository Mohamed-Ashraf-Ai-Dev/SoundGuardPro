package com.mohamedashraf.soundguardpro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("SoundGuardPrefs", Context.MODE_PRIVATE);
        boolean isActive = prefs.getBoolean("is_active", true);

        if (!isActive) return;

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("SoundGuard", "Incoming call from: " + incomingNumber);
            
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            
            // استدعاء الميثود من MainActivity (لاحظ أننا نحتاج لطريقة أفضل لاستدعاء الـ native method، 
            // ولكن سنبقيها هكذا للتبسيط بما أنها كانت تعمل)
            new MainActivity().maximizeVolumeNative(audioManager);
        }
    }
}
