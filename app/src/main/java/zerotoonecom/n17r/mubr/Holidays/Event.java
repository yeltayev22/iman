package zerotoonecom.n17r.mubr.Holidays;

/**
 * Created by Nurdaulet Yeltayev (yeltayev22) on 09.08.2017.
 */

public class Event {

    private String month_short;
    private String day_short;
    private String weekDay_short;
    private String event_name;
    private String hijriDate;
    private String grigDate;
    private String description;
    private boolean isToday;

    public Event(String mMonth_short, String mDay_short, String mWeekDay_short, String mEvent_name,
                 String mHijriDate, String mGrigDate, String mDescription, boolean mIsToday){
        month_short = mMonth_short;
        day_short = mDay_short;
        weekDay_short = mWeekDay_short;
        event_name = mEvent_name;
        hijriDate = mHijriDate;
        grigDate = mGrigDate;
        description = mDescription;
        isToday = mIsToday;
    }

    public String getMonth_short() {
        return month_short;
    }

    public String getDay_short() {
        return day_short;
    }

    public String getWeekDay_short() {
        return weekDay_short;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getHijriDate() {
        return hijriDate;
    }

    public String getGrigDate() {
        return grigDate;
    }

    public String getDescription() {
        return description;
    }

    public boolean isToday() {
        return isToday;
    }
}
