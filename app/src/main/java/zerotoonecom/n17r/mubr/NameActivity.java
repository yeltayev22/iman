package zerotoonecom.n17r.mubr;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import zerotoonecom.n17r.mubr.R;

public class NameActivity extends AppCompatActivity {

    TextView name;
    TextView kazakh_name;
    TextView arabic_name;
    TextView meaning;

    String[] names;
    String[] kazakhNames;
    String[] arabicNames;
    String[] meanings;

    Typeface scheherazade;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        name = (TextView) findViewById(R.id.name);
        kazakh_name = (TextView) findViewById(R.id.kazakh_name);
        arabic_name = (TextView) findViewById(R.id.arabic_name);
        meaning = (TextView) findViewById(R.id.meaning);

        names = getResources().getStringArray(R.array.name);
        kazakhNames = getResources().getStringArray(R.array.kazakh_names);
        arabicNames = getResources().getStringArray(R.array.arabic_names);
        meanings = getResources().getStringArray(R.array.meanings);

        scheherazade = Typeface.createFromAsset(getAssets(), "Scheherazade.ttf");


        Intent intent = getIntent();
        position = intent.getIntExtra(Consts.POSITION, 0);
        name.setText(names[position]);
        kazakh_name.setText(kazakhNames[position]);
        arabic_name.setText(arabicNames[position]);
        //  arabic_name.setTypeface(scheherazade);
        meaning.setText(meanings[position]);


    }
}
