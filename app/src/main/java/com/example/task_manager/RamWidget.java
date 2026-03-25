package com.example.task_manager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class RamWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int widgetId : appWidgetIds) {
            updateWidget(context,appWidgetManager, widgetId) ;
        }
    }

    void updateWidget(Context context , AppWidgetManager appWidgetManager , int wifgetId) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE) ;
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long total = memoryInfo.totalMem ;
        long free = memoryInfo.availMem ;
        long used = total - free ;

        int usedPercent = (int) ((used * 100.0)/total) ;

        @SuppressLint("RemoteViewLayout")
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wigiet_lay);
        views.setTextViewText(R.id.w_ram_status, "RAM Used: " + usedPercent + "%");


        appWidgetManager.updateAppWidget(wifgetId, views);
    }
}
