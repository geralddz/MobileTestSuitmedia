package com.app.mobiletestsuitmedia.data.retrofit

import com.app.mobiletestsuitmedia.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") size: Int
    ): UserResponse
}