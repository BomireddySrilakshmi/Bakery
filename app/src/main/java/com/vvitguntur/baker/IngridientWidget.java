package com.vvitguntur.baker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class IngridientWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("ProjectConstents",Context.MODE_PRIVATE);
        CharSequence title=sharedPreferences.getString("recipeName","");
        CharSequence widgetText = title+"\n\n"+sharedPreferences.getString("recipe_ingredients","");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingridient_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.ingridient,pendingIntent);
        // Instruct the widget manager to update the widget
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
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

