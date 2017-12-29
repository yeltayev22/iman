package zerotoonecom.n17r.mubr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import at.markushi.ui.CircleButton;

/**
 * Created by user on 14.07.2017.
 */

public class CounterActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory {

    private int counter = 0;
    private int currentCounter;

    CircleButton addButton;
    CircleButton resetButton;
    CircleButton minusButton;
    TextSwitcher counterTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter);
        setTitle(getString(R.string.counter));

        counterTv = (TextSwitcher) findViewById(R.id.counter_tv);
        counterTv.setFactory(this);
        //counterTv.setOutAnimation(this, R.anim.alpha);

        addButton = (CircleButton) findViewById(R.id.add_button);
        resetButton = (CircleButton) findViewById(R.id.reset_button);
        minusButton = (CircleButton) findViewById(R.id.minus_button);

        SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
        int currentCounter = sharedPref.getInt("counter", 0);
        counter = currentCounter;
        counterTv.setText(Integer.toString(counter));

        counterTv.setInAnimation(this, R.anim.zoom_in);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
                counter++;
                counterTv.setText(Integer.toString(counter));
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                counterTv.setText(Integer.toString(counter));
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter == 0) {
                    counterTv.setText("0");
                } else {
                    counter--;
                    counterTv.setText(Integer.toString(counter));
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("counter", counter);
        editor.commit();
    }

    @Override
    public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(130);
        t.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        t.setMaxLines(1);
        return t;
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
