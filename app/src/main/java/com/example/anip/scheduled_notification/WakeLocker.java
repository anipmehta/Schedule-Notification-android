package com.example.anip.scheduled_notification;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by Anip on 12/29/2015.
 */
public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;


    public static void acquire(Context ctx) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE,"My Tag");
        System.out.println("in wakelocker");
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}
