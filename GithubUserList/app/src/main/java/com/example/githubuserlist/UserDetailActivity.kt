package com.example.githubuserlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserlist.data.response.DetailUserResponse
import com.example.githubuserlist.data.retrofit.ApiConfig
import com.example.githubuserlist.databinding.ActivityUserDetailBinding
import com.example.githubuserlist.ui.SectionsPaferAdapter
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        private const val TAG = "UserDetailActivity"
        const val USERNAME = "user_name"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        findDetailUser()

        val sectionsPagerAdapter = SectionsPaferAdapter(this)
        sectionsPagerAdapter.appName = "${intent.getStringExtra(USERNAME)}"
        val view_pager = binding.viewPager
        view_pager.adapter = sectionsPagerAdapter
        val tabs = binding.followTab
        TabLayoutMediator(tabs, view_pager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun findDetailUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUsers("${intent.getStringExtra(USERNAME)}")
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserDetailData(responseBody)
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure1: ${t.message}")
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setUserDetailData(userDetail: DetailUserResponse) {
        binding.userName.text = userDetail.login
        binding.userFullName.text = userDetail.name
        binding.followersCount.text = userDetail.followers.toString()
        binding.followingCount.text = userDetail.following.toString()
        Glide.with(this@UserDetailActivity)
            .load(userDetail.avatarUrl)
            .into(binding.userAvatar)
    }
}