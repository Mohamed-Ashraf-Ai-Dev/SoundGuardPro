package com.manus.soundguardpro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("SoundGuard", "Incoming call from: " + incomingNumber);
            
            // هنا يتم استدعاء محرك C++ لرفع الصوت لأقصى درجة فوراً
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            
            // يمكن إضافة منطق هنا للتحقق من "جهات اتصال محددة" 
            // ولكن حالياً سنقوم برفع الصوت لأي مكالمة واردة لإثبات القوة
            new MainActivity().maximizeVolumeNative(audioManager);
        }
    }
}
