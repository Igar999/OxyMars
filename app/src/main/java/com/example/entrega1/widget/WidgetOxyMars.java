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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = WidgetOxyMarsConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_oxy_mars);
        Oxigeno oxi = Oxigeno.getOxi();
        String usuario = Utils.getUtils().comprobarUsuarioLogeado(context);
        //views.setTextViewText(R.id.widgetTexto, "OxyMars");

        Intent intent = new Intent(context,WidgetOxyMars.class);
        intent.setAction("com.example.entrega1.ACTUALIZAR_WIDGET");
        intent.putExtra( AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                7768, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widgetPlaneta, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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

    @SuppressLint("ShortAlarm")
    @Override
    public void onEnabled(Context context) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_oxy_mars);
        Oxigeno oxi = Oxigeno.getOxi();
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

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

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