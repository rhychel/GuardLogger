package com.rhymartmanchus.guardlogger.domain.requests

data class CheckInRequest (
    val userId: Int,
    val startTime: String,
    val endTime: String,
    val logs: String
)