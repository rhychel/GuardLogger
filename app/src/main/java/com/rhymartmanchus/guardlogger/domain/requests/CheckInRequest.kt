package com.rhymartmanchus.guardlogger.domain.requests

data class CheckInRequest (
    val userId: String,
    val startTime: String,
    val endTime: String,
    val logs: String
)