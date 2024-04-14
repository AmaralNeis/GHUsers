package com.example.ghusers.repository.api

import com.example.ghusers.model.Repository
import com.example.ghusers.model.UserDetail
import com.example.ghusers.model.UserItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GHApi {
    @GET("users")
    suspend fun getUsers(): Response<List<UserItem>>

    @GET("users/{user}")
    suspend fun  getUserWith(@Path("user") name: String): Response<UserDetail?>

    @GET("users/{user}/repos")
    suspend fun  getUserRepos(@Path("user") name: String): Response<List<Repository>?>
}