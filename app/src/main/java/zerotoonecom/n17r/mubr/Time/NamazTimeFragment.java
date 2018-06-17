package zerotoonecom.n17r.mubr.Time;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import zerotoonecom.n17r.mubr.R;
import zerotoonecom.n17r.mubr.data.JSONParser;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nurdaulet Yeltayev (@yeltayev22)
 */

public class NamazTimeFragment extends Fragment {

    TextView fajrTime;
    TextView sunriseTime;
    TextView dhuhrTime;
    TextView asrTime;
    TextView maghribTime;
    TextView ishaTime;

    TextView fajr_tv;
    TextView sunrise_tv;
    TextView dhuhr_tv;
    TextView asr_tv;
    TextView maghrib_tv;
    TextView isha_tv;

    RelativeLayout fajr_layout;
    RelativeLayout sunrise_layout;
    RelativeLayout dhuhr_layout;
    RelativeLayout asr_layout;
    RelativeLayout maghrib_layout;
    RelativeLayout isha_layout;

    TextView time_tv;
    TextView date_tv;
    TextView city_tv;
    TextView left_time_tv;
    TextView left_time;
    ViewSwitcher view_switcher;
    public String MUFTYAT_URL = "";

    private SharedPreferences mSharedPreferences;

    private SQLiteDatabase db;

    //JSON Node Names
    private static final String TAG_RESULT = "result";
    private static final String TAG_DATE = "date";
    private static final String TAG_FAJR = "Fajr";
    private static final String TAG_SUNRISE = "Sunrise";
    private static final String TAG_DHUHR = "Dhuhr";
    private static final String TAG_ASR = "Asr";
    private static final String TAG_MAGHRIB = "Maghrib";
    private static final String TAG_ISHA = "Isha";

    private boolean mActive = false;
    private Handler mHandler;
    private boolean mTimings = false;
    private long min = 86400001;

    public boolean[] colorTime = {false, false, false, false, false, false};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Handler to update time
        mHandler = new Handler();

        // Initializing Views
        View rootView = inflater.inflate(R.layout.namaz, container, false);
        fajrTime = (TextView) rootView.findViewById(R.id.fajr_time);
        sunriseTime = (TextView) rootView.findViewById(R.id.sunrise_time);
        dhuhrTime = (TextView) rootView.findViewById(R.id.dhuhr_time);
        asrTime = (TextView) rootView.findViewById(R.id.asr_time);
        maghribTime = (TextView) rootView.findViewById(R.id.maghrib_time);
        ishaTime = (TextView) rootView.findViewById(R.id.isha_time);
        view_switcher = (ViewSwitcher) rootView.findViewById(R.id.view_switcher);

        fajr_tv = (TextView) rootView.findViewById(R.id.fajr_tv);
        sunrise_tv = (TextView) rootView.findViewById(R.id.sunrise_tv);
        dhuhr_tv = (TextView) rootView.findViewById(R.id.dhuhr_tv);
        asr_tv = (TextView) rootView.findViewById(R.id.asr_tv);
        maghrib_tv = (TextView) rootView.findViewById(R.id.maghrib_tv);
        isha_tv = (TextView) rootView.findViewById(R.id.isha_tv);

        date_tv = (TextView) rootView.findViewById(R.id.date);
        city_tv = (TextView) rootView.findViewById(R.id.city_tv);
        time_tv = (TextView) rootView.findViewById(R.id.time_tv);
        left_time_tv = (TextView) rootView.findViewById(R.id.left_time_tv);
        left_time = (TextView) rootView.findViewById(R.id.left_time);

        fajr_layout = (RelativeLayout) rootView.findViewById(R.id.fajr_layout);
        sunrise_layout = (RelativeLayout) rootView.findViewById(R.id.sunrise_layout);
        dhuhr_layout = (RelativeLayout) rootView.findViewById(R.id.dhuhr_layout);
        asr_layout = (RelativeLayout) rootView.findViewById(R.id.asr_layout);
        maghrib_layout = (RelativeLayout) rootView.findViewById(R.id.maghrib_layout);
        isha_layout = (RelativeLayout) rootView.findViewById(R.id.isha_layout);

