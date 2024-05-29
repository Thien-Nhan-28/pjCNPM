package com.example.weatherapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Hour> hourlyList;

    public HourlyAdapter(Context context, ArrayList<Hour> hourlyList) {
        this.context = context;
        this.hourlyList = hourlyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_container_forecast_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hour hour = hourlyList.get(position);
        holder.txtTime.setText(hour.getTime());
        holder.txtTemp.setText(String.format("%s\u00B0C", hour.getTemp_c()));
        holder.txtFeelsLike.setText(String.format("%s\u00B0C", hour.getFeelslike_c()));
        Picasso.get().load(hour.getIcon()).into(holder.imgIcon);
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView txtTime, txtTemp, txtFeelsLike;
        public AppCompatImageView imgIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.textTime);
            txtTemp = itemView.findViewById(R.id.textTemperature);
            txtFeelsLike = itemView.findViewById(R.id.textFeelsLikeTemperature);
            imgIcon = itemView.findViewById(R.id.imageIcon);
        }
    }
}