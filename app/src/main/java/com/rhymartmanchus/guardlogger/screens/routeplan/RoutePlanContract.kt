package com.rhymartmanchus.guardlogger.screens.routeplan

import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests

sealed interface RoutePlanContract {

    interface View {

        fun showEndRoutePlanButton()
        fun renderRoutePlan(
            routePlan: RoutePlan
        )
        suspend fun showPatrolLogDialog(
            onSaveClicked: (RoutePlanRequests.SaveLog) -> Unit,
            onDismissClicked: () -> Unit
        )

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