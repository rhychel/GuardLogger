package com.rhymartmanchus.guardlogger.domain.models

data class RoutePlan (
    val id: Int,
    val hasStarted: Boolean,
    val isDone: Boolean,
    val startTime: String,
    val endTime: String,
    val locations: List<PatrolLocation>
)
