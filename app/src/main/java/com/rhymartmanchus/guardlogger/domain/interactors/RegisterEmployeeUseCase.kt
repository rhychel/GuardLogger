package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import javax.inject.Inject

class RegisterEmployeeUseCase @Inject constructor(
    private val gateway: ShiftsGateway
) : UseCase<RegisterEmployeeUseCase.Params, Unit>() {

    data class Params (
        val employeeId: String,
        val name: String,
        val pin: String
    )

    override suspend fun execute(params: Params) {
        if(params.employeeId.isEmpty()) {
            throw IllegalArgumentException("Employee ID cannot be empty")
        }
        if(params.name.isEmpty()) {
            throw IllegalArgumentException("Name cannot be empty")
        }
        if(params.pin.isEmpty()) {
            throw IllegalArgumentException("PIN cannot be empty")
        }

        gateway.signup(
            params.employeeId,
            params.name,
            params.pin
        )
    }

}