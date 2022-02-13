package com.rhymartmanchus.guardlogger.data

import com.google.gson.GsonBuilder
import com.rhymartmanchus.guardlogger.Constants
import com.rhymartmanchus.guardlogger.data.api.CurrentWeatherSerializer
import com.rhymartmanchus.guardlogger.data.api.models.CurrentWeatherRaw
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeathersRepositoryTest {

    lateinit var retrofit: Retrofit

    lateinit var weathersRepository: WeathersRepository

    @Before
    fun setUp() {

        retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(CurrentWeatherRaw::class.java, CurrentWeatherSerializer())
                        .create()
                )
            )
            .baseUrl(Constants.API_DOMAIN)
            .build()

        weathersRepository = WeathersRepository(retrofit)
    }

    @Test
    fun fetchWeatherByCoordinates() = runBlocking {

        val weather = weathersRepository.fetchWeatherByCoordinates(
            13.6610091,
            123.1801302
        )

        assertEquals("PH", weather.country)
        assertEquals("Magarao", weather.city)
    }
}