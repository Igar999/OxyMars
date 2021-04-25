package com.example.entrega1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.example.entrega1.notificaciones.NotificacionProgramada;
import com.example.entrega1.widget.WidgetActualizar;

public class Actividad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utils.getUtils().musicaPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Utils.getUtils().musicaPause();
    }

    /**
     * Se pausa la música, para que no siga sonando si la aplicación no está en primer plano, y se pone en marcha la alarma para que salga notificación mientras la aplicación está cerrada
     */
    @Override
    protected void onPause() {
        super.onPause();
        Utils.getUtils().musicaPause();

        AlarmManager am=(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(getApplicationContext(), WidgetActualizar.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 7475, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000, pi);

        //https://stackoverflow.com/questions/39674850/send-a-notification-when-the-app-is-closed
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificacionProgramada.class);
        alarmIntent = PendingIntent.getBroadcast(this, 01, intent, 0);
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 30 * 60 * 1000, alarmIntent); //LA NOTIFICACION LLEGA 30 MINUTOS DESPUÉS
    }

    /**
     * Se reproduce la música (si se indica en las preferencias) y se detiene la alarma de la notificación en caso de que exista, para que no llegue la notificación mientras se está jugando
     */
    @Override
    protected void onResume() {
        super.onResume();
        Utils.getUtils().musicaPlay();

        AlarmManager am=(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(getApplicationContext(), WidgetActualizar.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 7475, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000, pi);

        //https://stackoverflow.com/questions/28922521/how-to-cancel-alarm-from-alarmmanager/28922621
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificacionProgramada.class);
        alarmIntent = PendingIntent.getBroadcast(this, 01, intent, 0);
        alarmMgr.cancel(alarmIntent);
    }
}