package zerotoonecom.n17r.mubr;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import zerotoonecom.n17r.mubr.FirstLaunch.FirstLaunchActivity;
import zerotoonecom.n17r.mubr.Notification.NotificationReceiver;
import zerotoonecom.n17r.mubr.Settings.SettingsActivity;
import zerotoonecom.n17r.mubr.Time.NamazTimeContract;
import zerotoonecom.n17r.mubr.Time.NamazTimeDbHelper;
import zerotoonecom.n17r.mubr.data.CategoryAdapter;

public class MainActivity extends AppCompatActivity {

    private String chosenLang;
    private SQLiteDatabase db;
    private String fajr, sunrise, dhuhr, asr, maghrib, isha;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Intent intent = new Intent(this, FirstLaunchActivity.class);
            startActivity(intent);
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }
        chosenLang = settings.getString(getString(R.string.language_key), "ru");

        // Chosing the right language
        Locale locale = new Locale(chosenLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = sdfDate.format(c.getTime());
        SimpleDateFormat sdfCurrentSeconds = new SimpleDateFormat("HH:mm:ss");
        String time = sdfCurrentSeconds.format(new Date(System.currentTimeMillis()));
        long currentTimeInMillis = toMillis(time);

        prayTimes();

        Intent intent = new Intent(this, NotificationReceiver.class);

        PendingIntent noti = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarms = (AlarmManager) this.getSystemService(
                Context.ALARM_SERVICE);

        alarms.cancel(noti);

        if (settings.getBoolean(getString(R.string.fajr_notification_key), false) &&
                toMillis(fajr + ":00") > currentTimeInMillis) {
            setNotification(fajr, getString(R.string.fajr), false, intent, noti, alarms);
            Log.d("yeltayev22", "Fajr set");
        } else if (settings.getBoolean(getString(R.string.sunrise_notification_key), false) &&
                toMillis(sunrise + ":00") > currentTimeInMillis) {
            setNotification(sunrise, getString(R.string.sunrise), false, intent, noti, alarms);
            Log.d("yeltayev22", "Sunrise set");
        } else if (settings.getBoolean(getString(R.string.dhuhr_notification_key), false) &&
                toMillis(dhuhr + ":00") > currentTimeInMillis) {
            setNotification(dhuhr, getString(R.string.dhuhr), false, intent, noti, alarms);
            Log.d("yeltayev22", "Dhuhr set");
        } else if (settings.getBoolean(getString(R.string.asr_notification_key), false) &&
                toMillis(asr + ":00") > currentTimeInMillis) {
            setNotification(asr, getString(R.string.asr), false, intent, noti, alarms);
            Log.d("yeltayev22", "Asr set");
        } else if (settings.getBoolean(getString(R.string.maghrib_notification_key), false) &&
                toMillis(maghrib + ":00") > currentTimeInMillis) {
            setNotification(maghrib, getString(R.string.maghrib), false, intent, noti, alarms);
            Log.d("yeltayev22", "Maghrib set");
        } else if (settings.getBoolean(getString(R.string.isha_notification_key), false) &&
                toMillis(isha + ":00") > currentTimeInMillis) {
            setNotification(isha, getString(R.string.isha), false, intent, noti, alarms);
            Log.d("yeltayev22", "Isha set");
        } else if (settings.getBoolean(getString(R.string.fajr_notification_key), false)) {
            setNotification(getNextNamaz(1), getString(R.string.fajr), true, intent, noti, alarms);
            Log.d("yeltayev22", "Fajr set");
        } else if (settings.getBoolean(getString(R.string.sunrise_notification_key), false)) {
            setNotification(getNextNamaz(2), getString(R.string.sunrise), true, intent, noti, alarms);
            Log.d("yeltayev22", "Sunrise set");
        } else if (settings.getBoolean(getString(R.string.dhuhr_notification_key), false)) {
            setNotification(getNextNamaz(3), getString(R.string.dhuhr), true, intent, noti, alarms);
            Log.d("yeltayev22", "Dhuhr set");
        } else if (settings.getBoolean(getString(R.string.asr_notification_key), false)){
            setNotification(getNextNamaz(4), getString(R.string.asr), true, intent, noti, alarms);
            Log.d("yeltayev22", "Asr set");
        } else if (settings.getBoolean(getString(R.string.maghrib_notification_key), false)) {
            setNotification(getNextNamaz(5), getString(R.string.maghrib), true, intent, noti, alarms);
            Log.d("yeltayev22", "Maghrib set");
        } else if (settings.getBoolean(getString(R.string.isha_notification_key), false)) {
            setNotification(getNextNamaz(6), getString(R.string.isha), true, intent, noti, alarms);
            Log.d("yeltayev22", "Isha set");
        }

    }

    private String getNextNamaz(int namaz) {
        String nextNamaz = null;

        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(this);

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

    private void setNotification(String prayerTime, String prayerName, boolean isNextDay,
                                 Intent intent, PendingIntent noti, AlarmManager alarms) {

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

        intent = new Intent(this, NotificationReceiver.class);

        intent.putExtra("namaz_name", prayerName);

        noti = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarms = (AlarmManager) this.getSystemService(
                Context.ALARM_SERVICE);

        alarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), noti);

    }

    private void prayTimes() {
        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(this);
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
    }

    public long toMillis(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int secs = Integer.parseInt(hourMin[2]);
        int hoursInMins = hour * 3600 + mins * 60 + secs;
        return hoursInMins * 1000;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
