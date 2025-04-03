package com.dalmuina.pruebamango.heroes.data.network.retrofit

import com.dalmuina.pruebamango.core.domain.NetworkError
import com.dalmuina.pruebamango.core.domain.Result
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <reified T> safeApiCall(
    crossinline call: suspend () -> Response<T>
): Result<T, NetworkError> {
    return try {
        val response = call()
        responseToResult(response)
    } catch (e: IOException) {
        if (e is SocketTimeoutException) {
            Result.Error(NetworkError.REQUEST_TIMEOUT)
        } else if (e is UnknownHostException) {
            Result.Error(NetworkError.NO_INTERNET)
        } else {
            Result.Error(NetworkError.UNKNOWN)
        }
    } catch (e: Exception) {
        Result.Error(NetworkError.UNKNOWN)
    }
}
