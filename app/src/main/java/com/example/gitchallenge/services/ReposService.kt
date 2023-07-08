package com.example.gitchallenge.services

import com.example.gitchallenge.api.ApiModule
import com.example.gitchallenge.api.BaseApiCall
import com.example.gitchallenge.api.error.ApiException
import com.example.gitchallenge.api.error.NetworkException
import com.example.gitchallenge.api.utils.CallResult
import com.example.gitchallenge.models.Readme
import com.example.gitchallenge.models.Repo

private const val ERROR_API = "API Error"
private const val ERROR_NETWORK = "NETWORK Error"

class ReposService : BaseApiCall() {
    companion object {
        private var instance: ReposService? = null

        fun getInstance(): ReposService {
            if (instance == null) {
                instance = ReposService()
            }

            return instance!!
        }
    }

    private var api = ApiModule().reposApi

    suspend fun getRepos(page: Int): List<Repo> {
        when (val response = safeApiCall { api.getRepos(page) }) {
            is CallResult.Success -> {
                if (response.value.items != null) {
                    return response.value.items
                } else {
                    throw ApiException(ERROR_API)
                }
            }
            is CallResult.Failure -> {
                if (response.networkError) {
                    throw NetworkException(ERROR_NETWORK)
                } else {
                    throw ApiException(response.errorBody.toString())
                }
            }
        }
    }

    suspend fun getRepoDetails(ownerName: String, repoName: String): Repo {
        when (val response = safeApiCall { api.getRepo(ownerName, repoName) }) {
            is CallResult.Success -> {
                return response.value
            }
            is CallResult.Failure -> {
                if (response.networkError) {
                    throw NetworkException(ERROR_NETWORK)
                } else {
                    throw ApiException(response.errorBody.toString())
                }
            }
        }
    }

    suspend fun getReadme(ownerName: String, repoName: String): Readme {
        when (val response = safeApiCall { api.getReadme(ownerName, repoName) }) {
            is CallResult.Success -> {
                return response.value
            }
            is CallResult.Failure -> {
                if (response.networkError) {
                    throw NetworkException(ERROR_NETWORK)
                } else {
                    throw ApiException(response.errorBody.toString())
                }
            }
        }
    }
}