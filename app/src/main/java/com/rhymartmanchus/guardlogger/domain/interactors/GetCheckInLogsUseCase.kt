package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import javax.inject.Inject

class GetCheckInLogsUseCase @Inject constructor (
    private val gateway: SecurityLogsGateway
) : UseCase<Unit, GetCheckInLogsUseCase.Response>() {

    data class Response (
        val logs: List<CheckInLog>
    )

    override suspend fun execute(params: Unit): Response =
        Response(
            gateway.getCheckInLogs()
        )

}