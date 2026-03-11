package com.heurescreuses;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

public class WidgetUpdateService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(this);
        int[] ids = mgr.getAppWidgetIds(new ComponentName(this, HeuresCreusesWidget.class));
        for (int id : ids) HeuresCreusesWidget.updateAppWidget(this, mgr, id);
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}
