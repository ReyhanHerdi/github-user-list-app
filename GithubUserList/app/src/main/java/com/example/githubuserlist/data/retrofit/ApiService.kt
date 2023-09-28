package com.example.githubuserlist.data.retrofit

import com.example.githubuserlist.data.response.DetailFollowersResponse
import com.example.githubuserlist.data.response.DetailFollowersResponseItem
import com.example.githubuserlist.data.response.DetailFollowingResponse
import com.example.githubuserlist.data.response.DetailFollowingResponseItem
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
    @GET("users/{username}/followers")
    fun getFollowersUsers(
        @Path("username") username: String
    ): Call<List<DetailFollowersResponseItem>>
    @GET("users/{username}/following")
    fun getFollowingUsers(
        @Path("username") username: String
    ): Call<List<DetailFollowingResponseItem>>
}