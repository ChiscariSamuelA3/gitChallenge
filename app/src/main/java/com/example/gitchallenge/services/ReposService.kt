package com.example.gitchallenge.services

import com.example.gitchallenge.api.ApiModule
import com.example.gitchallenge.api.BaseApiCall
import com.example.gitchallenge.api.error.ApiException
import com.example.gitchallenge.api.error.NetworkException
import com.example.gitchallenge.api.utils.CallResult
import com.example.gitchallenge.models.Repo

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
                    throw ApiException("API Error")
                }
            }
            is CallResult.Failure -> {
                if (response.networkError) {
                    throw NetworkException("NETWORK Error")
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
                    throw NetworkException("NETWORK Error")
                } else {
                    throw ApiException(response.errorBody.toString())
                }
            }
        }
    }
}