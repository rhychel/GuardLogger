package com.rhymartmanchus.guardlogger.di

import com.rhymartmanchus.guardlogger.data.SecurityLogsRepository
import com.rhymartmanchus.guardlogger.data.ShiftsRepository
import com.rhymartmanchus.guardlogger.data.WeathersRepository
import com.rhymartmanchus.guardlogger.domain.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBinderModule {

    @Binds
    abstract fun bindAppDispatchers(
        appDispatcher: AppDispatcher
    ): IAppDispatchers

    @Binds
    abstract fun bindSecurityLogsRepository(
        securityLogsRepository: SecurityLogsRepository
    ): SecurityLogsGateway

    @Binds
    abstract fun bindShiftsRepository(
        shiftsRepository: ShiftsRepository
    ): ShiftsGateway

    @Binds
    abstract fun bindWeathersRepository(
        weathersRepository: WeathersRepository
    ): WeathersGateway

}