package zerotoonecom.n17r.mubr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FirstLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new FirstLaunchFragment()).commit();

    }
}
