package com.rhymartmanchus.guardlogger.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.rhymartmanchus.guardlogger.Constants
import com.rhymartmanchus.guardlogger.data.api.CurrentWeatherSerializer
import com.rhymartmanchus.guardlogger.data.api.models.CurrentWeatherRaw
import com.rhymartmanchus.guardlogger.data.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppProviderModule {

    @Provides
    fun provideGuardLoggerDatabase(
        @ApplicationContext context: Context
    ): GuardLoggerDatabase =
        GuardLoggerDatabase.getInstance(context)

    @Provides
    fun provideAreaLocationsDao(guardLoggerDatabase: GuardLoggerDatabase): AreaLocationsDao =
        guardLoggerDatabase.areaLocationsDao()

    @Provides
    fun provideAuthenticationsDao(guardLoggerDatabase: GuardLoggerDatabase): AuthenticationsDao =
        guardLoggerDatabase.authenticationsDao()

    @Provides
    fun provideCheckInLogsDao(guardLoggerDatabase: GuardLoggerDatabase): CheckInLogsDao =
        guardLoggerDatabase.checkInLogsDao()

    @Provides
    fun providePatrolLocationsDao(guardLoggerDatabase: GuardLoggerDatabase): PatrolLocationsDao =
        guardLoggerDatabase.patrolLocationsDao()

    @Provides
    fun provideRoutePlansDao(guardLoggerDatabase: GuardLoggerDatabase): RoutePlansDao =
        guardLoggerDatabase.routePlansDao()

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences("session", Context.MODE_PRIVATE)

    @Provides
    fun providerRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(CurrentWeatherRaw::class.java, CurrentWeatherSerializer())
                        .create()
                )
            )
            .baseUrl(Constants.API_DOMAIN)
            .build()

}