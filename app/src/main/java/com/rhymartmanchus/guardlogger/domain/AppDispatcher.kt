package com.rhymartmanchus.guardlogger.domain

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AppDispatcher @Inject constructor() : IAppDispatchers {
    override fun io(): CoroutineContext = Dispatchers.IO

    override fun ui(): CoroutineContext = Dispatchers.Main
}