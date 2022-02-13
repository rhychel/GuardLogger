package com.rhymartmanchus.guardlogger.data.api.models

import com.google.gson.annotations.SerializedName

data class CurrentWeatherRaw (
    @SerializedName("name")
    val cityName: String,

    @SerializedName("temperature")
    val temperature: Double,

    @SerializedName("minimumTemperature")
    val minimumTemperature: Double,

    @SerializedName("maximumTemperature")
    val maximumTemperature: Double,

    @SerializedName("cloudsPercentage")
    val cloudsPercentage: Double,

    @SerializedName("country")
    val country: String,

    @SerializedName("weather")
    val weather: List<WeatherRaw>
)