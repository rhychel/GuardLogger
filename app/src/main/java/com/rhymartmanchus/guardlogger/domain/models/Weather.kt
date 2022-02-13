package com.rhymartmanchus.guardlogger.domain.models

data class Weather (
    val main: String,
    val description : String,
    val icon: String,
    val city: String,
    val country: String,
    val temperature: Double,
    val minimumTemperature: Double,
    val maximumTemperature: Double,
    val cloudsPercentage: Double
)