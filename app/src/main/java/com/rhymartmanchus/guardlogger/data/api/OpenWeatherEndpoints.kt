package com.rhymartmanchus.guardlogger.data.api

import com.rhymartmanchus.guardlogger.data.api.models.CurrentWeatherRaw
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherEndpoints {

    @GET("/data/2.5/weather")
    suspend fun fetchCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): CurrentWeatherRaw

}