package zerotoonecom.n17r.mubr.Schedule;

/**
 * Created by Nurdaulet Yeltayev (yeltayev22) on 30.07.2017.
 */

public class Day {

    private String date;
    private String fajr;
    private String sunrise;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;

    public Day(String mDate, String mFajr, String mSunrise, String mDhuhr, String mAsr, String mMaghrib, String mIsha){
        date = mDate;
        fajr = mFajr;
        sunrise = mSunrise;
        dhuhr = mDhuhr;
        asr = mAsr;
        maghrib = mMaghrib;
        isha = mIsha;
    }


    public String getDate() {
        return date;
    }

    public String getFajr() {
        return fajr;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getDhuhr() {
        return dhuhr;
    }

    public String getAsr() {
        return asr;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public String getIsha() {
        return isha;
    }
}
