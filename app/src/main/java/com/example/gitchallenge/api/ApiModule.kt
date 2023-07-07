package com.example.gitchallenge.api

import android.content.Context
import com.example.gitchallenge.BuildConfig
import com.example.gitchallenge.api.endpoints.ReposApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiModule(context: Context) {
    private val client: OkHttpClient
        get() = OkHttpClient.Builder().also { client ->
            client.addInterceptor(Interceptor { chain ->
                val request =
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${BuildConfig.TOKEN}")
                        .build()
                chain.proceed(request)
            })

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }
        }.build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/search/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val reposApi: ReposApi
        get() = retrofit.create(ReposApi::class.java)
}