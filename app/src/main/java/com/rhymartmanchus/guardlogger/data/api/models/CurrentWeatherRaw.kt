package com.rhymartmanchus.guardlogger.data.api.models

import com.google.gson.annotations.SerializedName

data class CurrentWeatherRaw (
    @SerializedName("name")
    val cityName: String,

    @SerializedName("temp")
    val temperature: Double,

    @SerializedName("temp_min")
    val minimumTemperature: Double,

    @SerializedName("temp_max")
    val maximumTemperature: Double,

    @SerializedName("cloudsPercentage")
    val cloudsPercentage: Double,

    @SerializedName("country")
    val country: String,

    @SerializedName("weather")
    val weather: List<WeatherRaw>
)