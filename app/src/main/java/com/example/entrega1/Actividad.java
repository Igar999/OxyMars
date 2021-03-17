package com.example.entrega1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Actividad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Se pausa la música, para que no siga sonando si la aplicación no está en primer plano, y se pone en marcha la alarma para que salga notificación mientras la aplicación está cerrada
     */
    @Override
    protected void onPause() {
        super.onPause();
        Utils.getUtils().musicaPause();

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificacionProgramada.class);
        alarmIntent = PendingIntent.getBroadcast(this, 01, intent, 0);
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 30 * 60 * 1000, alarmIntent); //LA NOTIFICACION LLEGA 30 MINUTOS DESPUES
    }

    /**
     * Se reproduce la música (si se indica en las preferencias) y se detiene la alarma de la notificación en caso de que exista, para que no llegue la notificación mientras se está jugando
     */
    @Override
    protected void onResume() {
        super.onResume();
        Utils.getUtils().musicaPlay();

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificacionProgramada.class);
        alarmIntent = PendingIntent.getBroadcast(this, 01, intent, 0);
        alarmMgr.cancel(alarmIntent);
    }
}