package com.rhymartmanchus.guardlogger.data

import com.rhymartmanchus.guardlogger.data.db.AreaLocationsDao
import com.rhymartmanchus.guardlogger.data.db.CheckInLogsDao
import com.rhymartmanchus.guardlogger.data.db.PatrolLocationsDao
import com.rhymartmanchus.guardlogger.data.db.RoutePlansDao
import com.rhymartmanchus.guardlogger.data.db.models.CheckInLogDB
import com.rhymartmanchus.guardlogger.data.db.models.PatrolLocationDB
import com.rhymartmanchus.guardlogger.data.db.models.RoutePlanDB
import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.AreaLocation
import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import com.rhymartmanchus.guardlogger.domain.requests.CheckInRequest
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import javax.inject.Inject

class SecurityLogsRepository @Inject constructor(
    private val areaLocationsDao: AreaLocationsDao,
    private val checkInLogsDao: CheckInLogsDao,
    private val patrolLocationsDao: PatrolLocationsDao,
    private val routePlansDao: RoutePlansDao
) : SecurityLogsGateway {

    override suspend fun getCheckInLogs(): List<CheckInLog> =
        checkInLogsDao.getAllCheckInLogs()
            .map {
                CheckInLog(
                    it.user.name,
                    it.checkInLog.startTime,
                    it.checkInLog.endTime,
                    it.checkInLog.description
                )
            }
            .takeIf { it.isNotEmpty() } ?: throw NoDataException()

    override suspend fun getPatrolRoutePlan(userId: Int): RoutePlan =
        routePlansDao.getRoutePlanWithPatrolLocations(userId)
            ?.let {
                RoutePlan(
                    it.routePlan.id,
                    it.routePlan.hasStarted,
                    it.routePlan.isDone,
                    it.routePlan.startTime,
                    it.routePlan.endTime,
                    it.patrolLocations.map {
                        PatrolLocation(
                            it.id,
                            it.areaLocationId,
                            it.sorting,
                            it.name,
                            it.startTime,
                            it.endTime,
                            it.isVisited,
                            it.isCleared,
                            it.remarks
                        )
                    }
                )
            } ?: throw NoDataException()

    override suspend fun getLocations(): List<AreaLocation> =
        areaLocationsDao.getAllAreaLocations()
            .map {
                AreaLocation(
                    it.id,
                    it.name,
                    it.sorting
                )
            }
            .takeIf { it.isNotEmpty() } ?: throw NoDataException()

    override suspend fun saveRoutePlan(routePlanRequests: RoutePlanRequests): Int {
        when(routePlanRequests) {
            is RoutePlanRequests.SaveArrangementLocation -> {
                val patrolLocation = patrolLocationsDao.getPatrolLocationById(routePlanRequests.patrolLocationId)
                    ?: throw NoDataException()
                patrolLocationsDao.savePartolLocationLog(
                    patrolLocation.copy(
                        sorting = routePlanRequests.sorting
                    )
                )
            }
            is RoutePlanRequests.Create -> {
                routePlansDao.saveRoutePlan(
                    RoutePlanDB(
                        0,
                        false,
                        false,
                        "",
                        "",
                        routePlanRequests.userId
                    )
                )
                val savedRoutePlan = routePlansDao.getRoutePlanByUserId(routePlanRequests.userId)
                    ?: throw IllegalStateException("Unable to save Route Plan")
                patrolLocationsDao.savePatrolLocations(
                    routePlanRequests.patrolLocations
                        .map {
                            PatrolLocationDB(
                                0,
                                savedRoutePlan.id,
                                it.areaLocationId,
                                it.name,
                                it.startTime,
                                it.endTime,
                                it.isVisited,
                                it.isCleared,
                                it.sorting,
                                it.remarks
                            )
                        }
                )
            }
            is RoutePlanRequests.Start -> {
                val routePlan = routePlansDao.getRoutePlanById(routePlanRequests.routePlanId)
                    ?: throw NoDataException()
                routePlansDao.saveRoutePlan(
                    routePlan.copy(
                        startTime = routePlanRequests.time,
                        hasStarted = true
                    )
                )
            }
            is RoutePlanRequests.End -> {
                val routePlan = routePlansDao.getRoutePlanById(routePlanRequests.routePlanId)
                    ?: throw NoDataException()
                routePlansDao.saveRoutePlan(
                    routePlan.copy(
                        isDone = true,
                        endTime = routePlanRequests.time
                    )
                )
            }
            is RoutePlanRequests.Reset -> {
                patrolLocationsDao.deletePatrolLocations(
                    routePlanRequests.routePlanId
                )
                patrolLocationsDao.savePatrolLocations(
                    routePlanRequests.patrolLocations
                        .map {
                            PatrolLocationDB(
                                it.id,
                                routePlanRequests.routePlanId,
                                it.areaLocationId,
                                it.name,
                                it.startTime,
                                it.endTime,
                                it.isVisited,
                                it.isCleared,
                                it.sorting,
                                it.remarks
                            )
                        }
                )
            }
            is RoutePlanRequests.SaveLog -> {
                val patrolLocation = patrolLocationsDao.getPatrolLocationById(routePlanRequests.patrolLocationId)
                    ?: throw NoDataException()
                patrolLocationsDao.savePartolLocationLog(
                    patrolLocation.copy(
                        startTime = routePlanRequests.startTime,
                        endTime = routePlanRequests.endTime,
                        isCleared = routePlanRequests.isCleared,
                        isVisited = true,
                        remarks = routePlanRequests.remarks
                    )
                )
            }
        }
        return -1
    }

    override suspend fun saveCheckInLog(checkInRequest: CheckInRequest) {
        checkInLogsDao.saveCheckInLog(
            checkInRequest.let {
                CheckInLogDB(
                    0,
                    it.userId,
                    it.startTime,
                    it.endTime,
                    it.logs
                )
            }
        )
    }
}