package com.example.anip.scheduled_notification;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Anip on 12/29/2015.
 */
public class MyReciever extends BroadcastReceiver
{
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        //Toast.makeText(context,"Alarm Received", LENGTH_LONG).show();
        //Notification notification = intent.getParcelableExtra(NOTIFICATION);
        Intent service1 = new Intent(context, MyAlarmService.class);
        service1.putExtra(MyAlarmService.NOTIFICATION_ID,id);
        service1.putExtra(MyAlarmService.NOTIFICATION,notification);
        context.startService(service1);

    }
}