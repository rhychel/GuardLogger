package com.rhymartmanchus.guardlogger.screens.routeplan

import com.rhymartmanchus.guardlogger.domain.IAppDispatchers
import com.rhymartmanchus.guardlogger.domain.interactors.*
import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import com.rhymartmanchus.guardlogger.utils.DateTimeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.log

class RoutePlanPresenter @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val getActiveRoutePlanUseCase: GetActiveRoutePlanUseCase,
    private val savePatrolLocationSortingUseCase: SavePatrolLocationSortingUseCase,
    private val startRouteUseCase: StartRouteUseCase,
    private val endRouteUseCase: EndRouteUseCase,
    private val resetRoutePlanUseCase: ResetRoutePlanUseCase,
    private val savePatrolLogUseCase: SavePatrolLogUseCase
) : RoutePlanContract.Presenter, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = appDispatchers.ui() + SupervisorJob()
    private var view: RoutePlanContract.View? = null
    private val cachedPatrolLocations: MutableList<PatrolLocation> = mutableListOf()
    private lateinit var cachedRoutePlan: RoutePlan

    override fun takeView(view: RoutePlanContract.View) {
        this.view = view
    }

    override fun onViewCreated() {
        launch {
            cachedRoutePlan = withContext(appDispatchers.io()) {
                getActiveRoutePlanUseCase.execute(Unit)
            }.routePlan
            cachedPatrolLocations.addAll(cachedRoutePlan.locations.sortedBy { it.sorting })
            view?.renderRoutePlan(cachedRoutePlan, cachedPatrolLocations)
            if(cachedRoutePlan.hasStarted) {
                view?.showFinishButton()
            }
        }
    }

    override fun onStartRouteClicked() {
        launch {
            withContext(appDispatchers.io()) {
                startRouteUseCase.execute(
                    StartRouteUseCase.Params(
                        cachedRoutePlan.id,
                        DateTimeFormatter.toDateTime(
                            Calendar.getInstance().time
                        )
                    )
                )
            }
            view?.refreshLocations(true, cachedPatrolLocations)
            view?.showFinishButton()
        }
    }

    override fun onEndRouteClicked() {
        launch {
            withContext(appDispatchers.io()) {
                endRouteUseCase.execute(
                    EndRouteUseCase.Params(
                        cachedRoutePlan.id,
                        DateTimeFormatter.toDateTime(
                            Calendar.getInstance().time
                        )
                    )
                )
            }
            view?.toastRoutePlanIsCompleted()
            view?.navigateToHome()
        }
    }

    override fun onLocationClicked(patrolLocation: PatrolLocation) {
        launch {
            view?.showPatrolLogDialog(
                patrolLocation.name) { startTime, endTime, isCleared, logs ->
                withContext(appDispatchers.io()) {
                    savePatrolLogUseCase.execute(
                        SavePatrolLogUseCase.Params(
                            patrolLocation.id,
                            startTime,
                            endTime,
                            isCleared,
                            logs
                        )
                    )
                }
                view?.refreshPatrolLocation(
                    patrolLocation.copy(
                        startTime = startTime,
                        endTime = endTime,
                        isCleared = isCleared,
                        isVisited = true,
                        remarks = logs
                    )
                )
            }
        }
    }

    override fun rearrangePatrolLocation(
        fromPatrolLocation: PatrolLocation,
        toPatrolLocation: PatrolLocation
    ) {
        val fromIndex = cachedPatrolLocations.indexOfFirst { it.id == fromPatrolLocation.id }
        val toIndex = cachedPatrolLocations.indexOfFirst { it.id == toPatrolLocation.id }

        val moved = cachedPatrolLocations.removeAt(toIndex)
        cachedPatrolLocations.add(fromIndex, moved)
        launch {
            withContext(appDispatchers.io()) {
                cachedPatrolLocations.forEachIndexed { index, patrolLocation ->
                    savePatrolLocationSortingUseCase.execute(
                        SavePatrolLocationSortingUseCase.Params(
                            patrolLocation.id,
                            ".".repeat(index + 1)
                        )
                    )
                }
            }
        }
    }

    override fun onResetPatrolLocationArrangementClicked() {
        launch {
            val resetRoutePlan = resetRoutePlanUseCase.execute(Unit).resetRoutePlan
            cachedRoutePlan = resetRoutePlan
            cachedPatrolLocations.clear()
            cachedPatrolLocations.addAll(resetRoutePlan.locations)
            view?.resetRoutePlanLocations(cachedPatrolLocations)
        }
    }

}