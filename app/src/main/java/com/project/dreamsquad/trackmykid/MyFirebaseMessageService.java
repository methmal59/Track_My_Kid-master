package com.project.dreamsquad.trackmykid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.project.dreamsquad.trackmykid.activity.MainActivity;
import com.project.dreamsquad.trackmykid.models.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remotemsg) {

        System.out.println("MESSAE RECIEVEDDDDDDDDDDDDDDDD   " + remotemsg );
        Log.d(TAG, "From -> " + remotemsg.getFrom());
        Log.d(TAG, "Demo Notification Body -> " + remotemsg.getNotification().getBody());
        System.out.println(remotemsg.getNotification().getBody());
//        checkTimeandSendNotification(remotemsg.getNotification().getBody());
//        checkTimeandSendNotification();
    }


    public void checkTimeandSendNotification(){
        String topic;
        String msg;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Date today = new Date();

        if(day != 1 && day != 0) {

            SimpleDateFormat formatAMPM = new SimpleDateFormat("a");
            String ampm = formatAMPM.format(today);

            SimpleDateFormat formatHour = new SimpleDateFormat("hh");
            int hour = Integer.parseInt(formatHour.format(today));


            if((ampm.equals("AM")) && (hour > 5) && (hour < 8.5)){
                topic = "Dropoff " +UserProfile.getInstance().getKidName();
                msg = UserProfile.getInstance().getDriverName()+" is "+UserProfile.getInstance().getPickUpReminderDistance() + "km away";
                sendNotification(topic,msg);
            }

            if((ampm.equals("PM")) && (hour > -1) && (hour < 4)){
                topic = "Pickup " +UserProfile.getInstance().getKidName();
                msg = UserProfile.getInstance().getDriverName()+" is "+UserProfile.getInstance().getPickUpReminderDistance() + "km away";
                sendNotification(topic,msg);
            }
        }
    }


    private void sendNotification(String topic, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(topic)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
