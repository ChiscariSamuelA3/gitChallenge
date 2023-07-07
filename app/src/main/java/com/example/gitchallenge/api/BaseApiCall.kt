package com.example.gitchallenge.api

import com.example.gitchallenge.api.utils.CallResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): CallResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                CallResult.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        CallResult.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody()
                        )
                    }
                    else -> {
                        CallResult.Failure(true, null, null)
                    }
                }
            }
        }
    }
}