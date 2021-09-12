package com.mobline.domain.exceptions

fun interface ErrorConverter {
    fun convert(t: Throwable): Throwable
}