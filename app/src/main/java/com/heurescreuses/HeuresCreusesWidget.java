package com.heurescreuses;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import java.util.Calendar;

public class HeuresCreusesWidget extends AppWidgetProvider {

    static void updateAppWidget(Context ctx, AppWidgetManager mgr, int appWidgetId) {
        boolean creuse = PrefsManager.isCreuse(ctx);
        int next = PrefsManager.nextChange(ctx);

        RemoteViews views = new RemoteViews(ctx.getPackageName(), R.layout.widget_layout);

        // Icon & text
        views.setTextViewText(R.id.widget_icon, creuse ? "🌙" : "☀️");
        views.setTextViewText(R.id.widget_status, creuse ? "HC · CREUSE" : "HP · PLEINE");
        views.setInt(R.id.widget_status, "setTextColor",
            creuse ? Color.parseColor("#00E5A0") : Color.parseColor("#FF4D6D"));

        // Current time
        Calendar cal = Calendar.getInstance();
        views.setTextViewText(R.id.widget_time,
            String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));

        // Next change
        if (next >= 0) {
            views.setTextViewText(R.id.widget_next,
                String.format("→ %02d:%02d", next / 60, next % 60));
        } else {
            views.setTextViewText(R.id.widget_next, "—");
        }

        // Tap opens app
        Intent intent = new Intent(ctx, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_root, pi);

        mgr.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context ctx, AppWidgetManager mgr, int[] appWidgetIds) {
        for (int id : appWidgetIds) updateAppWidget(ctx, mgr, id);
    }
}
