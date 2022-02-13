package com.rhymartmanchus.guardlogger.data.db.models

import androidx.room.Embedded
import androidx.room.Relation

data class RoutePlanWithPatrolLocationsDB (
    @Embedded
    val routePlan: RoutePlanDB,
    @Relation(
        entity = PatrolLocationDB::class,
        parentColumn = "id",
        entityColumn = "routeId"
    )
    val patrolLocations: List<PatrolLocationDB>
)