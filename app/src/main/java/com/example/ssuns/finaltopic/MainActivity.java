package com.example.ssuns.finaltopic;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    public static final String baseURL = "http://ntut.com.tw:9000/";
    public static final int REQ_CODE_SPEECH_INPUT = 100;

    private ApiService apiService;

    protected TodayFragment fragTodayWeather;
    protected TomorrowFragment fragTomorrowWeather;
    protected TenDaysFragment fragTenDayWeather;
    protected SectionsPagerAdapter pagerAdapter;

    protected MaterialSearchBar sbSearchBar;
    protected TabLayout tblTabLayout;
    protected ViewPager vpViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initRetrofit();
        initListeners();
        initWeatherData();
    }

    /**
     * 初始化所有的View物件參考。
     */
    private void initViews() {
        fragTodayWeather = new TodayFragment();
        fragTomorrowWeather = new TomorrowFragment();
        fragTenDayWeather = new TenDaysFragment();

        sbSearchBar = findViewById(R.id.sbSearchBar);
        tblTabLayout = findViewById(R.id.tblTabLayout);
        vpViewPager = findViewById(R.id.vpViewPager);

        // Setting up TabLayout and ViewPager's context
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(fragTodayWeather, "今天");
        pagerAdapter.addFragment(fragTomorrowWeather, "明天");
        pagerAdapter.addFragment(fragTenDayWeather, "未來一週");
        vpViewPager.setAdapter(pagerAdapter);

        tblTabLayout.setupWithViewPager(vpViewPager);
    }

    /**
     * 初始化Retrofit
     */
    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 初始化Listeners
     */
    private void initListeners() {
        sbSearchBar.setOnSearchActionListener(this);
    }

    /**
     * 初始化天氣資料
     */
    private void initWeatherData() {
        sbSearchBar.setText("臺北市");
        sbSearchBar.setPlaceHolder("臺北市");
        updateDateAllDatas();
    }

    /**
     * 當聚焦、失焦於搜尋條時。(Focus)
     * @param enabled 是否聚焦於搜尋條上。
     */
    @Override
    public void onSearchStateChanged(boolean enabled) { }

    /**
     * 當使用者於搜尋條輸入完後，按下「搜尋」後的處理。
     * @param text 使用者輸入的文字。
     */
    @Override
    public void onSearchConfirmed(CharSequence text) {
        sbSearchBar.setPlaceHolder(text);
        updateDateAllDatas();
        closeKeyboard();
    }

    /**
     * 當搜尋條上所配置的按鈕按下後的處理。
     * @param buttonCode 按鈕代碼。
     */
    @Override
    public void onButtonClicked(int buttonCode) {
        promptSpeechInput();
    }

    /**
     * 關閉鍵盤。
     */
    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 更新所有資料：兩天天氣資料以及一週天氣資料。
     */
    public void updateDateAllDatas() {
        String cityName = sbSearchBar.getText();
        apiService.getTwoDaysWeatherData(cityName).enqueue(responseOfTwoDays);
        apiService.getOneWeekWeatherData(cityName).enqueue(responseOfOneWeek);
        //apiService.test(cityName).enqueue(responseOfTest);
        Toast.makeText(this, "更新天氣資料...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sbSearchBar.setText(result.get(0));
                    sbSearchBar.setPlaceHolder(result.get(0));
                    updateDateAllDatas();
                }
                break;
            }

        }
    }

    Callback<String> responseOfTest = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            Log.d("RspBody", response.body());
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Log.d("Rsp", t.getMessage());
        }
    };

    /**
     * 抓取「兩天天氣」的資料後的處理。
     */
    Callback<TwoDaysJson> responseOfTwoDays = new Callback<TwoDaysJson>() {
        @Override
        public void onResponse(@NonNull Call<TwoDaysJson> call, Response<TwoDaysJson> response) {
            TwoDaysJson jsonData = response.body();
            if (jsonData.getSuccess()) {
                fragTodayWeather.updateJsonData(jsonData);
                fragTomorrowWeather.updateJsonData(jsonData);
            }
            else {
                Toast.makeText(MainActivity.this,
                        "找不到「" + sbSearchBar.getText() + "」的近期兩天之天氣資料。",
                        Toast.LENGTH_SHORT).show();
            }
            fragTodayWeather.stopRefreshing();
            fragTomorrowWeather.stopRefreshing();
        }

        @Override
        public void onFailure(@NonNull Call<TwoDaysJson> call, @NonNull Throwable t) {
            Toast.makeText(MainActivity.this, "無法取得「未來兩天天氣資料」，請檢查您的網路狀態或詢問開發者。", Toast.LENGTH_SHORT).show();
            fragTodayWeather.stopRefreshing();
            fragTomorrowWeather.stopRefreshing();
        }
    };

    /**
     * 從簡短天氣敘述中取得相對應的天氣Icon圖示。
     * @param desc 簡短天氣敘述。
     * @param isNight 是否為晚上。
     * @return 天氣圖示資源。
     */
    public static int wxDescriptionToImageResource(String desc, boolean isNight) {
        if (desc.contains("雷雨")) {
            return R.drawable.thunderstorm;
        }
        else if (desc.contains("豪雨") || desc.contains("大豪雨") || desc.contains("豪大雨")) {
            return R.drawable.torrential_rain;
        }
        else if (desc.contains("短暫陣雨") && !desc.contains("多雲") && !desc.contains("陰")) {
            return isNight ? R.drawable.night_rainny : R.drawable.mostly_cloudy_rainny;
        }
        else if (desc.contains("陣雨") || desc.contains("雨")) {
            return R.drawable.rainny_day;
        }
        else if (desc.equals("晴時多雲") || desc.equals("多雲時晴")) {
            return isNight ? R.drawable.night_cloudy : R.drawable.mostly_clear;
        }
        else if (desc.equals("晴")) {
            return isNight ? R.drawable.night_clear : R.drawable.sunny_day;
        }
        else if (desc.contains("多雲")) {
            return R.drawable.cloudy_day;
        }
        else {
            return R.drawable.cloudy_day;
        }
    }

    /**
     * 抓取「一週天氣」的資料後的處理。
     */
    Callback<OneWeekJson> responseOfOneWeek = new Callback<OneWeekJson>() {
        @Override
        public void onResponse(@NonNull Call<OneWeekJson> call, Response<OneWeekJson> response) {
            OneWeekJson jsonData = response.body();
            if (jsonData.getSuccess()) {
                fragTenDayWeather.updateViewDataByJson(response.body());
            }
            else {
                Toast.makeText(MainActivity.this,
                        "找不到「" + sbSearchBar.getText() + "」的一週天氣資料。",
                        Toast.LENGTH_SHORT).show();
            }
            fragTenDayWeather.stopRefreshing();
        }

        @Override
        public void onFailure(@NonNull Call<OneWeekJson> call,@NonNull Throwable t) {
            Toast.makeText(MainActivity.this, "無法取得「一週天氣資料」，請檢查您的網路狀態或詢問開發者。", Toast.LENGTH_SHORT).show();
            fragTenDayWeather.stopRefreshing();
        }
    };

    /**
     * 處理TabLayout與ViewPager之間的關聯。
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<String> tabNames = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames.get(position);
        }

        public void addFragment(Fragment fragment, String tabName) {
            fragments.add(fragment);
            tabNames.add(tabName);
        }
    }
}
