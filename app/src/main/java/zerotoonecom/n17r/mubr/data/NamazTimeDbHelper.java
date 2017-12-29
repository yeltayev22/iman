package zerotoonecom.n17r.mubr.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by user on 19.07.2017.
 */

public class NamazTimeDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "namaztimings.db";
    public static final int DATABASE_VERSION = 2;
    public static final String LOG_TAG = NamazTimeDbHelper.class.getName();


    public NamazTimeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Table created!!!");
        String SQL_CREATE_TIMINGS_TABLE = "CREATE TABLE " + NamazTimeContract.NamazTimeEntry.TABLE_NAME + " ("
                + NamazTimeContract.NamazTimeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NamazTimeContract.NamazTimeEntry.COLUMN_DATE + " TEXT NOT NULL, "
                + NamazTimeContract.NamazTimeEntry.COLUMN_FAJR + " TEXT NOT NULL, "
                + NamazTimeContract.NamazTimeEntry.COLUMN_SUNRISE + " TEXT NOT NULL, "
                + NamazTimeContract.NamazTimeEntry.COLUMN_DHUHR +  " TEXT NOT NULL, "
                + NamazTimeContract.NamazTimeEntry.COLUMN_ASR +  " TEXT NOT NULL, "
                + NamazTimeContract.NamazTimeEntry.COLUMN_MAGHRIB +  " TEXT NOT NULL, "
                + NamazTimeContract.NamazTimeEntry.COLUMN_ISHA + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_TIMINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NamazTimeContract.NamazTimeEntry.TABLE_NAME);
        onCreate(db);
    }
}
