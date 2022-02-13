package com.rhymartmanchus.guardlogger.screens.logbook

import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import com.rhymartmanchus.guardlogger.domain.requests.CheckInRequest

sealed interface LogBookContract {

    interface View {

        fun showLogDetailsDialog(
            checkInLog: CheckInLog
        )
        suspend fun showAddLogDialog(
            onSaveClicked: (CheckInRequest) -> Unit,
            onDismissClicked: () -> Unit
        )
        fun renderCheckInLogs(
            checkInLogs: List<CheckInLog>
        )

    }

    interface Presenter {

        fun onViewCreated()
        fun onAddLogClicked()
        fun onLogClicked(logId: Int)

    }

}