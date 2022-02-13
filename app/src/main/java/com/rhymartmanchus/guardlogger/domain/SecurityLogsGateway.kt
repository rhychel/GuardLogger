package com.rhymartmanchus.guardlogger.domain

import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import com.rhymartmanchus.guardlogger.domain.models.AreaLocation
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import com.rhymartmanchus.guardlogger.domain.requests.CheckInRequest
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests

interface SecurityLogsGateway {

    suspend fun getCheckInLogs(): List<CheckInLog>
    suspend fun getPatrolRoutePlan(userId: Int): RoutePlan
    suspend fun getLocations(): List<AreaLocation>

    suspend fun saveRoutePlan(routePlanRequests: RoutePlanRequests): Int
    suspend fun saveCheckInLog(checkInRequest: CheckInRequest)

}