        // Saving last chosen city to which uploaded data
        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
        String currentCity = sharedPref.getString(getString(R.string.choose_current_city_key), "somecity");

        // Currently selected city in preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String selectedCity = mSharedPreferences.getString(getString(R.string.choose_city_key), "somecity");

        selectCity(selectedCity);

        if (isOnline()) {

            SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(getString(R.string.choose_current_city_key), selectedCity);
            editor.commit();

            if (currentCity.compareTo(selectedCity) == 0 && rowExists()) {
                getTimings();
            } else {
                delete();
                view_switcher.showNext();
                NamazTimeAsyncTask task = new NamazTimeAsyncTask();
                task.execute();
            }
        } else {
            if (currentCity.compareTo(selectedCity) == 0 && rowExists()) {
                getTimings();
            } else {
                Toast.makeText(getContext(), getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
            }
        }

        return rootView;
    }


    private boolean rowExists() {
        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(getContext());
        db = dbHelper.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + NamazTimeContract.NamazTimeEntry.TABLE_NAME, null);
        if (mCursor.moveToFirst()) {
            // DO SOMETHING WITH CURSOR
            mCursor.close();
            db.close();
            dbHelper.close();
            return true;
        } else {
            // I AM EMPTY
            mCursor.close();
            db.close();
            dbHelper.close();
            return false;
        }
    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            if (mActive && mTimings) {
                computeLeftTime();
                // Setting current time and date with updating
                SimpleDateFormat sdfCurrentTime = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdfCurrentWeekDay = new SimpleDateFormat("EEEE", Locale.getDefault());
                SimpleDateFormat sdfCurrentDay = new SimpleDateFormat("d", Locale.getDefault());
                SimpleDateFormat sdfCurrentMonth = new SimpleDateFormat("MMMM", Locale.getDefault());
                if (time_tv != null) {
                    String strWeekDay = sdfCurrentWeekDay.format(new Date(System.currentTimeMillis())).toLowerCase();
                    strWeekDay = strWeekDay.substring(0, 1).toUpperCase() + strWeekDay.substring(1);
                    String strMonth = sdfCurrentMonth.format(new Date(System.currentTimeMillis())).toLowerCase();
                    strMonth = strMonth.substring(0, 1).toUpperCase() + strMonth.substring(1);
                    date_tv.setText(strWeekDay + ", " + sdfCurrentDay.format(new Date(System.currentTimeMillis())) + " " + strMonth);
                    time_tv.setText(sdfCurrentTime.format(new Date(System.currentTimeMillis())));
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        }
    };

