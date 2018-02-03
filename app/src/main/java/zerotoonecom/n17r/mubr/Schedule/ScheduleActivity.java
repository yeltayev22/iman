package zerotoonecom.n17r.mubr.Schedule;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import zerotoonecom.n17r.mubr.R;
import zerotoonecom.n17r.mubr.Time.NamazTimeContract;
import zerotoonecom.n17r.mubr.Time.NamazTimeDbHelper;

public class ScheduleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout table;
    Spinner month;
    SQLiteDatabase db;
    private String date;
    private String fajr;
    private String sunrise;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;
    int[] months;
    ArrayList<Day> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        setTitle(getString(R.string.schedule));

        table = (TableLayout) findViewById(R.id.table);
        month = (Spinner) findViewById(R.id.month);
        month.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        month.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        int it = Integer.parseInt(String.valueOf(calendar.get(Calendar.MONTH)));
        month.setSelection(it);
        if (isLeap(year)) {
            months = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        } else {
            months = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        }
        days = new ArrayList<Day>();
        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(this);
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NamazTimeContract.NamazTimeEntry.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                date = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_DATE));
                fajr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_FAJR));
                sunrise = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_SUNRISE));
                dhuhr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_DHUHR));
                asr = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_ASR));
                maghrib = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_MAGHRIB));
                isha = cursor.getString(cursor.getColumnIndex(NamazTimeContract.NamazTimeEntry.COLUMN_ISHA));

                days.add(new Day(date, fajr, sunrise, dhuhr, asr, maghrib, isha));
                cursor.moveToNext();
            }
        }
        cursor.close();

    }

    private boolean isLeap(String year) {
        GregorianCalendar cal = new GregorianCalendar();
        if (cal.isLeapYear(Integer.parseInt(year))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        monthSchedule(position);
    }

    private void monthSchedule(int position) {
        table.removeAllViews();

        TableRow headingRow = new TableRow(this);
        headingRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                0));

        headingRow.setBackgroundResource(R.color.white);

        TextView date = new TextView(this);
        TextView fajr = new TextView(this);
        TextView sunrise = new TextView(this);
        TextView dhuhr = new TextView(this);
        TextView asr = new TextView(this);
        TextView maghrib = new TextView(this);
        TextView isha = new TextView(this);

        date.setText(R.string.table_date);
        date.setGravity(Gravity.CENTER_HORIZONTAL);
        headingRow.addView(date, 0);

        fajr.setText(R.string.fajr);
        fajr.setGravity(Gravity.CENTER_HORIZONTAL);
        headingRow.addView(fajr, 1);

        sunrise.setText(R.string.sunrise);
        sunrise.setGravity(Gravity.CENTER_HORIZONTAL);
        headingRow.addView(sunrise, 2);

        dhuhr.setText(R.string.dhuhr);
        dhuhr.setGravity(Gravity.CENTER_HORIZONTAL);
        headingRow.addView(dhuhr, 3);

        asr.setText(R.string.asr);
        asr.setGravity(Gravity.CENTER_HORIZONTAL);
        headingRow.addView(asr, 4);

        maghrib.setText(R.string.maghrib);
        maghrib.setGravity(Gravity.CENTER_HORIZONTAL);
        headingRow.addView(maghrib, 5);

        isha.setText(R.string.isha);
        isha.setGravity(Gravity.CENTER_HORIZONTAL);
        headingRow.addView(isha, 6);

        table.addView(headingRow, 0);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdfDate.format(c.getTime());
        String[] dat = currentDate.split("-");
        int currentDay = Integer.parseInt(dat[0]);
        int currentMonth = Integer.parseInt(dat[1]);

        int value = 0;
        for (int j = 0; j < position; j++) {
            value += months[j];
        }
        for (int i = 1; i <= months[position]; i++)
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    0));
            if (i % 2 != 0)
                tableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            else
                tableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.whiteGreen));

            if (i == currentDay && currentMonth == position + 1)
                tableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.current_time));

            TextView date_tv = new TextView(this);
            TextView fajr_tv = new TextView(this);
            TextView sunrise_tv = new TextView(this);
            TextView dhuhr_tv = new TextView(this);
            TextView asr_tv = new TextView(this);
            TextView maghrib_tv = new TextView(this);
            TextView isha_tv = new TextView(this);

            date_tv.setText(Integer.toString(i));
            date_tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(date_tv, 0);

            fajr_tv.setText(days.get(value + (i - 1)).getFajr());
            fajr_tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(fajr_tv, 1);

            sunrise_tv.setText(days.get(value + (i - 1)).getSunrise());
            sunrise_tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(sunrise_tv, 2);

            dhuhr_tv.setText(days.get(value + (i - 1)).getDhuhr());
            dhuhr_tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(dhuhr_tv, 3);

            asr_tv.setText(days.get(value + (i - 1)).getAsr());
            asr_tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(asr_tv, 4);

            maghrib_tv.setText(days.get(value + (i - 1)).getMaghrib());
            maghrib_tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(maghrib_tv, 5);

            isha_tv.setText(days.get(value + (i - 1)).getIsha());
            isha_tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(isha_tv, 6);

            table.addView(tableRow, i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
