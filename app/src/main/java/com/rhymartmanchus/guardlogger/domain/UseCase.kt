package com.rhymartmanchus.guardlogger.domain

abstract class UseCase <Params, Response> {

    abstract suspend fun execute(params: Params): Response

}