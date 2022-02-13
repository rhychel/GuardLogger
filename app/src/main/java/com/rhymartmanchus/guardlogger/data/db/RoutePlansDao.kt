package com.rhymartmanchus.guardlogger.data.db

import androidx.room.*
import com.rhymartmanchus.guardlogger.data.db.models.RoutePlanDB
import com.rhymartmanchus.guardlogger.data.db.models.RoutePlanWithPatrolLocationsDB

@Dao
interface RoutePlansDao {

    @Transaction
    @Query("SELECT * FROM route_plans WHERE userId = :userId AND isDone = 0")
    suspend fun getRoutePlanWithPatrolLocations(userId: Int): RoutePlanWithPatrolLocationsDB?

    @Query("SELECT * FROM route_plans WHERE userId = :id AND isDone = 0")
    suspend fun getRoutePlanByUserId(id: Int): RoutePlanDB?

    @Query("SELECT * FROM route_plans WHERE id = :id")
    suspend fun getRoutePlanById(id: Int): RoutePlanDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRoutePlan(routePlanDB: RoutePlanDB)

}