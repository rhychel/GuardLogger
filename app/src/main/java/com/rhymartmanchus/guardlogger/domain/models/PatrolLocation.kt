package com.rhymartmanchus.guardlogger.domain.models

data class PatrolLocation (
    val id: Int,
    val areaLocationId: Int,
    val sorting: String,
    val name: String,
    val startTime: String,
    val endTime: String,
    val isVisited: Boolean,
    val isCleared: Boolean,
    val remarks: String?
) {
    override fun equals(other: Any?): Boolean {
        other as PatrolLocation
        return other.id == id
    }
}