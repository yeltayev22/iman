package zerotoonecom.n17r.mubr;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by user on 02.08.2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        ListPreference city = (ListPreference) getPreferenceManager().findPreference(getString(R.string.choose_city_key));
        ListPreference lang = (ListPreference) getPreferenceManager().findPreference(getString(R.string.language_key));
        Preference share = (Preference) getPreferenceManager().findPreference(getString(R.string.share_key));
        Preference rate = (Preference) getPreferenceManager().findPreference(getString(R.string.rate_key));

        ListPreference azan = (ListPreference) getPreferenceManager().findPreference(getString(R.string.azan_key));
        final RingtonePreference ringtone = (RingtonePreference) getPreferenceManager().findPreference(getString(R.string.ringtone_key));

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String strRingtone = settings.getString(getString(R.string.ringtone_key), "ringtone");
        Uri ringtoneUri = Uri.parse(strRingtone);
        Ringtone ring = RingtoneManager.getRingtone(getActivity(), ringtoneUri);
        String name = ring.getTitle(getActivity());
        ringtone.setSummary(name);

        ringtone.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (settings.getString(getString(R.string.azan_key), "0").compareTo(getString(R.string.no_value)) != 0) {
                    Toast.makeText(getActivity(), R.string.unselect_azan, Toast.LENGTH_SHORT).show();
                    ringtone.setSummary(getString(R.string.no_label));
                    return false;
                } else {
                    Uri ringtoneUri = Uri.parse((String) newValue);
                    Ringtone ring = RingtoneManager.getRingtone(getActivity(), ringtoneUri);
                    String name = ring.getTitle(getActivity());
                    ringtone.setSummary(name);
                    return true;
                }
            }
        });

        city.setNegativeButtonText(getString(R.string.cancel));
        lang.setNegativeButtonText(getString(R.string.cancel));

        city.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!isOnline()) {
                    Toast.makeText(getActivity(), R.string.check_internet, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        lang.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                getActivity().recreate();
                return true;
            }
        });

        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String URL_TO_SHARE = Uri.parse("bit.ly/get-iman").toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " " + URL_TO_SHARE);
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
                return true;
            }
        });

        rate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("bit.ly/get-iman")));
                }
                return true;
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


}
