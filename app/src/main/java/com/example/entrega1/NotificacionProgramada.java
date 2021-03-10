package com.example.entrega1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class NotificacionProgramada extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Locale nuevaloc = new Locale(prefs.getString("lista_idioma", "es"));
        Locale.setDefault(nuevaloc);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context contexto = context.createConfigurationContext(configuration);
        context.getResources().updateConfiguration(configuration, contexto.getResources().getDisplayMetrics());

        Intent i = new Intent(context,LoginActivity.class);
        PendingIntent intentApp = PendingIntent.getActivity(context, 13, i, 0);

        //https://stackoverflow.com/questions/41888161/how-to-create-a-custom-notification-layout-in-android
        RemoteViews contentView;
        contentView = new RemoteViews(context.getPackageName(), R.layout.notificacion);
        contentView.setImageViewResource(R.id.planeta, R.mipmap.ic_launcher);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "aus")
                        .setSmallIcon(R.drawable.marte)
                        .setContentTitle(context.getString(R.string.tu_planeta_espera))
                        .setContentText(context.getString(R.string.vuelve))
                        .setSubText(context.getString(R.string.notificacion))
                        .setAutoCancel(true)
                        .setContent(contentView)
                        .setContentIntent(intentApp)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01, mBuilder.build());
    }
}