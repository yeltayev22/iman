package zerotoonecom.n17r.mubr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import zerotoonecom.n17r.mubr.data.GPSTracker;
import zerotoonecom.n17r.mubr.data.JSONParser;
import zerotoonecom.n17r.mubr.data.Mosque;
import zerotoonecom.n17r.mubr.data.MosqueAdapter;

public class MosqueActivity extends AppCompatActivity {

    public static final String MOSQUE_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    public static final String PLACE_INFO_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/details/json";
    public static final String MY_API_KEY = "AIzaSyD1atw89B_1VZkjCdX_57DCK5jSe0QbXbg";
    public static final String TAG_RESULT = "results";

    GPSTracker gps;
    double latitude, longitude;
    private String chosenLang;

    List<Mosque> mosques = new ArrayList<>();
    MosqueAdapter mAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout refresh;
    private int i;
    private LinearLayout emptyView;
    Button button;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        chosenLang = settings.getString(getString(R.string.language_key), "ru");
        Locale locale = new Locale(chosenLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosque);
        setTitle(getString(R.string.mosque));

        refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_mosque);
        emptyView = (LinearLayout) findViewById(R.id.empty_view);
        button = (Button) findViewById(R.id.try_again_button);
        gps = new GPSTracker(MosqueActivity.this);

        if (!gps.canGetLocation() || !isOnline()) {
            refresh.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            refresh.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final MosqueAsyncTask task = new MosqueAsyncTask();
        task.execute();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isOnline() || !gps.canGetLocation()) {
                    refresh.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    refresh.setRefreshing(false);
                }
                recyclerView.removeAllViews();
                mosques.clear();
                new MosqueAsyncTask().execute();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }


    private class MosqueAsyncTask extends AsyncTask<URL, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gps = new GPSTracker(MosqueActivity.this);
            // check if GPS enabled
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if (latitude == 0 && longitude == 0) {
                    refresh.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    cancel(true);
                }
               // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } /*else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }*/
        }

        @Override
        protected String doInBackground(URL... urls) {
            Uri baseUri = Uri.parse(MOSQUE_SEARCH_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("location", String.valueOf(latitude) + "," + String.valueOf(longitude))
                    .appendQueryParameter("radius", "20000")
                    //.appendQueryParameter("rankby", "distance")
                    .appendQueryParameter("type", "mosque")
                    .appendQueryParameter("language", chosenLang)
                    .appendQueryParameter("key", MY_API_KEY);
            URL url = null;
            try {
                url = new URL(uriBuilder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONParser jParser = new JSONParser();
            String json = null;

            try {
                json = jParser.getJSONFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            try {
                getPlaceId(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            refresh.setRefreshing(false);
        }

    }

    private void getPlaceId(String json) throws JSONException {
        JSONObject baseJsonResponse = new JSONObject(json);
        JSONArray result = baseJsonResponse.getJSONArray(TAG_RESULT);
        for (i = 0; i < result.length(); i++) {
            JSONObject mosque = result.getJSONObject(i);
            String place_id = mosque.getString("place_id");
            PlaceInfoAsyncTask info_task = new PlaceInfoAsyncTask();
            info_task.execute(place_id);
        }
    }

    private class PlaceInfoAsyncTask extends AsyncTask<String, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... place_ids) {
            String place_id = place_ids[0];
            Uri place_baseUri = Uri.parse(PLACE_INFO_SEARCH_URL);
            Uri.Builder place_uriBuilder = place_baseUri.buildUpon();
            place_uriBuilder.appendQueryParameter("placeid", place_id)
                    .appendQueryParameter("language", chosenLang)
                    .appendQueryParameter("key", MY_API_KEY);
            URL place_url = null;
            try {
                place_url = new URL(place_uriBuilder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONParser jParser = new JSONParser();
            String place_json = null;
            try {
                place_json = jParser.getJSONFromUrl(place_url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                extractFeatureFromJson(place_json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place_json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            mAdapter = new MosqueAdapter(mosques, getApplicationContext(), latitude, longitude);
            recyclerView.setAdapter(mAdapter);
        }

    }

    private void extractFeatureFromJson(String json) throws JSONException {
        JSONObject place_baseJsonResponse = new JSONObject(json);
        JSONObject place_result = place_baseJsonResponse.getJSONObject("result");
        String name = place_result.getString("name");
        String address = place_result.getString("formatted_address");
        String phone;
        if (place_result.has("international_phone_number")) {
            phone = place_result.getString("international_phone_number");
        } else {
            phone = getString(R.string.phone_not_available);
        }
        String rating = place_result.getString("rating");
        JSONObject geometry = place_result.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");
        String latitude = location.getString("lat");
        String longitude = location.getString("lng");
        //Toast.makeText(MosqueActivity.this, name + ' ' + rating + ' ' + address + ' ' + photo + ' ' + distance, Toast.LENGTH_SHORT).show();
        mosques.add(new Mosque(name, address, phone, rating, latitude, longitude));
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
