package com.example.entrega1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Vibrator;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class NotificacionProgramada extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,LoginActivity.class);
        PendingIntent intentApp = PendingIntent.getActivity(context, 13, i, 0);

        RemoteViews contentView;
        contentView = new RemoteViews(context.getPackageName(), R.layout.notificacion);
        contentView.setImageViewResource(R.id.planeta, R.mipmap.ic_launcher);



        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "aus")
                        .setSmallIcon(R.drawable.marte)
                        .setContentTitle("Tu planeta te espera")
                        .setContentText("Vuelve y haz de Marte un lugar mejor.")
                        .setSubText("Notificación")
                        .setAutoCancel(true)
                        .setContent(contentView)
                        .setContentIntent(intentApp);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01, mBuilder.build());


    }
}