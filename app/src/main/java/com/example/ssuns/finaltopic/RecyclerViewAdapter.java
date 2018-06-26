package com.example.ssuns.finaltopic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "RecyclerViewAdapter";

    private ArrayList<WeatherTagData> dataList = new ArrayList<>();
    private ArrayList<ViewHolder> viewHolderList = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context) {
        super();
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolderList.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        WeatherTagData data = dataList.get(position);
        holder.imgView.setImageResource(data.getImageSource());
        holder.txvTemperature.setText(data.getTemperature());
        holder.txvTimes.setText(data.getTime());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDatas(ArrayList<WeatherTagData> dataList) {
        this.dataList = dataList;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parentLayout;
        TextView txvTemperature, txvTimes;
        ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            txvTemperature = itemView.findViewById(R.id.txvTemperature);
            txvTimes = itemView.findViewById(R.id.txvTimes);
            imgView = itemView.findViewById(R.id.imgImage);
            parentLayout = itemView.findViewById(R.id.weather_element);
        }

        public void setData(String temperature, String time, int imageSource) {
            parentLayout.setVisibility(View.VISIBLE);
            txvTemperature.setText(temperature);
            txvTimes.setText(time);
            imgView.setImageResource(imageSource);
        }

        public void setVisible() {
            parentLayout.setVisibility(View.VISIBLE);
        }

        public void setGone() {
            parentLayout.setVisibility(View.GONE);
        }
    }

    public static class WeatherTagData {

        int temperature, startTime, imageSource;
        boolean enable;

        public WeatherTagData() {
            this(24, 0, R.drawable.sunny_day, true);
        }

        public WeatherTagData(int temperature, int startTime, int imageSource, boolean enable) {
            this.temperature = temperature;
            this.startTime = startTime;
            this.imageSource = imageSource;
            this.enable = enable;
        }

        void setData(int temperature, int startTime, int imageSource, boolean enable) {
            this.temperature = temperature;
            this.startTime = startTime;
            this.imageSource = imageSource;
            this.enable = enable;
        }

        public String getTemperature() {
            return String.valueOf(temperature) + "°";
        }

        public int getImageSource() {
            return imageSource;
        }

        public String getTime() {
            return timeFormat(startTime);
        }

        private String timeFormat(int time) {
            String result = time >= 12 ? "PM" : "AM";
            if (time == 0 || time == 12)
                result += "12";
            else
                result += String.valueOf(time > 12 ? time - 12 : time);
            return result + ":00";
        }
    }
}
