package zerotoonecom.n17r.mubr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import zerotoonecom.n17r.mubr.Counter.CounterActivity;
import zerotoonecom.n17r.mubr.Holidays.CalendarActivity;
import zerotoonecom.n17r.mubr.Mosques.MosqueActivity;
import zerotoonecom.n17r.mubr.Names.NamesActivity;
import zerotoonecom.n17r.mubr.Schedule.ScheduleActivity;
import zerotoonecom.n17r.mubr.Time.NamazTimeContract;
import zerotoonecom.n17r.mubr.Time.NamazTimeDbHelper;

public class MenuFragment extends Fragment {

    ImageView counter_image_view;
    ImageView schedule_image_view;
    ImageView events_image_view;
    ImageView mosque_image_view;
    ImageView names_image_view;

    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        names_image_view = (ImageView) rootView.findViewById(R.id.names_image_view);
        counter_image_view = (ImageView) rootView.findViewById(R.id.counter_image_view);
        schedule_image_view = (ImageView) rootView.findViewById(R.id.schedule_image_view);
        events_image_view = (ImageView) rootView.findViewById(R.id.events_image_view);
        mosque_image_view = (ImageView) rootView.findViewById(R.id.mosque_image_view);

        counter_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CounterActivity.class);
                startActivity(intent);
            }
        });

        schedule_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                if (rowExists()) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.check_download), Toast.LENGTH_LONG).show();
                }
            }
        });

        events_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        mosque_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MosqueActivity.class);
                startActivity(intent);
            }
        });

        names_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NamesActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private boolean rowExists() {
        NamazTimeDbHelper dbHelper = new NamazTimeDbHelper(getContext());
        db = dbHelper.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + NamazTimeContract.NamazTimeEntry.TABLE_NAME, null);
        if (mCursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            mCursor.close();
            db.close();
            dbHelper.close();
            return true;
        } else
        {
            // I AM EMPTY
            mCursor.close();
            db.close();
            dbHelper.close();
            return false;
        }
    }


}
