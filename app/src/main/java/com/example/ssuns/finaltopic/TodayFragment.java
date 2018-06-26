package com.example.ssuns.finaltopic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class TodayFragment extends Fragment {

    private RecyclerViewAdapter recyclerViewAdapter;

    private PullRefreshLayout parentLayout;
    private TextView txvDate, txvTemperatureNow;
    private TextView txvHumidity, txvDewPoint, txvWindSpeed, txvATTempurature, txvCIPoint;
    private TextView txvDescription;
    private ImageView imgWeather;
    private RecyclerView recyclerViewTemperature;
    private GridLayoutManager gridLayoutManager;

    private TwoDaysJson jsonData;

    private Timer clockTimer;

    public TodayFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set 1-second timer
        clockTimer = new Timer();
        clockTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                clockHandler.obtainMessage().sendToTarget();
            }
        }, 0, 1000);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        Context context = view.getContext();
        parentLayout = view.findViewById(R.id.parent_layout_today);
        parentLayout.setOnRefreshListener(pullRefreshListener);
        txvDate = view.findViewById(R.id.txt_date);
        Calendar now = Calendar.getInstance();
        String ampm = now.get(Calendar.AM_PM) == Calendar.AM ? "上午" : "下午";
        @SuppressLint("DefaultLocale")
        String dateString = String.format("%1$tm月%1$td日 %2$s%1$tl:%1$tM", now, ampm);
        txvDate.setText(dateString);
        txvTemperatureNow = view.findViewById(R.id.txt_temperature_now);

        txvHumidity = view.findViewById(R.id.txt_humidity);
        txvDewPoint = view.findViewById(R.id.txt_dew_point);
        txvWindSpeed = view.findViewById(R.id.txt_wind_speed);
        txvATTempurature = view.findViewById(R.id.txt_AT_temp);
        txvCIPoint = view.findViewById(R.id.txt_CI_point);

        txvDescription = view.findViewById(R.id.txvDescription);

        imgWeather = view.findViewById(R.id.imgWeather);

        recyclerViewAdapter = new RecyclerViewAdapter(context);
        recyclerViewTemperature = view.findViewById(R.id.recycler_view_temperature);
        recyclerViewTemperature.setAdapter(recyclerViewAdapter);
        gridLayoutManager = new GridLayoutManager(context, 4);

        recyclerViewTemperature.setLayoutManager(gridLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (jsonData != null) {
            updateViewDataByJson();
        }
    }

    // When user pull to refresh
    private PullRefreshLayout.OnRefreshListener pullRefreshListener = new PullRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            ((MainActivity) TodayFragment.this.getActivity()).updateDateAllDatas();
        }
    };

    /**
     * 停止重新整理的Icon圖示。
     */
    public void stopRefreshing() {
        parentLayout.setRefreshing(false);
    }

    public void updateJsonData(TwoDaysJson twoDaysJson) {
        this.jsonData = twoDaysJson;
        updateViewDataByJson();
    }

    /**
     * 藉由JSON資料來更新版面上元件內容資料。
     */
    public void updateViewDataByJson() {
        if (jsonData == null) return;
        TwoDaysJson.WeatherElementBean weatherElement = jsonData.getWeatherElement();
        TwoDaysJson.WeatherElementBean.ATBean AT = null;
        TwoDaysJson.WeatherElementBean.CIBean CI = null;
        //List<TwoDaysJson.WeatherElementBean.PoP6hBean> allPoP6h = weatherElement.getPoP6h();  //!
        TwoDaysJson.WeatherElementBean.RHBean RH = null;
        TwoDaysJson.WeatherElementBean.TdBean Td = null;
        TwoDaysJson.WeatherElementBean.WeatherDescriptionBean weatherDescription = null;
        TwoDaysJson.WeatherElementBean.WSBean WS = null;
        TwoDaysJson.WeatherElementBean.WxBean Wx = null;
        List<TwoDaysJson.WeatherElementBean.TBean> TList = weatherElement.getT();

        if (weatherElement.getAT().size() >= 1)
            AT = weatherElement.getAT().get(0);
        if (weatherElement.getCI().size() >= 1)
            CI = weatherElement.getCI().get(0);
        if (weatherElement.getRH().size() >= 1)
            RH = weatherElement.getRH().get(0);
        if (weatherElement.getTd().size() >= 1)
            Td = weatherElement.getTd().get(0);
        if (weatherElement.getWeatherDescription().size() >= 1)
            weatherDescription = weatherElement.getWeatherDescription().get(0);
        if (weatherElement.getWS().size() >= 1)
            WS = weatherElement.getWS().get(0);
        if (weatherElement.getWx().size() >= 1)
            Wx = weatherElement.getWx().get(0);

        String ATString = AT == null ? "未提供資料" : String.valueOf(AT.getValue());
        String CIPointString = CI == null ? "未提供資料" : CI.getValue();
        String humidityString = RH == null ? "未提供資料" : String.valueOf(RH.getValue()) + "%";
        String nowTemperature = TList.size() == 0 ? "ND" : String.valueOf(TList.get(0).getValue()) + "°C";
        String dewPointString = Td == null ? "未提供資料" : String.valueOf(Td.getValue()) + "°";
        String description = weatherDescription == null ? "無敘述資料提供" : weatherDescription.getValue();
        String windSpeedString = WS == null ? "未提供資料" : String.valueOf(WS.getValue());
        String wxDescription = Wx == null ? "無敘述資料提供" : Wx.getValue();

        txvATTempurature.setText(ATString);
        txvCIPoint.setText(CIPointString);
        txvHumidity.setText(humidityString);
        txvTemperatureNow.setText(nowTemperature);
        txvDewPoint.setText(dewPointString);
        txvDescription.setText(description);
        txvWindSpeed.setText(windSpeedString);

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR);
        boolean isNight = (18 <= hour && hour <= 23) || (0 <= hour && hour <= 5);
        imgWeather.setImageResource(MainActivity.wxDescriptionToImageResource(wxDescription, isNight));

        @SuppressLint("DefaultLocale") String nowDateString = String.format("%1$tY-%1$tm-%1$td", now);
        ArrayList<RecyclerViewAdapter.WeatherTagData> weatherTagDataList = new ArrayList<>();
        int size = TList.size();

        // 循著溫度資料清單來判斷
        for (int i = 0; weatherTagDataList.size() <= 4 && i < size; i++) {
            TwoDaysJson.WeatherElementBean.TBean T = TList.get(i);
            // 比對日期是否為今天
            if (nowDateString.equals(T.getDate())) {
                RecyclerViewAdapter.WeatherTagData weatherTagData = new RecyclerViewAdapter.WeatherTagData();
                hour = Integer.valueOf(T.getTime().substring(0, 2));
                isNight = (18 <= hour && hour <= 23) || (0 <= hour && hour <= 5);
                weatherTagData.setData(T.getValue(),
                        Integer.valueOf(T.getTime().substring(0, 2)),
                        MainActivity.wxDescriptionToImageResource(wxDescription, isNight),
                        true);

                weatherTagDataList.add(weatherTagData);
            }
        }

        // 將weatherTagDataList設定至RecyclerViewAdapter
        recyclerViewAdapter.setDatas(weatherTagDataList);

        // 最後設定gridLayoutManager.spam = count
        gridLayoutManager.setSpanCount(weatherTagDataList.size());
    }

    // Clock Timer
    @SuppressLint("HandlerLeak")
    private Handler clockHandler = new Handler() {
        public void handleMessage(Message msg) {
            Calendar rightNow = Calendar.getInstance();
            String coma = rightNow.get(Calendar.SECOND) % 2 == 0 ? ":" : " ";
            String ampm = rightNow.get(Calendar.AM_PM) == Calendar.AM ? "上午" : "下午";
            @SuppressLint("DefaultLocale")
            String dateString =
                    String.format("%1$tm月%1$td日 %2$s%1$tl%3$s%1$tM", rightNow, ampm, coma);
            if (txvDate != null)
                txvDate.setText(dateString);
        }
    };
}
