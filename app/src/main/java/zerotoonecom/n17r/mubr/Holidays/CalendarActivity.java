package zerotoonecom.n17r.mubr.Holidays;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zerotoonecom.n17r.mubr.R;

public class CalendarActivity extends AppCompatActivity {

    private List<Event> events = new ArrayList<>();
    private EventAdapter mAdapter;
    private int position = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setTitle(getString(R.string.event));


        final RecyclerView eventsRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mAdapter = new EventAdapter(events, getApplicationContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        eventsRecyclerView.setLayoutManager(mLayoutManager);
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventsRecyclerView.setAdapter(mAdapter);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setItemViewCacheSize(18);

        addEvents();
        eventsRecyclerView.getLayoutManager().scrollToPosition(position);
    }

    private void addEvents() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Date date = new Date();

        String strMonthFormat = "MMM";
        String strMonth = new SimpleDateFormat(strMonthFormat).format(date);
        strMonth = strMonth.substring(0, 1).toUpperCase() + strMonth.substring(1);

        String strWeekFormat = "EEE";
        String strWeek = new SimpleDateFormat(strWeekFormat).format(date).toUpperCase();

        String strGrig = getGrigDate(date);
        String hijriDate = getHijriDate(year, month, day);


        // Before Holidays
        if (((month < 2) || (month == 2 && day < 19)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 0;
        }

        // Beginning of holy 3 months
        if (month == 2 && day == 19) {
            events.add(new Event(getString(R.string.event1_month_short), getString(R.string.event1_day_short),
                    getString(R.string.event1_week_short), getString(R.string.event1_name),
                    getString(R.string.event1_hijri_date), getString(R.string.event1_grig_date), getString(R.string.event1_info), true));
            position = 0;
        } else {
            events.add(new Event(getString(R.string.event1_month_short), getString(R.string.event1_day_short),
                    getString(R.string.event1_week_short), getString(R.string.event1_name),
                    getString(R.string.event1_hijri_date), getString(R.string.event1_grig_date), getString(R.string.event1_info), false));
        }

        // Before Miraj
        if (((month < 3) || (month == 3 && day < 13)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 1;
        }
        // Miraj
        if (month == 3 && day == 13) {
            events.add(new Event(getString(R.string.event2_month_short), getString(R.string.event2_day_short),
                    getString(R.string.event2_week_short), getString(R.string.event2_name),
                    getString(R.string.event2_hijri_date), getString(R.string.event2_grig_date), getString(R.string.event2_info), true));
            position = 1;
        } else {
            events.add(new Event(getString(R.string.event2_month_short), getString(R.string.event2_day_short),
                    getString(R.string.event2_week_short), getString(R.string.event2_name),
                    getString(R.string.event2_hijri_date), getString(R.string.event2_grig_date), getString(R.string.event2_info), false));
        }

        // Before Baraat
        if (((month < 3) || (month == 3 && day < 30)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 2;
        }
        // Baraat
        if (month == 3 && day == 30) {
            events.add(new Event(getString(R.string.event3_month_short), getString(R.string.event3_day_short),
                    getString(R.string.event3_week_short), getString(R.string.event3_name),
                    getString(R.string.event3_hijri_date), getString(R.string.event3_grig_date), getString(R.string.event3_info), true));
            position = 2;
        } else {
            events.add(new Event(getString(R.string.event3_month_short), getString(R.string.event3_day_short),
                    getString(R.string.event3_week_short), getString(R.string.event3_name),
                    getString(R.string.event3_hijri_date), getString(R.string.event3_grig_date), getString(R.string.event3_info), false));
        }

        // Before Ramadan
        if (((month < 4) || (month == 4 && day < 16)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 3;
        }
        // Ramadan
        if (month == 4 && day == 16) {
            position = 3;
            events.add(new Event(getString(R.string.event4_month_short), getString(R.string.event4_day_short),
                    getString(R.string.event4_week_short), getString(R.string.event4_name),
                    getString(R.string.event4_hijri_date), getString(R.string.event4_grig_date), getString(R.string.event4_info), true));
        } else {
            events.add(new Event(getString(R.string.event4_month_short), getString(R.string.event4_day_short),
                    getString(R.string.event4_week_short), getString(R.string.event4_name),
                    getString(R.string.event4_hijri_date), getString(R.string.event4_grig_date), getString(R.string.event4_info), false));
        }

        // Before Qadr
        if (((month < 5) || (month == 5 && day < 10)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 4;
        }
        // Qadr
        if (month == 5 && day == 10) {
            position = 4;
            events.add(new Event(getString(R.string.event5_month_short), getString(R.string.event5_day_short),
                    getString(R.string.event5_week_short), getString(R.string.event5_name),
                    getString(R.string.event5_hijri_date), getString(R.string.event5_grig_date), getString(R.string.event5_info), true));
        } else {
            events.add(new Event(getString(R.string.event5_month_short), getString(R.string.event5_day_short),
                    getString(R.string.event5_week_short), getString(R.string.event5_name),
                    getString(R.string.event5_hijri_date), getString(R.string.event5_grig_date), getString(R.string.event5_info), false));
        }



        // Before Eid Ramadan
        if (((month < 5) || (month == 5 && day < 15)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 5;
        }
        // Qadr
        if (month == 5 && day == 15) {
            position = 5;
            events.add(new Event(getString(R.string.event6_month_short), getString(R.string.event6_day_short),
                    getString(R.string.event6_week_short), getString(R.string.event6_name),
                    getString(R.string.event6_hijri_date), getString(R.string.event6_grig_date), getString(R.string.event6_info), true));
        } else {
            events.add(new Event(getString(R.string.event6_month_short), getString(R.string.event6_day_short),
                    getString(R.string.event6_week_short), getString(R.string.event6_name),
                    getString(R.string.event6_hijri_date), getString(R.string.event6_grig_date), getString(R.string.event6_info), false));
        }


        // before Arafa
        if (((month < 7) || (month == 7 && day < 20)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 6;
        }
        // Arafa day
        if (month == 7 && day == 20) {
            events.add(new Event(getString(R.string.event9_month_short), getString(R.string.event9_day_short),
                    getString(R.string.event9_week_short), getString(R.string.event9_name),
                    getString(R.string.event9_hijri_date), getString(R.string.event9_grig_date), getString(R.string.event9_info), true));
            position = 6;
        } else {
            events.add(new Event(getString(R.string.event9_month_short), getString(R.string.event9_day_short),
                    getString(R.string.event9_week_short), getString(R.string.event9_name),
                    getString(R.string.event9_hijri_date), getString(R.string.event9_grig_date), getString(R.string.event9_info), false));
        }

        // Qurban 1
        if (month == 7 && day == 21) {
            events.add(new Event(getString(R.string.event10_month_short), getString(R.string.event10_day_short),
                    getString(R.string.event10_week_short), getString(R.string.event10_name),
                    getString(R.string.event10_hijri_date), getString(R.string.event10_grig_date), getString(R.string.event10_info), true));
            position = 7;
        } else {
            events.add(new Event(getString(R.string.event10_month_short), getString(R.string.event10_day_short),
                    getString(R.string.event10_week_short), getString(R.string.event10_name),
                    getString(R.string.event10_hijri_date), getString(R.string.event10_grig_date), getString(R.string.event10_info), false));
        }

        // Today before Hijri NY
        if (((month < 8) || (month == 8 && day < 11)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 8;
        }

        // Hijri
        if (month == 8 && day == 11) {
            events.add(new Event(getString(R.string.event14_month_short), getString(R.string.event14_day_short),
                    getString(R.string.event14_week_short), getString(R.string.event14_name),
                    getString(R.string.event14_hijri_date), getString(R.string.event14_grig_date), getString(R.string.event14_info), true));
            position = 8;
        } else {
            events.add(new Event(getString(R.string.event14_month_short), getString(R.string.event14_day_short),
                    getString(R.string.event14_week_short), getString(R.string.event14_name),
                    getString(R.string.event14_hijri_date), getString(R.string.event14_grig_date), getString(R.string.event14_info), false));
        }

        // Today before Aashoora
        if (((month < 8) || (month == 8 && day < 20)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 9;
        }
        // Aashoora
        if (month == 8 && day == 20) {
            events.add(new Event(getString(R.string.event15_month_short), getString(R.string.event15_day_short),
                    getString(R.string.event15_week_short), getString(R.string.event15_name),
                    getString(R.string.event15_hijri_date), getString(R.string.event15_grig_date), getString(R.string.event15_info), true));
            position = 9;
        } else {
            events.add(new Event(getString(R.string.event15_month_short), getString(R.string.event15_day_short),
                    getString(R.string.event15_week_short), getString(R.string.event15_name),
                    getString(R.string.event15_hijri_date), getString(R.string.event15_grig_date), getString(R.string.event15_info), false));
        }

        // Today before Mawlid
        if (((month < 10) || (month == 10 && day < 19)) && position == -1) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 10;
        }

        // Mawlid
        if (month == 10 && day == 19) {
            events.add(new Event(getString(R.string.event16_month_short), getString(R.string.event16_day_short),
                    getString(R.string.event16_week_short), getString(R.string.event16_name),
                    getString(R.string.event16_hijri_date), getString(R.string.event16_grig_date), getString(R.string.event16_info), true));
            position = 10;
        } else {
            events.add(new Event(getString(R.string.event16_month_short), getString(R.string.event16_day_short),
                    getString(R.string.event16_week_short), getString(R.string.event16_name),
                    getString(R.string.event16_hijri_date), getString(R.string.event16_grig_date), getString(R.string.event16_info), false));
        }

        // Today after all holidays
        if ((month == 10 && day > 19) || (month >= 11 && day > 1)) {
            events.add(new Event(strMonth, String.valueOf(day),
                    strWeek, getString(R.string.today), String.valueOf(hijriDate), strGrig, getString(R.string.today), true));
            position = 11;
        }
    }

    @NonNull
    private String getHijriMonth(int monthHijri) {
        switch (monthHijri){
            case 1:
                return getString(R.string.muharram);
            case 2:
                return getString(R.string.safar);
            case 3:
                return getString(R.string.rabilalawwal);
            case 4:
                return getString(R.string.rabialakhar);
            case 5:
                return getString(R.string.jumadaalawwal);
            case 6:
                return getString(R.string.jumadaalakhirah);
            case 7:
                return getString(R.string.rajab);
            case 8:
                return getString(R.string.shaban);
            case 9:
                return getString(R.string.ramadan);
            case 10:
                return getString(R.string.shawwal);
            case 11:
                return getString(R.string.dhulqadah);
            case 12:
                return getString(R.string.dhulhijjah);
        }
        return "";
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


    public String getGrigDate(Date date) {
        String strGrigFormat = "d-MMMM-yyyy";
        String strGrigDateFormat = new SimpleDateFormat(strGrigFormat).format(date);
        String[] strGrigDate = strGrigDateFormat.split("-");
        String dayGrig = strGrigDate[0];
        String monthGrig = strGrigDate[1];
        String yearGrig = strGrigDate[2];
        monthGrig = monthGrig.substring(0, 1).toUpperCase() + monthGrig.substring(1);
        String strGrig = String.valueOf(dayGrig) + " " + String.valueOf(monthGrig) + ", " + String.valueOf(yearGrig);
        return strGrig;
    }

    public String getHijriDate(int year, int month, int day) {
        Chronology iso = ISOChronology.getInstanceUTC();
        Chronology hijri = IslamicChronology.getInstance();
        LocalDate todayIso;
        if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11) {
            if (day == 31) {
                todayIso = new LocalDate(year, month+2, 1, iso);
            } else {
                todayIso = new LocalDate(year, month+1, day+1, iso);
            }
        } else {
            if (day == 30) {
                todayIso = new LocalDate(year, month+2, 1, iso);
            } else {
                todayIso = new LocalDate(year, month+1, day+1, iso);
            }
        }
        LocalDate localHijriDate = new LocalDate(todayIso.toDateTimeAtCurrentTime(), hijri);
        String[] strHijriDate = localHijriDate.toString().split("-");
        int yearHijri = Integer.parseInt(strHijriDate[0]);
        int monthHijri = Integer.parseInt(strHijriDate[1]);
        int dayHijri = Integer.parseInt(strHijriDate[2]);
        String hijriDate = String.valueOf(dayHijri) + " " + String.valueOf(getHijriMonth(monthHijri)) + ", " + String.valueOf(yearHijri);
        return hijriDate;
    }
}
