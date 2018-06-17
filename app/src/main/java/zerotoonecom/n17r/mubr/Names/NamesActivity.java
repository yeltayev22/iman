package zerotoonecom.n17r.mubr.Names;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import zerotoonecom.n17r.mubr.R;

public class NamesActivity extends AppCompatActivity {

    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);
        setTitle(getString(R.string.names));

        ListView list = (ListView) findViewById(R.id.name_list);
        adapter = new MainAdapter(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), NameActivity.class);
                intent.putExtra(Consts.POSITION, position);
                startActivity(intent);
            }
        });

        ViewCompat.setNestedScrollingEnabled(list, true);
    }


    private class MainAdapter extends BaseAdapter {
        private final int count = 99;
        private Context context;
        private String[] names;
        private String[] kazakhNames;
        private String[] arabicNames;
        private Typeface scheherazade;

        MainAdapter(Context c) {
            context = c;

            names = c.getResources().getStringArray(R.array.name);
            kazakhNames = c.getResources().getStringArray(R.array.kazakh_names);
            arabicNames = c.getResources().getStringArray(R.array.arabic_names);

            scheherazade = Typeface.createFromAsset(c.getAssets(), "Scheherazade.ttf");
        }

        @Override
        public View getView(int position, View root, ViewGroup parent) {
            if (root == null) {
                root = LayoutInflater.from(context).inflate(R.layout.item_name, parent, false);
            }

            TextView number = (TextView) root.findViewById(R.id.number);
            TextView name = (TextView) root.findViewById(R.id.name);
            TextView kazakhName = (TextView) root.findViewById(R.id.kazakh_name);
            TextView arabicName = (TextView) root.findViewById(R.id.arabic_name);

            number.setText(String.valueOf(position + 1));
            name.setText(names[position]);
            arabicName.setText(arabicNames[position]);
            //arabicName.setTypeface(scheherazade);
            kazakhName.setText(kazakhNames[position]);

            return root;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public String getItem(int pos) {
            return null;
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }
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
