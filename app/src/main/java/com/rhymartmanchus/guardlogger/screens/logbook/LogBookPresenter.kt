package com.rhymartmanchus.guardlogger.screens.logbook

import com.rhymartmanchus.guardlogger.domain.IAppDispatchers
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.interactors.GetCheckInLogsUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.SaveCheckInLogUseCase
import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LogBookPresenter @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val getCheckInLogsUseCase: GetCheckInLogsUseCase,
    private val saveCheckInLogUseCase: SaveCheckInLogUseCase
) : LogBookContract.Presenter, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = appDispatchers.ui() + SupervisorJob()
    private var view: LogBookContract.View? = null
    private val cachedLogs: MutableList<CheckInLog> = mutableListOf()

    override fun takeView(view: LogBookContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun onViewCreated() {
        launch {
            try {
                val logs = withContext(appDispatchers.io()) {
                    getCheckInLogsUseCase.execute(Unit)
                }.logs
                cachedLogs.addAll(logs)
                view?.renderCheckInLogs(logs)
                view?.hideNoLogsAvailable()
            } catch (e: NoDataException) {
                // No data
            }
        }
    }

    override fun onAddLogClicked() {
        launch {
            view?.showAddLogDialog { startTime, endTime, logs ->
                val result = withContext(appDispatchers.io()) {
                    saveCheckInLogUseCase.execute(
                        SaveCheckInLogUseCase.Params(
                            startTime,
                            endTime,
                            logs)
                    )
                }.checkInLog
                cachedLogs.add(0, result)
                view?.appendCheckInLog(result)
                view?.hideNoLogsAvailable()
            }
        }
    }

    override fun onLogClicked(position: Int) {
        view?.showLogDetailsDialog(cachedLogs[position])
    }

}