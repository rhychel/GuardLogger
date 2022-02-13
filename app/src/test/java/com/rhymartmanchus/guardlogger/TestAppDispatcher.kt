package com.rhymartmanchus.guardlogger

import com.rhymartmanchus.guardlogger.domain.IAppDispatchers
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class TestAppDispatcher : IAppDispatchers {
    override fun io(): CoroutineContext = Dispatchers.Unconfined

    override fun ui(): CoroutineContext = Dispatchers.Unconfined
}