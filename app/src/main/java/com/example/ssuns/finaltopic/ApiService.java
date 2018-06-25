package com.example.ssuns.finaltopic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("twodays/{city}")
    Call<TwoDaysJson> getTwoDaysWeatherData(@Path("city") String newsId);

    @GET("week/{city}")
    Call<OneWeekJson> getOneWeekWeatherData(@Path("city") String newsId);

    @GET("week/{city}")
    Call<String> test(@Path("city") String newsId);
}
