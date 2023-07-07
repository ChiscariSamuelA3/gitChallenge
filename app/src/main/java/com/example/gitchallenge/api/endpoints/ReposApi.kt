package com.example.gitchallenge.api.endpoints

import com.example.gitchallenge.api.ApiResponse
import com.example.gitchallenge.models.Readme
import com.example.gitchallenge.models.Repo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReposApi {
    @GET("search/repositories?q=topic:android&sort=stars&order=desc")
    suspend fun getRepos(
        @Query("page") page: Int
    ): ApiResponse<List<Repo>>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Repo

    @GET("repos/{owner}/{repo}/readme")
    suspend fun getReadme(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Readme
}