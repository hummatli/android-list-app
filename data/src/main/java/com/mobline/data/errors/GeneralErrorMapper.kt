package com.mobline.data.errors

import com.mobline.data.remote.auth.error.ServerProblemDescription
import com.mobline.domain.exceptions.ErrorMapper
import com.mobline.domain.exceptions.NetworkError
import com.mobline.domain.exceptions.ServerError
import com.mobline.domain.exceptions.UnknownError
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class GeneralErrorMapper(private val json: Json) : ErrorMapper {

    override fun mapError(e: Throwable): Throwable = when (e) {
        is HttpException -> mapHttpErrors(e)
        is SocketException,
        is SocketTimeoutException,
        is UnknownHostException,
        -> NetworkError(e)
        else -> UnknownError(e)
    }

    private fun mapHttpErrors(error: HttpException): Throwable {
        val description = try {
            error.response()?.errorBody()?.string()?.let {
                json.decodeFromString<ServerProblemDescription>(it)
            } ?: ServerProblemDescription()
        } catch (ex: Throwable) {
            Timber.e(ex)
            null
        } ?: ServerProblemDescription()

        return when (error.code()) {
            in 500..599 -> {
                ServerError.ServerIsDown(description.error_code, description.error)
            }
            else -> {
                when (description.error_code.toIntOrNull()) {
                    in 1..10 -> {
                        ServerError.KnownError(description.error_code, description.error)
                    }
                    else -> ServerError.Unexpected(description.error_code, description.error)
                }
            }
        }
    }
}