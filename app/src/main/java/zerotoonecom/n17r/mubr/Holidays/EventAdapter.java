package zerotoonecom.n17r.mubr.Holidays;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import zerotoonecom.n17r.mubr.R;

/**
 * Created by Nurdaulet Yeltayev (yeltayev22) on 09.08.2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> events;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView month_short, day_short, weekDay_short, event_name, muslim_month, description;
        LinearLayout description_layout, main_layout;
        RelativeLayout today_layout;
        CardView card_view;

        TextView current_grig_date, current_hijri_date;
        TextView hijri_date, grig_date;

        public MyViewHolder(View view) {
            super(view);

            card_view = (CardView) view.findViewById(R.id.card_view);
            today_layout = (RelativeLayout) view.findViewById(R.id.today_layout);

            month_short = (TextView) view.findViewById(R.id.month_short);
            day_short = (TextView) view.findViewById(R.id.day_short);
            weekDay_short = (TextView) view.findViewById(R.id.week_short);
            event_name = (TextView) view.findViewById(R.id.event_name);
            muslim_month = (TextView) view.findViewById(R.id.muslim_month);
            description = (TextView) view.findViewById(R.id.description);
            hijri_date = (TextView) view.findViewById(R.id.hijri_date);
            grig_date = (TextView) view.findViewById(R.id.grig_date);

            description_layout = (LinearLayout) view.findViewById(R.id.description_layout);
            main_layout = (LinearLayout) view.findViewById(R.id.main_layout);

            current_grig_date = (TextView) view.findViewById(R.id.current_grid_date);
            current_hijri_date = (TextView) view.findViewById(R.id.current_hijri_date);

            main_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (description_layout.getVisibility() == View.GONE) {
                        description_layout.setVisibility(View.VISIBLE);
                    } else {
                        description_layout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public EventAdapter(List<Event> events, Context mContext) {
        this.events = events;
        context = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Event event = events.get(position);
        if (event.getEvent_name().compareTo(context.getString(R.string.today)) == 0) {
            holder.card_view.setVisibility(View.GONE);
            holder.today_layout.setVisibility(View.VISIBLE);
            holder.current_grig_date.setText(event.getGrigDate());
            holder.current_hijri_date.setText(event.getHijriDate());

        } else if (event.getEvent_name().compareTo(context.getString(R.string.today)) != 0 && event.isToday()) {
            holder.main_layout.setBackgroundResource(R.drawable.current_time_rectangle);
            holder.card_view.setVisibility(View.VISIBLE);
            holder.today_layout.setVisibility(View.GONE);
            holder.month_short.setText(event.getMonth_short());
            holder.day_short.setText(event.getDay_short());
            holder.weekDay_short.setText(event.getWeekDay_short());
            holder.event_name.setText(event.getEvent_name());
            holder.muslim_month.setText(event.getHijriDate());
            holder.description.setText(event.getDescription());
            holder.grig_date.setText(event.getGrigDate());
            holder.hijri_date.setText(event.getHijriDate());
            holder.description_layout.setVisibility(View.GONE);
        } else {
            holder.card_view.setVisibility(View.VISIBLE);
            holder.today_layout.setVisibility(View.GONE);
            holder.month_short.setText(event.getMonth_short());
            holder.day_short.setText(event.getDay_short());
            holder.weekDay_short.setText(event.getWeekDay_short());
            holder.event_name.setText(event.getEvent_name());
            holder.muslim_month.setText(event.getHijriDate());
            holder.description.setText(event.getDescription());
            holder.grig_date.setText(event.getGrigDate());
            holder.hijri_date.setText(event.getHijriDate());
            holder.description_layout.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return events.size();
    }


}