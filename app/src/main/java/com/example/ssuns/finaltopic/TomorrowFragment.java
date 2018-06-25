package com.example.ssuns.finaltopic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TomorrowFragment extends Fragment {
    private RecyclerViewAdapter recyclerViewAdapter;

    private TwoDaysJson jsonData;

    private PullRefreshLayout parentLayout;
    private TextView txvDate, txvOverview;
    private TextView txvHumidity, txvWindSpeed, txvDewPoint;
    private TextView txvDescription;
    private ImageView imgWeather;
    private RecyclerView recyclerViewTemperature;
    private GridLayoutManager gridLayoutManager;

    public TomorrowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tomorrow, container, false);
        Context context = view.getContext();

        parentLayout = view.findViewById(R.id.parent_layout_tomorrow);
        parentLayout.setOnRefreshListener(pullRefreshListener);

        txvDate = view.findViewById(R.id.txt_date_tomorrow);
        txvOverview = view.findViewById(R.id.txt_weather_overview_tomorrow);
        Calendar now = Calendar.getInstance();
        @SuppressLint("DefaultLocale")
        String dateString = String.format("%1$tm月%1$td日, %1$tA", now);
        txvDate.setText(dateString);

        txvHumidity = view.findViewById(R.id.txt_humidity_tomorrow);
        txvWindSpeed = view.findViewById(R.id.txt_wind_speed_tomorrow);
        txvDewPoint = view.findViewById(R.id.txt_dew_point_tomorrow);

        txvDescription = view.findViewById(R.id.txvDescription_Tomorrow);

        imgWeather = view.findViewById(R.id.imageView_tomorrow);

        recyclerViewAdapter = new RecyclerViewAdapter(context);
        recyclerViewTemperature = view.findViewById(R.id.recycler_view_temperature_tomorrow);
        recyclerViewTemperature.setAdapter(recyclerViewAdapter);
        gridLayoutManager = new GridLayoutManager(context, 4);
        recyclerViewTemperature.setLayoutManager(gridLayoutManager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViewByJsonData();
    }

    /**
     * 停止更新時Icon的圖案。
     */
    public void stopRefreshing() {
        if (parentLayout != null) {
            parentLayout.setRefreshing(false);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // When user pull to refresh
    private PullRefreshLayout.OnRefreshListener pullRefreshListener = new PullRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            ((MainActivity) TomorrowFragment.this.getActivity()).updateDateAllDatas();
        }
    };

    public void updateJsonData(TwoDaysJson jsonData) {
        this.jsonData = jsonData;
        updateViewByJsonData();
    }

    /**
     * 以當前儲存的JSON資料來更新畫面顯示。
     */
    public void updateViewByJsonData() {
        if (jsonData == null) return;
        TwoDaysJson.WeatherElementBean weatherElement = jsonData.getWeatherElement();
        TwoDaysJson.WeatherElementBean.WxBean Wx = null;
        TwoDaysJson.WeatherElementBean.WeatherDescriptionBean weatherDescription = null;
        TwoDaysJson.WeatherElementBean.RHBean RH = null;
        TwoDaysJson.WeatherElementBean.WSBean WS = null;
        TwoDaysJson.WeatherElementBean.TdBean Td = null;
        List<TwoDaysJson.WeatherElementBean.TBean> TList = weatherElement.getT();

        if (weatherElement.getWx().size() >= 2)
            Wx = weatherElement.getWx().get(1);
        if (weatherElement.getWeatherDescription().size() >= 2)
            weatherDescription = weatherElement.getWeatherDescription().get(1);
        if (weatherElement.getRH().size() >= 2)
            RH = weatherElement.getRH().get(1);
        if (weatherElement.getWS().size() >= 2)
            WS = weatherElement.getWS().get(1);
        if (weatherElement.getTd().size() >= 2)
            Td = weatherElement.getTd().get(1);

        String overviewString = Wx == null ? "無敘述資料" : Wx.getValue();
        String descriptionString = weatherDescription == null ? "無敘述資料提供" : weatherDescription.getValue();
        String humidityString = RH == null ? "未提供資料" : String.valueOf(RH.getValue()) + "%";
        String windSpeedString = WS == null ? "未提供資料" : String.valueOf(WS.getValue());
        String dewPointString = Td == null ? "未提供資料" : String.valueOf(Td.getValue()) + "°";

        txvOverview.setText(overviewString);
        txvDescription.setText(descriptionString);
        txvHumidity.setText(humidityString);
        txvWindSpeed.setText(windSpeedString);
        txvDewPoint.setText(dewPointString);
        imgWeather.setImageResource(MainActivity.wxDescriptionToImageResource(overviewString ,false));

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.HOUR, 24);
        @SuppressLint("DefaultLocale") String tomorrowDateString = String.format("%1$tY-%1$tm-%1$td", tomorrow);
        ArrayList<RecyclerViewAdapter.WeatherTagData> weatherTagDataList = new ArrayList<>();
        int size = TList.size();

        // 循著溫度資料清單來判斷
        for (int i = 0; weatherTagDataList.size() <= 4 && i < size; i++) {
            TwoDaysJson.WeatherElementBean.TBean T = TList.get(i);
            // 比對日期是否為明天
            if (tomorrowDateString.equals(T.getDate())) {
                RecyclerViewAdapter.WeatherTagData weatherTagData = new RecyclerViewAdapter.WeatherTagData();
                int hour = Integer.valueOf(T.getTime().substring(0, 2));
                boolean isNight = (18 <= hour && hour <= 23) || (0 <= hour && hour <= 5);

                weatherTagData.setData(T.getValue(),
                        Integer.valueOf(T.getTime().substring(0, 2)),
                        MainActivity.wxDescriptionToImageResource(overviewString, isNight),
                        true);

                weatherTagDataList.add(weatherTagData);
            }
        }

        // 將weatherTagDataList設定至RecyclerViewAdapter
        recyclerViewAdapter.setDatas(weatherTagDataList);

        // 最後設定gridLayoutManager.spam = count
        gridLayoutManager.setSpanCount(weatherTagDataList.size());
    }


}