    private void computeLeftTime() {
        min = 86400001;

        String fajr = fajrTime.getText().toString();
        String sunrise = sunriseTime.getText().toString();
        String dhuhr = dhuhrTime.getText().toString();
        String asr = asrTime.getText().toString();
        String maghrib = maghribTime.getText().toString();
        String isha = ishaTime.getText().toString();

        // Getting current time in Millis
        SimpleDateFormat sdfCurrentSeconds = new SimpleDateFormat("HH:mm:ss");
        String time = sdfCurrentSeconds.format(new Date(System.currentTimeMillis()));
        long currentTimeInMillis = toMillis(time);

        // Taking differences of timings with current time to get the nearest one
        long compareFajr = toMillis(fajr + ":00") - currentTimeInMillis;
        long compareSunrise = toMillis(sunrise + ":00") - currentTimeInMillis;
        long compareDhuhr = toMillis(dhuhr + ":00") - currentTimeInMillis;
        long compareAsr = toMillis(asr + ":00") - currentTimeInMillis;
        long compareMaghrib = toMillis(maghrib + ":00") - currentTimeInMillis;
        long compareIsha = toMillis(isha + ":00") - currentTimeInMillis;

        if (compareFajr > 0) {
            min = compareFajr;
            thisTime(isha_layout, isha_tv, ishaTime, 5);
            notThisTime(fajr_layout, fajr_tv, fajrTime);
            notThisTime(sunrise_layout, sunrise_tv, sunriseTime);
            notThisTime(dhuhr_layout, dhuhr_tv, dhuhrTime);
            notThisTime(asr_layout, asr_tv, asrTime);
            notThisTime(maghrib_layout, maghrib_tv, maghribTime);
        } else if (compareSunrise > 0) {
            min = Math.min(min, compareSunrise);
            thisTime(fajr_layout, fajr_tv, fajrTime, 0);
            notThisTime(isha_layout, isha_tv, ishaTime);
            notThisTime(sunrise_layout, sunrise_tv, sunriseTime);
            notThisTime(dhuhr_layout, dhuhr_tv, dhuhrTime);
            notThisTime(asr_layout, asr_tv, asrTime);
            notThisTime(maghrib_layout, maghrib_tv, maghribTime);
        } else if (compareDhuhr > 0) {
            min = Math.min(min, compareDhuhr);
            thisTime(sunrise_layout, sunrise_tv, sunriseTime, 1);
            notThisTime(fajr_layout, fajr_tv, fajrTime);
            notThisTime(isha_layout, isha_tv, ishaTime);
            notThisTime(dhuhr_layout, dhuhr_tv, dhuhrTime);
            notThisTime(asr_layout, asr_tv, asrTime);
            notThisTime(maghrib_layout, maghrib_tv, maghribTime);
        } else if (compareAsr > 0) {
            min = Math.min(min, compareAsr);
            thisTime(dhuhr_layout, dhuhr_tv, dhuhrTime, 2);
            notThisTime(fajr_layout, fajr_tv, fajrTime);
            notThisTime(isha_layout, isha_tv, ishaTime);
            notThisTime(sunrise_layout, sunrise_tv, sunriseTime);
            notThisTime(asr_layout, asr_tv, asrTime);
            notThisTime(maghrib_layout, maghrib_tv, maghribTime);
        } else if (compareMaghrib > 0) {
            min = Math.min(min, compareMaghrib);
            thisTime(asr_layout, asr_tv, asrTime, 3);
            notThisTime(fajr_layout, fajr_tv, fajrTime);
            notThisTime(isha_layout, isha_tv, ishaTime);
            notThisTime(dhuhr_layout, dhuhr_tv, dhuhrTime);
            notThisTime(sunrise_layout, sunrise_tv, sunriseTime);
            notThisTime(maghrib_layout, maghrib_tv, maghribTime);
        } else if (compareIsha > 0) {
            min = Math.min(min, compareIsha);
            thisTime(maghrib_layout, maghrib_tv, maghribTime, 4);
            notThisTime(fajr_layout, fajr_tv, fajrTime);
            notThisTime(isha_layout, isha_tv, ishaTime);
            notThisTime(dhuhr_layout, dhuhr_tv, dhuhrTime);
            notThisTime(asr_layout, asr_tv, asrTime);
            notThisTime(sunrise_layout, sunrise_tv, sunriseTime);
        } else {
            min = (86400000 - currentTimeInMillis) + toMillis(nextFajr() + ":00");
            thisTime(isha_layout, isha_tv, ishaTime, 5);
            notThisTime(fajr_layout, fajr_tv, fajrTime);
            notThisTime(sunrise_layout, sunrise_tv, sunriseTime);
            notThisTime(dhuhr_layout, dhuhr_tv, dhuhrTime);
            notThisTime(asr_layout, asr_tv, asrTime);
            notThisTime(maghrib_layout, maghrib_tv, maghribTime);
        }

        // Converting left millis to hours, minutes and seconds
        int seconds = (int) (min / 1000) % 60;
        int minutes = (int) ((min / (1000 * 60)) % 60);
        int hours = (int) ((min / (1000 * 60 * 60)) % 24);

        /** Checking is the numbers les than 10, if it is true append 0
         * before the number as (03:03:06), else set as (13:25:36)
         */
        if (hours >= 10) {
            left_time_tv.setText(String.valueOf(hours + ":"));
        } else {
            left_time_tv.setText(String.valueOf("0" + hours + ":"));
        }

        if (minutes >= 10) {
            left_time_tv.append(String.valueOf(minutes + ":"));
        } else {
            left_time_tv.append(String.valueOf("0" + minutes + ":"));
        }

        if (seconds >= 10) {
            left_time_tv.append(String.valueOf(seconds));
        } else {
            left_time_tv.append(String.valueOf("0" + seconds));
        }
        // for widget
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = preferences.edit();
        for (int i = 0; i < 6; i++) {
            if (colorTime[i]) {
                edit.putInt(String.valueOf(R.string.color), i);
                edit.commit();

            }
        }
    }

