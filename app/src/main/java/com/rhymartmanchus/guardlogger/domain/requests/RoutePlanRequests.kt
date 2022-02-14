package com.rhymartmanchus.guardlogger.domain.requests

import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation

sealed interface RoutePlanRequests {

    data class Start (
        val routePlanId: Int,
        val time: String
    ) : RoutePlanRequests

    data class End (
        val routePlanId: Int,
        val time: String
    ) : RoutePlanRequests

    data class Create(
        val userId: Int,
        val patrolLocations: List<PatrolLocation>
    ) : RoutePlanRequests

    data class Reset(
        val routePlanId: Int,
        val patrolLocations: List<PatrolLocation>
    ) : RoutePlanRequests

    data class SaveArrangementLocation(
        val patrolLocationId: Int,
        val sorting: String
    ) : RoutePlanRequests

    data class SaveLog(
        val startTime: String,
        val endTime: String,
        val patrolLocationId: Int,
        val isCleared: Boolean,
        val remarks: String?
    ) : RoutePlanRequests
}
