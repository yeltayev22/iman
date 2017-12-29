package zerotoonecom.n17r.mubr;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class NamazWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        SharedPreferences settings = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String chosenLang = settings.getString(context.getString(R.string.language_key), "ru");
        Locale locale = new Locale(chosenLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.namaz_widget_provider);

            remoteViews = buildUpdate(context, remoteViews); // Update the view using the new data.

            Intent clickIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_view, pendingIntent);

            Intent intent = new Intent(context, NamazWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, NamazWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    private RemoteViews buildUpdate(Context context, RemoteViews views) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String city = preferences.getString(String.valueOf(R.string.city), "somecity");
        String fajr = preferences.getString(String.valueOf(R.string.fajr), "00");
        String sunrise = preferences.getString(String.valueOf(R.string.sunrise), "00");
        String dhuhr = preferences.getString(String.valueOf(R.string.dhuhr), "00");
        String asr = preferences.getString(String.valueOf(R.string.asr), "00");
        String maghrib = preferences.getString(String.valueOf(R.string.maghrib), "00");
        String isha = preferences.getString(String.valueOf(R.string.isha), "00");

        // for color current time in widget
        int thisTime = preferences.getInt(String.valueOf(R.string.color), 100);
        colorAll(views, context);
        colorThis(thisTime, views, context);

        SimpleDateFormat sdfCurrentWeekDay = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat sdfCurrentDay = new SimpleDateFormat("d", Locale.getDefault());
        SimpleDateFormat sdfCurrentMonth = new SimpleDateFormat("MMMM", Locale.getDefault());
        String strWeekDay = sdfCurrentWeekDay.format(new Date(System.currentTimeMillis())).toLowerCase();
        strWeekDay = strWeekDay.substring(0,1).toUpperCase() + strWeekDay.substring(1);
        String strMonth = sdfCurrentMonth.format(new Date(System.currentTimeMillis())).toLowerCase();
        strMonth = strMonth.substring(0,1).toUpperCase() + strMonth.substring(1);
        views.setTextViewText(R.id.date1, strWeekDay + ", " + sdfCurrentDay.format(new Date(System.currentTimeMillis())) + " " + strMonth);

        views.setTextViewText(R.id.city, city);
        views.setTextViewText(R.id.fajr_tv1, context.getString(R.string.fajr));
        views.setTextViewText(R.id.fajr_time1, fajr);
        views.setTextViewText(R.id.sunrise_tv1, context.getString(R.string.sunrise));
        views.setTextViewText(R.id.sunrise_time1, sunrise);
        views.setTextViewText(R.id.dhuhr_tv1, context.getString(R.string.dhuhr));
        views.setTextViewText(R.id.dhuhr_time1, dhuhr);
        views.setTextViewText(R.id.asr_tv1, context.getString(R.string.asr));
        views.setTextViewText(R.id.asr_time1, asr);
        views.setTextViewText(R.id.maghrib_tv1, context.getString(R.string.maghrib));
        views.setTextViewText(R.id.maghrib_time1, maghrib);
        views.setTextViewText(R.id.isha_tv1, context.getString(R.string.isha));
        views.setTextViewText(R.id.isha_time1, isha);

        return views;
    }

    private void colorThis(int thisTime, RemoteViews views, Context context) {
        switch (thisTime) {
            case 0:
                views.setInt(R.id.fajr_layout1, "setBackgroundResource", R.drawable.current_time_rectangle);
                views.setTextColor(R.id.fajr_tv1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                views.setTextColor(R.id.fajr_time1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                break;
            case 1:
                views.setInt(R.id.sunrise_layout1, "setBackgroundResource", R.drawable.current_time_rectangle);
                views.setTextColor(R.id.sunrise_tv1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                views.setTextColor(R.id.sunrise_time1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                break;
            case 2:
                views.setInt(R.id.dhuhr_layout1, "setBackgroundResource", R.drawable.current_time_rectangle);
                views.setTextColor(R.id.dhuhr_tv1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                views.setTextColor(R.id.dhuhr_time1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                break;
            case 3:
                views.setInt(R.id.asr_layout1, "setBackgroundResource", R.drawable.current_time_rectangle);
                views.setTextColor(R.id.asr_time1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                views.setTextColor(R.id.asr_tv1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                break;
            case 4:
                views.setInt(R.id.maghrib_layout1, "setBackgroundResource", R.drawable.current_time_rectangle);
                views.setTextColor(R.id.maghrib_time1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                views.setTextColor(R.id.maghrib_tv1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                break;
            case 5:
                views.setInt(R.id.isha_layout1, "setBackgroundResource", R.drawable.current_time_rectangle);
                views.setTextColor(R.id.isha_time1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                views.setTextColor(R.id.isha_tv1, ContextCompat.getColor(context, R.color.colorPrimaryDark));
                break;

        }
    }

    private void colorAll(RemoteViews views, Context context) {
        views.setInt(R.id.fajr_layout1, "setBackgroundResource", 0);
        views.setTextColor(R.id.fajr_time1, ContextCompat.getColor(context, R.color.colorPrimary));
        views.setTextColor(R.id.fajr_tv1, ContextCompat.getColor(context, R.color.colorPrimary));

        views.setInt(R.id.sunrise_layout1, "setBackgroundResource", 0);
        views.setTextColor(R.id.sunrise_time1, ContextCompat.getColor(context, R.color.colorPrimary));
        views.setTextColor(R.id.sunrise_tv1, ContextCompat.getColor(context, R.color.colorPrimary));

        views.setInt(R.id.dhuhr_layout1, "setBackgroundResource", 0);
        views.setTextColor(R.id.dhuhr_time1, ContextCompat.getColor(context, R.color.colorPrimary));
        views.setTextColor(R.id.dhuhr_tv1, ContextCompat.getColor(context, R.color.colorPrimary));

        views.setInt(R.id.asr_layout1, "setBackgroundResource", 0);
        views.setTextColor(R.id.asr_time1, ContextCompat.getColor(context, R.color.colorPrimary));
        views.setTextColor(R.id.asr_tv1, ContextCompat.getColor(context, R.color.colorPrimary));

        views.setInt(R.id.maghrib_layout1, "setBackgroundResource", 0);
        views.setTextColor(R.id.maghrib_time1, ContextCompat.getColor(context, R.color.colorPrimary));
        views.setTextColor(R.id.maghrib_tv1, ContextCompat.getColor(context, R.color.colorPrimary));

        views.setInt(R.id.isha_layout1, "setBackgroundResource", 0);
        views.setTextColor(R.id.isha_time1, ContextCompat.getColor(context, R.color.colorPrimary));
        views.setTextColor(R.id.isha_tv1, ContextCompat.getColor(context, R.color.colorPrimary));

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

