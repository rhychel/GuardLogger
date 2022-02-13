package com.rhymartmanchus.guardlogger.screens.login

sealed interface SignUpState {
    object Success : SignUpState
    object NotRegistered : SignUpState
}