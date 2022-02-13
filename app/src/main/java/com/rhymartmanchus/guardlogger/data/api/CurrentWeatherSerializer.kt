package com.rhymartmanchus.guardlogger.data.api

import com.google.gson.JsonObject

import com.rhymartmanchus.guardlogger.data.api.models.CurrentWeatherRaw

import com.google.gson.GsonBuilder

import com.google.gson.Gson

import com.google.gson.JsonParseException

import com.google.gson.JsonDeserializationContext

import com.google.gson.JsonElement

import com.google.gson.JsonDeserializer
import java.lang.reflect.Type


class CurrentWeatherSerializer : JsonDeserializer<CurrentWeatherRaw?> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CurrentWeatherRaw {
        val gson = GsonBuilder().create()
        val currentWeatherRaw = gson.fromJson(
            json,
            CurrentWeatherRaw::class.java
        )
        val currentWeather = json.asJsonObject
        val main = currentWeather.getAsJsonObject("main")
        return currentWeatherRaw.copy(
            country = currentWeather.getAsJsonObject("sys")["country"].asString,
            temperature = main["temp"].asDouble,
            minimumTemperature = main["temp_min"].asDouble,
            maximumTemperature = main["temp_max"].asDouble,
            cloudsPercentage = currentWeather.getAsJsonObject("clouds")["all"].asDouble
        )
    }
}