    private String nextFajr() {

        String fajr = null;

        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(getContext());

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
            fajr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_FAJR));
            cursor.close();
        }
        dbHelper.close();
        db.close();
        return fajr;
    }

    private void notThisTime(RelativeLayout layout, TextView tv, TextView time) {
        layout.setBackgroundResource(0);
        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        time.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    private void thisTime(RelativeLayout layout, TextView tv, TextView time, int pos) {
        for (int i = 0; i < 6; i++)
            colorTime[i] = false;
        colorTime[pos] = true;

        layout.setBackgroundResource(R.drawable.current_time_rectangle);
        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        time.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
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
    public void onResume() {
        super.onResume();
        mActive = true;
        mRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String selectedCity = mSharedPreferences.getString(getString(R.string.choose_city_key), "astana");
        editor.putString("selected_city", selectedCity);

        editor.commit();

        mActive = false;
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    // Deleting last uploaded timings from database, if city has changed
    private void delete() {
        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(getContext());
        db = dbHelper.getWritableDatabase();
        db.delete(NamazTimeContract.NamazTimeEntry.TABLE_NAME, null, null);
        dbHelper.close();
        db.close();
    }

    private void getTimings() {

        //For current time
        mHandler.post(mRunnable);

        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(getContext());
        db = dbHelper.getReadableDatabase();

        // Current date to choose from the database
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdfDate.format(c.getTime());

        // Selecting needed row from database using current date
        String query = "SELECT * FROM " + NamazTimeContract.NamazTimeEntry.TABLE_NAME
                + " WHERE " + NamazTimeContract.NamazTimeEntry.COLUMN_DATE + "='" + currentDate.trim() + "'";
        Cursor cursor = db.rawQuery(query, null);

        // Getting timings and set to the textViews
        if (cursor != null && cursor.moveToFirst()) {
            String fajr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_FAJR));
            String sunrise = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_SUNRISE));
            String dhuhr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_DHUHR));
            String asr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_ASR));
            String maghrib = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_MAGHRIB));
            String isha = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_ISHA));

            fajrTime.setText(fajr);
            sunriseTime.setText(sunrise);
            dhuhrTime.setText(dhuhr);
            asrTime.setText(asr);
            maghribTime.setText(maghrib);
            ishaTime.setText(isha);

            cursor.close();
            db.close();
            dbHelper.close();

            // for widget
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(String.valueOf(R.string.city), city_tv.getText().toString());
            edit.putString(String.valueOf(R.string.fajr), fajr);
            edit.putString(String.valueOf(R.string.sunrise), sunrise);
            edit.putString(String.valueOf(R.string.dhuhr), dhuhr);
            edit.putString(String.valueOf(R.string.asr), asr);
            edit.putString(String.valueOf(R.string.maghrib), maghrib);
            edit.putString(String.valueOf(R.string.isha), isha);
            edit.commit();

            mTimings = true;
        }
    }

    // Selecting city from the preferences and setting textViews to this city
    private void selectCity(String selectedCity) {
        Calendar calendar = Calendar.getInstance();
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        switch (selectedCity) {
            case "astana":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/51.133333/71.433333";
                city_tv.setText(R.string.astana_label);
                break;
            case "almaty":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/43.25/76.9";
                city_tv.setText(R.string.almaty_label);
                break;
            case "atyrau":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/47.116667/51.883333";
                city_tv.setText(R.string.atyrau_label);
                break;
            case "aqtobe":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/50.3/57.166667";
                city_tv.setText(R.string.aqtobe_label);
                break;
            case "aqtau":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/43.65/51.15";
                city_tv.setText(R.string.aqtau_label);
                break;
            case "taldykorgan":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/45.016667/78.366667";
                city_tv.setText(getString(R.string.taldykorgan_label));
                break;
            case "kokshetau":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/53.291667/69.391667";
                city_tv.setText(getString(R.string.kokshetau_label));
                break;
            case "taraz":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/42.883333/71.366667";
                city_tv.setText(getString(R.string.taraz_label));
                break;
            case "semey":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/50.411111/80.2275";
                city_tv.setText(getString(R.string.semey_label));
                break;
            case "karagandy":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/49.8/73.116667";
                city_tv.setText(getString(R.string.karagandy_label));
                break;
            case "kostanay":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/53.214917/63.631031";
                city_tv.setText(getString(R.string.kostanay_label));
                break;
            case "kyzylorda":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/44.85/65.516667";
                city_tv.setText(getString(R.string.kyzylorda_label));
                break;
            case "oral":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/51.212184/51.367069";
                city_tv.setText(getString(R.string.oral_label));
                break;
            case "pavlodar":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/52.315556/76.956389";
                city_tv.setText(getString(R.string.pavlodar_label));
                break;
            case "petropavlovsk":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/54.862222/69.140833";
                city_tv.setText(getString(R.string.petropavlovsk_label));
                break;
            case "shymkent":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/42.3/69.6";
                city_tv.setText(getString(R.string.shymkent_label));
                break;
            case "turkestan":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/43.3/68.243611";
                city_tv.setText(getString(R.string.turkestan_label));
                break;
            case "oskemen":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/49.948759/82.628459";
                city_tv.setText(getString(R.string.oskemen_label));
                break;
            case "zhezqazghan":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/47.783333/67.7";
                city_tv.setText(getString(R.string.zhezqazghan_label));
                break;
            case "usharal":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/46.169722/80.939444";
                city_tv.setText(getString(R.string.usharal_label));
                break;
            case "hromtau":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/50.250278/58.434722";
                city_tv.setText(getString(R.string.hromtau_label));
                break;
            case "temirtau":
                MUFTYAT_URL = "http://namaz.muftyat.kz/api/times/" + year + "/50.058756/72.953424";
                city_tv.setText(getString(R.string.temirtau_label));
                break;
        }

    }

    private class NamazTimeAsyncTask extends AsyncTask<URL, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            left_time.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            Uri baseUri = Uri.parse(MUFTYAT_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            URL url = null;
            try {
                url = new URL(uriBuilder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONParser jParser = new JSONParser();
            String json = null;
            try {
                json = jParser.getJSONFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            extractFeatureFromJsonToDatabase(json);
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            getTimings();
            view_switcher.showNext();
            left_time.setVisibility(View.VISIBLE);
        }
    }

    private void extractFeatureFromJsonToDatabase(String json) {
        try {
            NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(getContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();

            JSONObject baseJsonResponse = new JSONObject(json);
            JSONArray result = baseJsonResponse.getJSONArray(TAG_RESULT);
            for (int i = 0; i < result.length(); i++) {
                JSONArray month = result.getJSONArray(i);
                for (int j = 0; j < month.length(); j++) {
                    JSONObject day = month.getJSONObject(j);

                    String date = day.getString(TAG_DATE);
                    String fajr = day.getString(TAG_FAJR);
                    String sunrise = day.getString(TAG_SUNRISE);
                    String dhuhr = day.getString(TAG_DHUHR);
                    String asr = day.getString(TAG_ASR);
                    String maghrib = day.getString(TAG_MAGHRIB);
                    String isha = day.getString(TAG_ISHA);

                    cv.put(NamazTimeContract.NamazTimeEntry.COLUMN_DATE, date);
                    cv.put(NamazTimeContract.NamazTimeEntry.COLUMN_FAJR, fajr);
                    cv.put(NamazTimeContract.NamazTimeEntry.COLUMN_SUNRISE, sunrise);
                    cv.put(NamazTimeContract.NamazTimeEntry.COLUMN_DHUHR, dhuhr);
                    cv.put(NamazTimeContract.NamazTimeEntry.COLUMN_ASR, asr);
                    cv.put(NamazTimeContract.NamazTimeEntry.COLUMN_MAGHRIB, maghrib);
                    cv.put(NamazTimeContract.NamazTimeEntry.COLUMN_ISHA, isha);

                    db.insert(NamazTimeContract.NamazTimeEntry.TABLE_NAME, null, cv);
                }
            }
            dbHelper.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();

    }

    // Checking internet connection
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}