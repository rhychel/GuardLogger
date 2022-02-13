package com.rhymartmanchus.guardlogger.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rhymartmanchus.guardlogger.data.db.*
import com.rhymartmanchus.guardlogger.data.db.models.CheckInLogDB
import com.rhymartmanchus.guardlogger.data.db.models.PatrolLocationDB
import com.rhymartmanchus.guardlogger.data.db.models.RoutePlanDB
import com.rhymartmanchus.guardlogger.data.db.models.UserDB
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.requests.CheckInRequest
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecurityLogsRepositoryTest {

    lateinit var areaLocationsDao: AreaLocationsDao
    lateinit var checkInLogsDao: CheckInLogsDao
    lateinit var patrolLocationsDao: PatrolLocationsDao
    lateinit var routePlansDao: RoutePlansDao
    lateinit var authenticationsDao: AuthenticationsDao

    val appContext by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    lateinit var securityLogsRepository: SecurityLogsRepository

    @Before
    fun setUp() = runBlocking {
        val database = GuardLoggerDatabase.getInMemoryInstance(appContext)
        val realDb = GuardLoggerDatabase.getInstance(appContext)
        areaLocationsDao = realDb.areaLocationsDao()
        checkInLogsDao = database.checkInLogsDao()
        patrolLocationsDao = database.patrolLocationsDao()
        routePlansDao = database.routePlansDao()
        authenticationsDao = database.authenticationsDao()

        securityLogsRepository = SecurityLogsRepository(
            areaLocationsDao, checkInLogsDao, patrolLocationsDao, routePlansDao
        )

        authenticationsDao.saveUser(
            UserDB(
                123,
                "Rhy",
                "123-123",
                "1234"
            )
        )
    }

    @After
    fun tearDown() {
        GuardLoggerDatabase.clearAllTables()
        GuardLoggerDatabase.destroyInstance()
    }

    @Test
    fun getCheckInLogs() = runBlocking {
        authenticationsDao.saveUser(
            UserDB(123, "Rhy", "1234-1234", "1234")
        )
        for(r in 1..2) {
            checkInLogsDao.saveCheckInLog(
                CheckInLogDB(0,
                    123,
                    "Feb 12, 2022 - 11:00 AM",
                    "Feb 12, 2022 - 11:00 AM",
                    "This is a description of log"
                )
            )
        }

        val checkInLogs = securityLogsRepository.getCheckInLogs()

        assertEquals(2, checkInLogs.size)
        assertEquals("Rhy", checkInLogs[0].employeeName)
        assertEquals("Feb 12, 2022 - 11:00 AM", checkInLogs[0].startTime)
        assertEquals("Feb 12, 2022 - 11:00 AM", checkInLogs[0].endTime)
        assertEquals("This is a description of log", checkInLogs[0].log)
    }

    @Test(expected = NoDataException::class)
    fun throwNoDataExceptionWhenThereIsNoCheckInLogs() = runBlocking {
        securityLogsRepository.getCheckInLogs()

        Unit
    }

    @Test
    fun getPatrolRoutePlan() = runBlocking {
        routePlansDao.saveRoutePlan(
            RoutePlanDB(
                2,
                false,
                false,
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:00 AM",
                123
            )
        )
        patrolLocationsDao.savePatrolLocations(
            listOf(
                PatrolLocationDB(
                    0,
                    2,
                    3,
                    "Phelan Building",
                    "",
                    "",
                    false,
                    false,
                    ".",
                    ""
                )
            )
        )

        val routePlan = securityLogsRepository.getPatrolRoutePlan(123)

        assertEquals(2, routePlan.id)
        assertEquals(1, routePlan.locations.size)
        assertEquals(3, routePlan.locations[0].areaLocationId)
        assertEquals("Phelan Building", routePlan.locations[0].name)
    }

    @Test(expected = NoDataException::class)
    fun throwNoDataExceptionWhenNoActiveRoutePlan() = runBlocking {

        securityLogsRepository.getPatrolRoutePlan(123)

        Unit
    }

    @Test
    fun getLocations() = runBlocking {

        val locations = securityLogsRepository.getLocations()

        assertEquals(8, locations.size)
        assertEquals("Phelan Building", locations[0].name)
        assertEquals(".", locations[0].sorting)
    }

    @Test
    fun saveRoutePlanArrangeRouteLocation() = runBlocking {
        routePlansDao.saveRoutePlan(
            RoutePlanDB(
                2,
                false,
                false,
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:00 AM",
                123
            )
        )
        patrolLocationsDao.savePatrolLocations(
            listOf(
                PatrolLocationDB(
                    4,
                    2,
                    3,
                    "Phelan Building",
                    "",
                    "",
                    false,
                    false,
                    ".",
                    ""
                ),
                PatrolLocationDB(
                    5,
                    2,
                    2,
                    "Xavier Hall",
                    "",
                    "",
                    false,
                    false,
                    "..",
                    ""
                )
            )
        )

        securityLogsRepository.saveRoutePlan(
            RoutePlanRequests.ArrangeRouteLocation(
                5, ".", 4, ".."
            )
        )

        val routePlan = routePlansDao.getRoutePlanWithPatrolLocations(123)

        assertEquals(".",
            routePlan?.patrolLocations?.find { it.id == 5 }?.sorting)
        assertEquals("..",
            routePlan?.patrolLocations?.find { it.id == 4 }?.sorting)
    }

    @Test
    fun saveRoutePlanCreate() = runBlocking {
        val areaLocations = areaLocationsDao.getAllAreaLocations()

        securityLogsRepository.saveRoutePlan(
            RoutePlanRequests.Create(
                234,
                areaLocations.map {
                    PatrolLocation(
                        0,
                        it.id,
                        it.sorting,
                        it.name,
                        "",
                        "",
                        false,
                        false,
                        null
                    )
                }
            )
        )

        val routePlan = routePlansDao.getRoutePlanWithPatrolLocations(234)
        assertEquals(8, routePlan?.patrolLocations?.size)
        assert(routePlan?.patrolLocations?.find {
            it.name == "Xavier Hall"
        } != null)
    }

    @Test
    fun saveRoutePlanStart() = runBlocking {
        routePlansDao.saveRoutePlan(
            RoutePlanDB(
                2,
                false,
                false,
                "",
                "",
                123
            )
        )

        securityLogsRepository.saveRoutePlan(
            RoutePlanRequests.Start(
                2,
                "Feb 12, 2022 - 11:00 AM"
            )
        )

        val routePlan = routePlansDao.getRoutePlanByUserId(123)

        assertEquals("Feb 12, 2022 - 11:00 AM", routePlan?.startTime)
        assertEquals(false, routePlan?.isDone)
        assertEquals(true, routePlan?.hasStarted)
    }

    @Test(expected = NoDataException::class)
    fun throwNoDataExceptionWhenRoutePlanIsNotExistingOnStart() = runBlocking {
        securityLogsRepository.saveRoutePlan(
            RoutePlanRequests.Start(123, "Feb 12, 2022 - 11:00 AM")
        )

        Unit
    }

    @Test
    fun saveRoutePlanEnd()  = runBlocking {
        routePlansDao.saveRoutePlan(
            RoutePlanDB(
                2,
                true,
                false,
                "Feb 12, 2022 - 11:00 AM",
                "",
                123
            )
        )

        securityLogsRepository.saveRoutePlan(
            RoutePlanRequests.End(
                2,
                "Feb 12, 2022 - 11:05 AM"
            )
        )

        val routePlan = routePlansDao.getRoutePlanById(2)

        assertEquals("Feb 12, 2022 - 11:05 AM", routePlan?.endTime)
        assertEquals(true, routePlan?.isDone)
        assertEquals(true, routePlan?.hasStarted)
    }

    @Test(expected = NoDataException::class)
    fun throwNoDataExceptionWhenRoutePlanIsNotExistingOnEnd() = runBlocking {
        securityLogsRepository.saveRoutePlan(
            RoutePlanRequests.End(123, "Feb 12, 2022 - 11:00 AM")
        )

        Unit
    }

    @Test
    fun saveRoutePlanReset() = runBlocking {
        routePlansDao.saveRoutePlan(
            RoutePlanDB(
                2,
                false,
                false,
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:00 AM",
                123
            )
        )
        patrolLocationsDao.savePatrolLocations(
            listOf(
                PatrolLocationDB(
                    4,
                    2,
                    3,
                    "Phelan Building",
                    "",
                    "",
                    false,
                    false,
                    "..",
                    ""
                ),
                PatrolLocationDB(
                    5,
                    2,
                    2,
                    "Xavier Hall",
                    "",
                    "",
                    false,
                    false,
                    ".",
                    ""
                )
            )
        )

        securityLogsRepository.saveRoutePlan(
            RoutePlanRequests.Reset(
                2,
                listOf(
                    PatrolLocation(
                        4,
                        3,
                        ".",
                        "Phelan Building",
                        "",
                        "",
                        false,
                        false,
                        ""
                    ),
                    PatrolLocation(
                        5,
                        2,
                        "..",
                        "Xavier Hall",
                        "",
                        "",
                        false,
                        false,
                        ""
                    )
                )
            )
        )

        val routePlan = routePlansDao.getRoutePlanWithPatrolLocations(123)
        assertEquals(".", routePlan?.patrolLocations?.find { it.id == 4 }?.sorting)
        assertEquals("..", routePlan?.patrolLocations?.find { it.id == 5 }?.sorting)
    }

    @Test
    fun saveRoutePlanSaveLog() = runBlocking {
        routePlansDao.saveRoutePlan(
            RoutePlanDB(
                2,
                false,
                false,
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:00 AM",
                123
            )
        )
        patrolLocationsDao.savePatrolLocations(
            listOf(
                PatrolLocationDB(
                    4,
                    2,
                    3,
                    "Phelan Building",
                    "",
                    "",
                    false,
                    false,
                    "..",
                    ""
                ),
                PatrolLocationDB(
                    5,
                    2,
                    2,
                    "Xavier Hall",
                    "",
                    "",
                    false,
                    false,
                    ".",
                    ""
                )
            )
        )

        securityLogsRepository.saveRoutePlan(
            RoutePlanRequests.SaveLog(
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:09 AM",
                4,
                true,
                "Only cats here"
            )
        )

        val patrolLocation = patrolLocationsDao.getPatrolLocationById(4)

        assertEquals("Feb 12, 2022 - 11:00 AM", patrolLocation?.startTime)
        assertEquals("Feb 12, 2022 - 11:09 AM", patrolLocation?.endTime)
        assertEquals(true, patrolLocation?.isVisited)
        assertEquals(true, patrolLocation?.isCleared)
        assertEquals("Only cats here", patrolLocation?.remarks)
    }

    @Test(expected = NoDataException::class)
    fun throwANoDataExceptionWhenThereIsNoPatrolLocationWithId() = runBlocking {
        securityLogsRepository.saveRoutePlan(
            RoutePlanRequests.SaveLog(
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:00 AM",
                1,
                false,
                ""
            )
        )

        Unit
    }

    @Test
    fun saveCheckInLog() = runBlocking {

        securityLogsRepository.saveCheckInLog(
            CheckInRequest(
                123,
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:00 AM",
                "This is the description"
            )
        )

        val checkInLogDB = checkInLogsDao.getAllCheckInLogs()[0].checkInLog

        assertEquals(123, checkInLogDB.userId)
        assertEquals("Feb 12, 2022 - 11:00 AM", checkInLogDB.startTime)
        assertEquals("Feb 12, 2022 - 11:00 AM", checkInLogDB.endTime)
        assertEquals("This is the description", checkInLogDB.description)
    }
}