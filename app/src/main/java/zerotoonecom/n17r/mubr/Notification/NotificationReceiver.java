package zerotoonecom.n17r.mubr.Notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import zerotoonecom.n17r.mubr.MainActivity;
import zerotoonecom.n17r.mubr.R;
import zerotoonecom.n17r.mubr.Time.NamazTimeContract;
import zerotoonecom.n17r.mubr.Time.NamazTimeDbHelper;

/**
 * Created by Nurdaulet Yeltayev (yeltayev22) on 24.01.2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    String currentDate;
    private SQLiteDatabase db;
    String fajr, sunrise, asr, dhuhr, maghrib, isha;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("yeltayev22", "OnReceive received");
        //Log.d("yeltayev22", intent.getStringExtra("namaz_name"));
        sendNotification(context, intent);

        nextNotificationTime(context);
    }

    private void nextNotificationTime(Context context) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = sdfDate.format(c.getTime());
        SimpleDateFormat sdfCurrentSeconds = new SimpleDateFormat("HH:mm:ss");
        String time = sdfCurrentSeconds.format(new Date(System.currentTimeMillis()));
        long currentTimeInMillis = toMillis(time);

        prayTimes(context);

        SharedPreferences settings = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(context);

        if (settings.getBoolean(context.getString(R.string.fajr_notification_key), false) &&
                toMillis(fajr + ":00") > currentTimeInMillis) {
            setNotification(fajr, context.getString(R.string.fajr), false, context);
            Log.d("yeltayev22", "Fajr set");
        } else if (settings.getBoolean(context.getString(R.string.sunrise_notification_key), false) &&
                toMillis(sunrise + ":00") > currentTimeInMillis) {
            setNotification(sunrise, context.getString(R.string.sunrise), false, context);
            Log.d("yeltayev22", "Sunrise set");
        } else if (settings.getBoolean(context.getString(R.string.dhuhr_notification_key), false) &&
                toMillis(dhuhr + ":00") > currentTimeInMillis) {
            setNotification(dhuhr, context.getString(R.string.dhuhr), false, context);
            Log.d("yeltayev22", "Dhuhr set");
        } else if (settings.getBoolean(context.getString(R.string.asr_notification_key), false) &&
                toMillis(asr + ":00") > currentTimeInMillis) {
            setNotification(asr, context.getString(R.string.asr), false, context);
            Log.d("yeltayev22", "Asr set");
        } else if (settings.getBoolean(context.getString(R.string.maghrib_notification_key), false) &&
                toMillis(maghrib + ":00") > currentTimeInMillis) {
            setNotification(maghrib, context.getString(R.string.maghrib), false, context);
            Log.d("yeltayev22", "Maghrib set");
        } else if (settings.getBoolean(context.getString(R.string.isha_notification_key), false) &&
                toMillis(isha + ":00") > currentTimeInMillis) {
            setNotification(isha, context.getString(R.string.isha), false, context);
            Log.d("yeltayev22", "Isha set");
        } else if (settings.getBoolean(context.getString(R.string.fajr_notification_key), false)) {
            setNotification(getNextNamaz(context,1), context.getString(R.string.fajr), true, context);
            Log.d("yeltayev22", "Fajr set");
        } else if (settings.getBoolean(context.getString(R.string.sunrise_notification_key), false)) {
            setNotification(getNextNamaz(context,2), context.getString(R.string.sunrise), true, context);
            Log.d("yeltayev22", "Sunrise set");
        } else if (settings.getBoolean(context.getString(R.string.dhuhr_notification_key), false)) {
            setNotification(getNextNamaz(context,3), context.getString(R.string.dhuhr), true, context);
            Log.d("yeltayev22", "Dhuhr set");
        } else if (settings.getBoolean(context.getString(R.string.asr_notification_key), false)) {
            setNotification(getNextNamaz(context,4), context.getString(R.string.asr), true, context);
            Log.d("yeltayev22", "Asr set");
        } else if (settings.getBoolean(context.getString(R.string.maghrib_notification_key), false)) {
            setNotification(getNextNamaz(context,5), context.getString(R.string.maghrib), true, context);
            Log.d("yeltayev22", "Maghrib set");
        } else if (settings.getBoolean(context.getString(R.string.isha_notification_key), false)) {
            setNotification(getNextNamaz(context, 6), context.getString(R.string.isha), true, context);
            Log.d("yeltayev22", "Isha set");
        }

    }

    private String getNextNamaz(Context context, int namaz) {
        String nextNamaz = null;

        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(context);

        db = dbHelper.getReadableDatabase();

        // Current date to choose from the database
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdfDate.format(c.getTime());

        // Selecting needed row from database using current date
        String query = "SELECT * FROM " + NamazTimeContract.NamazTimeEntry.TABLE_NAME
                + " WHERE " + NamazTimeContract.NamazTimeEntry.COLUMN_DATE + "='" + currentDate.trim() + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            if (namaz == 1) {
                nextNamaz = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_FAJR));
            } else if (namaz == 2) {
                nextNamaz = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_SUNRISE));
            } else if (namaz == 3) {
                nextNamaz = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_DHUHR));
            } else if (namaz == 4) {
                nextNamaz = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_ASR));
            } else if (namaz == 5) {
                nextNamaz = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_MAGHRIB));
            } else if (namaz == 6) {
                nextNamaz = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_ISHA));
            }
            cursor.close();
        }
        dbHelper.close();
        db.close();
        return nextNamaz;
    }

    private void setNotification(String prayerTime, String prayerName, boolean isNextDay, Context context) {

        if (isNextDay) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
            currentDate = sdfDate.format(c.getTime());
        }

        String[] dat = currentDate.split("-");
        int day = Integer.parseInt(dat[0]);
        int month = Integer.parseInt(dat[1]);
        int year = Integer.parseInt(dat[2]);
        String[] hourMin = prayerTime.split(":");
        int h = Integer.parseInt(hourMin[0]);
        int m = Integer.parseInt(hourMin[1]);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
        cal.set(Calendar.SECOND, 1);

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("namaz_name", prayerName);

        PendingIntent noti = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarms = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);

        alarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), noti);

    }

    private void prayTimes(Context context) {
        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(context);
        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + NamazTimeContract.NamazTimeEntry.TABLE_NAME
                + " WHERE " + NamazTimeContract.NamazTimeEntry.COLUMN_DATE + "='" + currentDate.trim() + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            fajr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_FAJR));
            sunrise = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_SUNRISE));
            dhuhr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_DHUHR));
            asr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_ASR));
            maghrib = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_MAGHRIB));
            isha = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_ISHA));
        }
        cursor.close();
        db.close();
        dbHelper.close();
    }

    public long toMillis(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int secs = Integer.parseInt(hourMin[2]);
        int hoursInMins = hour * 3600 + mins * 60 + secs;
        return hoursInMins * 1000;
    }

    private void sendNotification(Context context, Intent intent) {
        Uri alarmSound = null;

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String azan = settings.getString(context.getString(R.string.azan_key), "something");

        if (azan.compareTo(context.getString(R.string.no_value)) == 0){
            String strRingtonePreference = settings.getString(context.getString(R.string.ringtone_key),
                    "ringtone");
            alarmSound = Uri.parse(strRingtonePreference);
        } else if (azan.compareTo(context.getString(R.string.azan_kasym_value)) == 0) {
            alarmSound = Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.kasym);
        } else if (azan.compareTo(context.getString(R.string.azan_kasym_1_value)) == 0) {
            alarmSound = Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.kasym1);
        } else if (azan.compareTo(context.getString(R.string.azan_ali_ahmad_value)) == 0) {
            alarmSound = Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.ali_ahmad);
        }

        String namaz_name = intent.getStringExtra("namaz_name");


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(context.getString(R.string.azan))
                        .setContentText(namaz_name)
                        .setSound(alarmSound);

        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.InboxStyle());
        // Add as notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

        if(settings.getBoolean(context.getString(R.string.vibrate_key), false)){
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
        }

    }


}

