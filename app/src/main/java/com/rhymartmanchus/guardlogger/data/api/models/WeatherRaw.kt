package com.rhymartmanchus.guardlogger.data.api.models

import com.google.gson.annotations.SerializedName

data class WeatherRaw (
    @SerializedName("id")
    val id: Int,

    @SerializedName("main")
    val main: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val iconCode: String
)