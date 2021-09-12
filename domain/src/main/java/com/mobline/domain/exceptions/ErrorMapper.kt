package com.mobline.domain.exceptions

fun interface ErrorMapper {
    fun mapError(e: Throwable): Throwable
}