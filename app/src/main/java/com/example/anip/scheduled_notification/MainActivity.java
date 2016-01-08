package com.example.anip.scheduled_notification;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText txtDate,txtTime;
    int j=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtDate = (EditText) findViewById(R.id.txtDate);
        txtTime = (EditText) findViewById(R.id.txtTime);
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        Button date=(Button)findViewById(R.id.date);
        Button time=(Button)findViewById(R.id.time);
        Button notif=(Button)findViewById(R.id.notif);
        Button cancel=(Button)findViewById(R.id.button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Intent notificationIntent = new Intent(MainActivity.this, MyReciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,2, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                //notificationManager.cancel(2);
            }
        });
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time=txtTime.getText().toString();
                String date=txtDate.getText().toString();
                String ftime=time.replaceAll(":", "");
                String fdate=date.replaceAll("-", "");
                Log.i("hell", "time" + ftime);
                Log.i("hell","date"+fdate);
                Calendar c1=Calendar.getInstance();
                try {
                    c1.setTime(new SimpleDateFormat("ddMMyyyyHHmmss").parse(fdate+ftime+"00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long tiime=c1.getTimeInMillis();
                Log.i("hell", String.valueOf(tiime));
                long tim = System.currentTimeMillis();
                long delay=tiime-tim;
                if (delay > 0) {
                    Log.i("hell", "notification generated");

                    Log.i("hell", "name is");
                    scheduleNotification(getNotification("Don't miss your run tomorrow!", "Hey Naman!\nYou planned for a run / ride tomorrow. Good Luck! ",tiime), delay);

                }
            }

        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepic();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepic();
            }
        });


    }

    private void timepic() {
        TimePickerDialog tpd=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txtTime.setText(hourOfDay + ":"+minute);

            }
        }, mHour, mMinute, false);
        tpd.show();
    }
    private void datepic(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        txtDate.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }
    private void scheduleNotification(Notification notification, long delay) {

        Intent notificationIntent = new Intent(this, MyReciever.class);
        notificationIntent.putExtra(MyReciever.NOTIFICATION_ID, j);
        notificationIntent.putExtra(MyReciever.NOTIFICATION, notification);

        System.out.println("index"+j);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,j, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        j=j+1;

        long futureInMillis = System.currentTimeMillis()+delay;
        System.out.println("final time"+futureInMillis);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }
    private Notification getNotification(String title,String content, long delay) {
        Log.i("hell", "get notifcation");
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(content);
        //builder.setVisibility(Visibility.MODE_IN);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setStyle(new Notification.BigTextStyle()
                .bigText("Hey Naman! You planned for a ride tomorrow. Good Luck!"));
        Intent myIntent=new Intent(MainActivity.this,MainActivity.class);
        PendingIntent intent2 = PendingIntent.getActivity(this, 1,
                myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(intent2);
        builder.setWhen(delay);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Event details:");
        inboxStyle.addLine("hi");
        inboxStyle.addLine("how");
        inboxStyle.addLine("are");
        //builder.setStyle(inboxStyle);

        return builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
