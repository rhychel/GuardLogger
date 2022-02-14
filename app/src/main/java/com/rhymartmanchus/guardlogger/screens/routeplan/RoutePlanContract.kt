package com.rhymartmanchus.guardlogger.screens.routeplan

import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests

sealed interface RoutePlanContract {

    interface View {

        fun toastRoutePlanIsCompleted()
        fun navigateToHome()
        fun showFinishButton()
        fun refreshLocations(
            hasStarted: Boolean, locations: List<PatrolLocation>
        )
        fun renderRoutePlan(
            routePlan: RoutePlan, locations: List<PatrolLocation>
        )
        fun resetRoutePlanLocations(
            locations: List<PatrolLocation>
        )
        suspend fun showPatrolLogDialog(
            locationName: String,
            onSaveClicked: suspend (startTime: String, endTime: String, isCleared: Boolean, logs: String) -> Unit
        )
        fun refreshPatrolLocation(patrolLocation: PatrolLocation)

    }

    interface Presenter {

        fun takeView(view: View)
        fun onViewCreated()
        fun onStartRouteClicked()
        fun onEndRouteClicked()
        fun onLocationClicked(
            patrolLocation: PatrolLocation
        )
        fun rearrangePatrolLocation(
            fromPatrolLocation: PatrolLocation,
            toPatrolLocation: PatrolLocation
        )
        fun onResetPatrolLocationArrangementClicked()

    }

}