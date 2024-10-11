package com.example.socialappbynavgraph.apiService

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://gorest.co.in/public/v2/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface ApiService {

    @Headers("Authorization:Bearer b9358ad700e480f8924d965da24579c89b8ea0524e669737b210ceaaa09b1a00")
    @GET("users")
    suspend fun getUsersDetails(): List<Users>

    @Headers("Authorization:Bearer b9358ad700e480f8924d965da24579c89b8ea0524e669737b210ceaaa09b1a00")
    @DELETE("users/{userId}")
    suspend fun deleteProfile(@Path("userId") userId: Int): Response<Unit>

    //@Path is a path to show which take string which is equal to parameter passed to in @DELETE
    // userId: Int is a variable with type Int
    @Headers("Authorization:Bearer b9358ad700e480f8924d965da24579c89b8ea0524e669737b210ceaaa09b1a00")
    @POST("users")
    suspend fun postProfile(@Body post: Users): Response<Users>

    // PUT is used to update data on server
    @Headers("Authorization:Bearer b9358ad700e480f8924d965da24579c89b8ea0524e669737b210ceaaa09b1a00")
    @PUT("users/{userId}")
    suspend fun putProfile(@Path("userId") userId: Int, @Body post: Users): Response<Users>
}

object Api {
    fun getRetrofitInstance(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}