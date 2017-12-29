package zerotoonecom.n17r.mubr.data;

import android.provider.BaseColumns;

/**
 * Created by user on 19.07.2017.
 */

public class NamazTimeContract {

    public NamazTimeContract() {}

    public static final class NamazTimeEntry implements BaseColumns {

        public static final String TABLE_NAME = "timings";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_FAJR = "fajr";
        public static final String COLUMN_SUNRISE = "sunrise";
        public static final String COLUMN_DHUHR = "dhuhr";
        public static final String COLUMN_ASR = "asr";
        public static final String COLUMN_MAGHRIB = "maghrib";
        public static final String COLUMN_ISHA = "isha";

    }

}
