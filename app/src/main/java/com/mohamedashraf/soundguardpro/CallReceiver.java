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

            java.util.Set<String> whiteList = prefs.getStringSet("white_list", new java.util.HashSet<String>());
            
            // If white list is empty, allow all. If not, only allow numbers in the list.
            if (whiteList.isEmpty() || (incomingNumber != null && isNumberInWhiteList(incomingNumber, whiteList))) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, 
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 
                        AudioManager.FLAG_SHOW_UI);
                }
            }
        }
    }

    private boolean isNumberInWhiteList(String incoming, java.util.Set<String> whiteList) {
        if (incoming == null) return false;
        // Simple normalization: remove non-digits
        String normalizedIncoming = incoming.replaceAll("[^0-9]", "");
        if (normalizedIncoming.isEmpty()) return false;

        for (String whiteNumber : whiteList) {
            String normalizedWhite = whiteNumber.replaceAll("[^0-9]", "");
            if (normalizedWhite.isEmpty()) continue;

            // Match if one is a suffix of the other (handles local vs international formats)
            // Minimum length check to avoid false positives with very short numbers
            if (normalizedIncoming.length() >= 7 && normalizedWhite.length() >= 7) {
                if (normalizedIncoming.endsWith(normalizedWhite) || normalizedWhite.endsWith(normalizedIncoming)) {
                    return true;
                }
            } else if (normalizedIncoming.equals(normalizedWhite)) {
                return true;
            }
        }
        return false;
    }
}
