package com.example.githubuserlist.data.retrofit

import com.example.githubuserlist.data.response.DetailFollowersResponseItem
import com.example.githubuserlist.data.response.DetailFollowingResponseItem
import com.example.githubuserlist.data.response.DetailUserResponse
import com.example.githubuserlist.data.response.GithubUserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getGithubUser(
        @Query("q") q: String,
    ): Call<GithubUserResponse>
    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getDetailUsers(
        @Path("username") username: String
    ): Call<DetailUserResponse>
    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getFollowersUsers(
        @Path("username") username: String
    ): Call<List<DetailFollowersResponseItem>>
    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getFollowingUsers(
        @Path("username") username: String
    ): Call<List<DetailFollowingResponseItem>>
}