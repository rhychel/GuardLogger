package com.rhymartmanchus.guardlogger.domain.exceptions

class EmployeeNotRegisteredException(
    val throwable: Throwable? = null
) : Exception()