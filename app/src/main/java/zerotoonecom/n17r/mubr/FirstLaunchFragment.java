package zerotoonecom.n17r.mubr;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

/**
 * Created by user on 02.08.2017.
 */

public class FirstLaunchFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_first);
        Preference start = getPreferenceManager().findPreference("startlink");
        if (start != null) {
            start.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    if (isOnline()) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


}
