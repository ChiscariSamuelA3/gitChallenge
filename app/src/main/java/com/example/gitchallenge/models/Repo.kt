package com.example.gitchallenge.models

import com.google.gson.annotations.SerializedName

data class Repo(
    val id: Int,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val owner: Owner,
    val description: String,
    val url: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    val forks: Int,
    val watchers: Int,
)

data class Owner(
    val login: String,
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String
)