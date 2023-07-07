package com.example.gitchallenge.api.endpoints

import com.example.gitchallenge.api.ApiResponse
import com.example.gitchallenge.models.Repo
import retrofit2.http.GET
import retrofit2.http.Query

interface ReposApi {
    @GET("repositories?q=topic:android&sort=stars&order=desc")
    suspend fun getRepos(
        @Query("page") page: Int
    ): ApiResponse<List<Repo>>
}