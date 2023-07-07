package com.example.gitchallenge.api

data class ApiResponse<T> (
    val items: T?
)