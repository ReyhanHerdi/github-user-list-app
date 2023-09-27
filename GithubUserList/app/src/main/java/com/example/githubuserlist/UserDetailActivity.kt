package com.example.githubuserlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.githubuserlist.data.response.DetailUserResponse
import com.example.githubuserlist.data.retrofit.ApiConfig
import com.example.githubuserlist.databinding.ActivityUserDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        private const val TAG = "UserDetailActivity"
        const val USERNAME = "user_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findDetailUser()
        Log.d("USER", "${USERNAME}")
    }

    private fun findDetailUser() {
        val client = ApiConfig.getApiService().getDetailUsers("${intent.getStringExtra(USERNAME)}")
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserDetailData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure1: ${t.message}")
            }

        })
    }

    private fun setUserDetailData(userDetail: DetailUserResponse) {
        binding.userName.text = userDetail.login
        binding.userFullName.text = userDetail.name
        binding.followersCount.text = userDetail.followers.toString()
        binding.followingCount.text = userDetail.following.toString()
        Glide.with(this@UserDetailActivity)
            .load("${userDetail.avatarUrl}")
            .into(binding.userAvatar)

    }
}