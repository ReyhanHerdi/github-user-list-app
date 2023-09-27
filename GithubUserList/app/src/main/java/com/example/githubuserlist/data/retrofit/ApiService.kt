package com.example.githubuserlist.data.retrofit

import com.example.githubuserlist.data.response.DetailUserResponse
import com.example.githubuserlist.data.response.GithubUserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getGithubUser(
        @Query("q") q: String,
    ): Call<GithubUserResponse>
    @GET("users/{username}")
    fun getDetailUsers(
        @Path("username") username: String
    ): Call<DetailUserResponse>
}