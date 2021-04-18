package com.example.entrega1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ServicioFirebase extends FirebaseMessagingService {

    public ServicioFirebase() {

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        //https://stackoverflow.com/questions/37787373/firebase-fcm-how-to-get-token
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*if (remoteMessage.getData().size() > 0) {

        }
        if (remoteMessage.getNotification() != null) {
        }*/
        Log.i("notificacion", "Llega aqui");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Locale nuevaloc = new Locale(prefs.getString("lista_idioma", "es"));
        Locale.setDefault(nuevaloc);
        Configuration configuration = getApplicationContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context contexto = getApplicationContext().createConfigurationContext(configuration);
        getApplicationContext().getResources().updateConfiguration(configuration, contexto.getResources().getDisplayMetrics());

        //https://stackoverflow.com/questions/41888161/how-to-create-a-custom-notification-layout-in-android
        RemoteViews contentView;
        contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notificacion);
        contentView.setImageViewResource(R.id.planeta, R.mipmap.ic_launcher);

        NotificationManager elManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("aus", "Ausente",
                    NotificationManager.IMPORTANCE_DEFAULT);
            elManager.createNotificationChannel(elCanal);
        }

        contentView.setTextViewText(R.id.textoVuelve, getApplicationContext().getString(R.string.borradoCorrectamente));

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "aus")
                        .setSmallIcon(R.drawable.marte)
                        .setContentTitle(getApplicationContext().getString(R.string.app_name))
                        .setContentText(getApplicationContext().getString(R.string.borradoCorrectamente))
                        .setSubText(getApplicationContext().getString(R.string.notificacion))
                        .setAutoCancel(true)
                        .setContent(contentView)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01, mBuilder.build());
    }

    //https://stackoverflow.com/questions/37787373/firebase-fcm-how-to-get-token
    public static String getToken(Context context){
        return  context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");

    }

}
