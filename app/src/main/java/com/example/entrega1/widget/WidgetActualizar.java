package com.example.entrega1.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.entrega1.Oxigeno;
import com.example.entrega1.R;
import com.example.entrega1.Utils;
import com.example.entrega1.basedatos.ActualizarDatosUsuario;
import com.example.entrega1.basedatos.ReceptorResultados;

public class WidgetActualizar extends BroadcastReceiver {
    /**
     * Cuando se recive una petición de actualizar el widget, pone en el widget el nuevo valor de oxígeno y se relanza una alarma para actualizar de nuevo el widget un segundo después.
     * @param context La actividad desde la que se lanza la petición
     * @param intent El intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_oxy_mars);
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
