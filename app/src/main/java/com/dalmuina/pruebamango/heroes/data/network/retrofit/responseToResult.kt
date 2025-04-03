package com.dalmuina.pruebamango.heroes.data.network.retrofit

import com.dalmuina.pruebamango.core.domain.NetworkError
import com.dalmuina.pruebamango.core.domain.Result
import retrofit2.Response

inline fun <reified T> responseToResult(
    response: Response<T>
): Result<T, NetworkError> {
    return when (response.code()) {
        in 200..299 -> {
            response.body()?.let { body ->
                Result.Success(body)
            } ?: Result.Error(NetworkError.SERIALIZATION)
        }
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        409 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}
