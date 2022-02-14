package com.rhymartmanchus.guardlogger.screens.logbook

import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import com.rhymartmanchus.guardlogger.domain.requests.CheckInRequest

sealed interface LogBookContract {

    interface View {

        fun showLogDetailsDialog(
            checkInLog: CheckInLog
        )
        fun showAddLogDialog(
            onSaveClicked: suspend (startTime: String, endTime: String, logs: String) -> Unit
        )
        fun renderCheckInLogs(
            checkInLogs: List<CheckInLog>
        )
        fun hideNoLogsAvailable()
        fun appendCheckInLog(
            checkInLog: CheckInLog
        )

    }

    interface Presenter {

        fun takeView(view: View)
        fun detachView()
        fun onViewCreated()
        fun onAddLogClicked()
        fun onLogClicked(position: Int)

    }

}