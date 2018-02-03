package zerotoonecom.n17r.mubr.FirstLaunch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import zerotoonecom.n17r.mubr.R;

public class FirstLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new FirstLaunchFragment()).commit();

    }
}
