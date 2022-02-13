package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.models.Session
import javax.inject.Inject

class GetCurrentSessionUseCase @Inject constructor(
    private val gateway: ShiftsGateway
) : UseCase<Unit, GetCurrentSessionUseCase.Response>() {

    data class Response (
        val session: Session
    )

    override suspend fun execute(params: Unit): Response =
        Response(
            gateway.getCurrentSession()
        )

}