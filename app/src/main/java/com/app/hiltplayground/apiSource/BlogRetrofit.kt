package com.app.hiltplayground.apiSource

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface BlogRetrofit {

    @GET("blogs")
    suspend fun get(): ApiResponse<List<BlogNetworkEntity>>
}