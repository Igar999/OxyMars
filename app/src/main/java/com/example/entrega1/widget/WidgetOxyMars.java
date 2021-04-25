package com.example.entrega1.widget;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.entrega1.Oxigeno;
import com.example.entrega1.R;
import com.example.entrega1.Utils;
import com.example.entrega1.basedatos.ObtenerDatosUsuario;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetOxyMarsConfigureActivity WidgetOxyMarsConfigureActivity}
 */
public class WidgetOxyMars extends AppWidgetProvider {

    /**
     * Se actualiza el widget
     * @param context El contexto
     * @param appWidgetManager El manager
     * @param appWidgetId El id
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = WidgetOxyMarsConfigureActivity.loadTitlePref(context, appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_oxy_mars);
        Intent intent = new Intent(context,WidgetOxyMars.class);
        intent.setAction("com.example.entrega1.ACTUALIZAR_WIDGET");
        intent.putExtra( AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                7768, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widgetPlaneta, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Se actualiza el widget y se establece una alarma para volver a actualizarlo un segundo después.
     * @param context EL contexto
     * @param appWidgetManager El manager
     * @param appWidgetIds Los ids
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(context, WidgetActualizar.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 7475, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000, pi);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WidgetOxyMarsConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    /**
     * Se comprueba si hay un usuario logeado, y si es así, se obtienen sus datos, y se lanza una alarma para actualizar el widget.
     * @param context
     */
    @SuppressLint("ShortAlarm")
    @Override
    public void onEnabled(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_oxy_mars);
        String usuario = Utils.getUtils().comprobarUsuarioLogeado(context);
        views.setTextViewText(R.id.widgetTexto, "OxyMars");
        if (usuario != ""){
            Utils.getUtils().setUsuario(usuario);
            ObtenerDatosUsuario cargarDatos = new ObtenerDatosUsuario(usuario);
            new Thread(cargarDatos).start();
        }
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WidgetActualizar.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 7475, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000, pi);
    }

    /**
     * Si se recibe una acción de toque, se suma el oxígeno por toque al oxígeno total, como si se hubiera tocado el planeta en el juego.
     * @param context El contexto
     * @param intent El intent
     */
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.entrega1.ACTUALIZAR_WIDGET")) {
            int widgetId = intent.getIntExtra( AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Oxigeno.getOxi().aumentarOxigenoToque();
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_oxy_mars);
                remoteViews.setTextViewText(R.id.widgetTexto, String.valueOf(Oxigeno.getOxi().ponerCantidad(Oxigeno.getOxi().getOxigeno(), true)));
                updateAppWidget(context, widgetManager, widgetId);
            }
        }
    }
}