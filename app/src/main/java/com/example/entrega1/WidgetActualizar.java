package com.example.entrega1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.entrega1.runnables.ActualizarDatosUsuario;
import com.example.entrega1.runnables.ReceptorResultados;

public class WidgetActualizar extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_oxy_mars);
        //Oxigeno.getOxi().aumentarOxigenoSegundo();
        remoteViews.setTextViewText(R.id.widgetTexto, String.valueOf(Oxigeno.getOxi().ponerCantidad(Oxigeno.getOxi().getOxigeno(), true)));
        ComponentName tipowidget = new ComponentName(context, WidgetOxyMars.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(tipowidget, remoteViews);

        if (Utils.getUtils().getUsuario() != ""){
            if (ReceptorResultados.getReceptorResultados().haAcabadoActualizar()){
                new Thread(new ActualizarDatosUsuario(Utils.getUtils().getUsuario(), Oxigeno.getOxi().getOxigeno(),Oxigeno.getOxi().getOxiToque(),Oxigeno.getOxi().getOxiSegundo(),Oxigeno.getOxi().getDesbloqueadoToque(),Oxigeno.getOxi().getDesbloqueadoSegundo()));
            }
        }

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(context, WidgetActualizar.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 7475, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000, pi);

    }
}
