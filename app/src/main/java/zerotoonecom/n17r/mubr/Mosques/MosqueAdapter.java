package zerotoonecom.n17r.mubr.Mosques;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import zerotoonecom.n17r.mubr.R;

/**
 * Created by Nurdaulet Yeltayev (yeltayev22) on 09.08.2017.
 */

public class MosqueAdapter extends RecyclerView.Adapter<MosqueAdapter.CustomViewHolder> {

    private static final String TAG = MosqueAdapter.class.getSimpleName();
    private double latitude;
    private double longitude;
    List<Mosque> mosques;
    Context context;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView mosque_name, mosque_address, mosque_phone, mosque_rating, mosque_distance;

        public CustomViewHolder(View view) {
            super(view);
            mosque_name = (TextView) view.findViewById(R.id.mosque_name);
            mosque_address = (TextView) view.findViewById(R.id.mosque_address);
            mosque_phone = (TextView) view.findViewById(R.id.mosque_phone);
            mosque_rating = (TextView) view.findViewById(R.id.mosque_rating);
            mosque_distance = (TextView) view.findViewById(R.id.mosque_distance);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    openPreferredLocationInMap(mosques.get(position).getLatitude(),
                            mosques.get(position).getLongitude(), mosques.get(position).getName());
                }
            });
        }
    }

    public MosqueAdapter(List<Mosque> mosques, Context context, double latitude, double longitude) {
        this.mosques = mosques;
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mosques, null);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final Mosque mosque = mosques.get(position);
        holder.mosque_name.setText(String.valueOf(mosque.getName()));
        holder.mosque_address.setText(String.valueOf(mosque.getAddress()));
        holder.mosque_rating.setText(String.valueOf(mosque.getRating()));
        holder.mosque_phone.setText(String.valueOf(mosque.getPhone()));
        float distance = getDistance(mosque);
        holder.mosque_distance.setText(String.valueOf(distance) + " " + context.getString(R.string.km));
    }

    @Override
    public int getItemCount() {
        return mosques.size();
    }

    private void openPreferredLocationInMap(String lat, String lng, String name) {
        Uri geoLocation = Uri.parse("geo:" + lat + "," + lng + "?q=" + name);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, context.getString(R.string.no_apps), Toast.LENGTH_SHORT).show();
        }
    }

    public float getDistance(Mosque mosque) {
        Location loc1 = new Location("");
        loc1.setLatitude(latitude);
        loc1.setLongitude(longitude);
        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble(mosque.getLatitude()));
        loc2.setLongitude(Double.parseDouble(mosque.getLongitude()));
        float distanceInMeters = loc1.distanceTo(loc2);

        float distance = distanceInMeters/1000;
        distance = (float) Math.round(distance * 100) / 100;
        return distance;
    }

}