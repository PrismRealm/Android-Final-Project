package com.example.ssuns.finaltopic;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.Calendar;

public class TenDaysFragment  extends Fragment {

    private PullRefreshLayout parentLayout;
    private ListView lstWeatherItems;
    private WeatherItemAdapter weatherItemAdapter;
    private OneWeekJson jsonData;

    public TenDaysFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ten_days, container, false);

        parentLayout = view.findViewById(R.id.ten_days_parent_layout);
        parentLayout.setOnRefreshListener(pullRefreshListener);

        weatherItemAdapter = new WeatherItemAdapter(view.getContext(), R.layout.weather_list_item);
        lstWeatherItems = view.findViewById(R.id.list_ten_days);
        lstWeatherItems.setAdapter(weatherItemAdapter);
        lstWeatherItems.setOnItemClickListener(onListItemClicked);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // The condition can be removed after updating data on starting application
        if (jsonData != null) {
            weatherItemAdapter.setJsonData(jsonData);
            weatherItemAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 以JSON資料來更新View上的顯示。
     */
    public void updateViewDataByJson(OneWeekJson jsonData) {
        this.jsonData = jsonData;
        // Views may not be loaded
        if (weatherItemAdapter != null) {
            weatherItemAdapter.setJsonData(jsonData);
            weatherItemAdapter.notifyDataSetChanged();
            parentLayout.setRefreshing(false);
        }
    }

    /**
     * 停止更新的Icon顯示。
     */
    public void stopRefreshing() {
        if (parentLayout != null) {
            parentLayout.setRefreshing(false);
        }
    }

    /**
     * 當使用者下拉版面後的更新處理。
     */
    private PullRefreshLayout.OnRefreshListener pullRefreshListener = new PullRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            ((MainActivity) getActivity()).updateDateAllDatas();
        }
    };

    private ListView.OnItemClickListener onListItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.alert_dialog_weather_message, null);
            DialogData data = weatherItemAdapter.getDialogData(position);
            ((TextView) dialogView.findViewById(R.id.txvAvgTemperature_Dialog)).setText(data.getAvgTemp());
            ((TextView) dialogView.findViewById(R.id.txvHumidity_Dialog)).setText(data.getHumidity());
            ((TextView) dialogView.findViewById(R.id.txvUV_Dialog)).setText(data.getUV());
            ((TextView) dialogView.findViewById(R.id.txvRainRate_Dialog)).setText(data.getRainRate());
            ((TextView) dialogView.findViewById(R.id.txvDescription_Dialog)).setText(data.getDescription());
            builder.setView(dialogView);
            builder.setTitle("概略天氣資料");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    };

    class WeatherItemAdapter<T> extends ArrayAdapter<T> {

        private OneWeekJson jsonData;
        private int count;

        WeatherItemAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            count = 0;
        }

        void setJsonData(OneWeekJson newJsonData) {
            this.jsonData = newJsonData;
            this.count = newJsonData.getWeatherElement().getPoP12h().size();
        }

        /**
         * 取得指定天氣欄位下的資料。
         * @param position 指定第position項的資料。
         * @return Dialog所需要的資料。
         */
        public DialogData getDialogData(int position) {
            OneWeekJson.WeatherElementBean.TBean tBean = null;
            OneWeekJson.WeatherElementBean.RHBean rhBean = null;
            OneWeekJson.WeatherElementBean.UVIBean uviBean = null;
            OneWeekJson.WeatherElementBean.PoP12hBean poP12hBean = null;
            OneWeekJson.WeatherElementBean.WeatherDescriptionBean descBean = null;

            if (position < jsonData.getWeatherElement().getT().size())
                tBean = jsonData.getWeatherElement().getT().get(position);
            if (position < jsonData.getWeatherElement().getRH().size())
                rhBean = jsonData.getWeatherElement().getRH().get(position);
            if (position < jsonData.getWeatherElement().getUVI().size())
                uviBean = jsonData.getWeatherElement().getUVI().get(position);
            if (position < jsonData.getWeatherElement().getPoP12h().size())
                poP12hBean = jsonData.getWeatherElement().getPoP12h().get(position);
            if (position < jsonData.getWeatherElement().getWeatherDescription().size())
                descBean = jsonData.getWeatherElement().getWeatherDescription().get(position);

            String tString = tBean == null ? "未提供資料" : String.valueOf(tBean.getValue());
            String rhString = rhBean == null ? "未提供資料" : String.valueOf(rhBean.getValue());
            String uviString = uviBean == null ? "未提供資料" : uviBean.getValue();
            String poP12hString = poP12hBean == null ? "未提供資料" : String.valueOf(poP12hBean.getValue());
            String descString = descBean == null ? "未提供資料" : descBean.getValue();

            return new DialogData(tString, rhString, uviString, poP12hString, descString);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            OneWeekJson.WeatherElementBean.PoP12hBean poP12hBean = null;
            OneWeekJson.WeatherElementBean.MaxTBean maxTBean = null;
            OneWeekJson.WeatherElementBean.MinTBean minTBean = null;
            OneWeekJson.WeatherElementBean.WxBean wxBean = null;
            OneWeekJson.WeatherElementBean.WeatherDescriptionBean weatherDescriptionBean = null;

            if (position < jsonData.getWeatherElement().getPoP12h().size())
                poP12hBean = jsonData.getWeatherElement().getPoP12h().get(position);
            if (position < jsonData.getWeatherElement().getMaxT().size())
                maxTBean = jsonData.getWeatherElement().getMaxT().get(position);
            if (position < jsonData.getWeatherElement().getMinT().size())
                minTBean = jsonData.getWeatherElement().getMinT().get(position);
            if (position < jsonData.getWeatherElement().getWx().size())
                wxBean = jsonData.getWeatherElement().getWx().get(position);
            if (position < jsonData.getWeatherElement().getWeatherDescription().size())
                weatherDescriptionBean = jsonData.getWeatherElement().getWeatherDescription().get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.weather_list_item, parent, false);
            }

            if (position == 0){
                ((TextView) convertView.findViewById(R.id.txvDate)).setText("今天");
            }
            else {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, position);
                @SuppressLint("DefaultLocale")
                String dateText = String.format("%1$tm月%1$td日 %1$ta", cal);
                ((TextView) convertView.findViewById(R.id.txvDate)).setText(dateText);
            }

            String rainRateText = String.valueOf(poP12hBean == null ? "20" : poP12hBean.getValue()) + "%";
            String highTempText = String.valueOf(maxTBean == null ? "ND" : maxTBean.getValue()) + "°";
            String lowTempText = String.valueOf(minTBean == null ? "ND" : minTBean.getValue()) + "°";
            String description = wxBean == null ? "無敘述資料提供" : wxBean.getValue();
            ((TextView) convertView.findViewById(R.id.txvDescription)).setText(description);
            ((TextView) convertView.findViewById(R.id.txvRainRate)).setText(rainRateText);
            ((TextView) convertView.findViewById(R.id.txvHighestTemperature)).setText(highTempText);
            ((TextView) convertView.findViewById(R.id.txvLowestTemperature)).setText(lowTempText);
            ((ImageView) convertView.findViewById(R.id.imgWeatherIcon)).setImageResource(
                    MainActivity.wxDescriptionToImageResource(description, false));

            return convertView;
        }

        @Override
        public int getCount() {
            return count;
        }
    }

    /**
     * 存放在Dialog上需要的資料。
     */
    static class DialogData {
        private String avgTemp;
        private String humidity;
        private String UV;
        private String rainRate;
        private String description;

        DialogData(String avgTemp, String humidity, String UV, String rainRate, String description) {
            this.avgTemp = avgTemp;
            this.humidity = humidity;
            this.UV = UV;
            this.rainRate = rainRate;
            this.description = description;
        }

        public String getAvgTemp() { return avgTemp; }

        public String getHumidity() { return humidity + "%"; }

        public String getRainRate() {  return rainRate + "%"; }

        public String getUV() {
            return UV;
        }

        public String getDescription() { return description; }
    }

}
