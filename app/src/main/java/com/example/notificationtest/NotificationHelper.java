package com.example.notificationtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHelper {

    private  static  final String Channel_ID="simplified_coding";
    private  static  final String Channel_Name="simplified coding";
    private  static  final String Channel_Desc="simplified coding notifications";




    public static void displayNotification(Context context,String title,String body){

        Intent intent=new Intent(context,ProfileActivity.class);
        intent.putExtra("body",body);

        PendingIntent pendingIntent=PendingIntent.getActivity(
                context,
                1,
                intent,
                PendingIntent.FLAG_ONE_SHOT
        );

      //  PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Channel_ID, Channel_Name, importance);
            channel.setDescription(Channel_Desc);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Bitmap largeIcon= BitmapFactory.decodeResource(context.getResources(),R.drawable.mercedes);

            Notification notification=new NotificationCompat.Builder(context,Channel_ID)
                    .setSmallIcon(R.drawable.ic_alert)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setLargeIcon(largeIcon)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(largeIcon)
                    .bigLargeIcon(null))
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText("dfjdjjkdfjdjfkdsfkjkj dfdkdfkjdfkjdf dfgkjdfgjdfkj dfgkjdfkjdg dfgkjdfkjd")
//                            .setBigContentTitle("wewe")
//                    )

                    .setContentIntent(pendingIntent)
                    .addAction(0,"Confirm",pendingIntent)
                    .setAutoCancel(true)
                    .setColor(Color.BLUE)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();


          //  notificationManager.createNotificationChannel(channel);

            notificationManager.notify(1,notification);

        }else{


            Bitmap largeIcon= BitmapFactory.decodeResource(context.getResources(),R.drawable.mercedes);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Channel_ID)
                    .setSmallIcon(R.drawable.ic_alert)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setLargeIcon(largeIcon)
                    .setContentIntent(pendingIntent)
                    .addAction(0,"Confirm",pendingIntent)
                    .setAutoCancel(true)
                    .setColor(Color.BLUE)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1,builder.build());

        }



    }




    private void displayNotificationOne() {
        //display notification from android 8 (api level 26) and above
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(Channel_ID, Channel_Name, importance);
//            channel.setDescription(Channel_Desc);
//
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//
//
//
//            Notification notification=new NotificationCompat.Builder(this,Channel_ID)
//                    .setSmallIcon(R.mipmap.car_icon)
//                    .setContentTitle(title)
//                    .setContentText(body)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//            NotificationManager notificationManager =c getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
    }
}
