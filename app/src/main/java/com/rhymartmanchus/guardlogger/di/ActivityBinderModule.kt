package com.rhymartmanchus.guardlogger.di

import com.rhymartmanchus.guardlogger.screens.home.HomeContract
import com.rhymartmanchus.guardlogger.screens.home.HomePresenter
import com.rhymartmanchus.guardlogger.screens.logbook.LogBookContract
import com.rhymartmanchus.guardlogger.screens.logbook.LogBookPresenter
import com.rhymartmanchus.guardlogger.screens.login.MainContract
import com.rhymartmanchus.guardlogger.screens.login.MainPresenter
import com.rhymartmanchus.guardlogger.screens.routeplan.RoutePlanContract
import com.rhymartmanchus.guardlogger.screens.routeplan.RoutePlanPresenter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityBinderModule {

    @Binds
    abstract fun provideMainPresenter(
        mainPresenter: MainPresenter
    ): MainContract.Presenter

    @Binds
    abstract fun provideHomePresenter(
        homePresenter: HomePresenter
    ): HomeContract.Presenter

    @Binds
    abstract fun provideLogBookPresenter(
        logBookPresenter: LogBookPresenter
    ): LogBookContract.Presenter

    @Binds
    abstract fun provideRoutePlanPresenter(
        routePlanPresenter: RoutePlanPresenter
    ): RoutePlanContract.Presenter

}