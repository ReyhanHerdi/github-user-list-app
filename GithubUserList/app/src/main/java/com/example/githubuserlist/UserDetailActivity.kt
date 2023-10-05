package com.example.githubuserlist

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserlist.data.response.DetailUserResponse
import com.example.githubuserlist.data.retrofit.ApiConfig
import com.example.githubuserlist.database.FavoriteUser
import com.example.githubuserlist.database.FavoriteUserDao
import com.example.githubuserlist.databinding.ActivityUserDetailBinding
import com.example.githubuserlist.helper.ViewModelFactory
import com.example.githubuserlist.ui.SectionsPaferAdapter
import com.example.githubuserlist.ui.insert.FavUserAddUpdateViewModel
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var favUserAddUpdateViewModel: FavUserAddUpdateViewModel

    companion object {
        private const val TAG = "UserDetailActivity"
        const val USERNAME = "user_name"
        const val AVATAR_URL = "avatar_url"
        const val EXTRA_NOTE = "extra_note"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private var isEdit = false
    private var favoriteUser: FavoriteUser? = null
    private var _activityUserDetailBinding: ActivityUserDetailBinding? = null

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

        favUserAddUpdateViewModel = obtainViewModel(this@UserDetailActivity)

        favoriteUser = intent.getParcelableExtra(EXTRA_NOTE)
        if (favoriteUser != null) {
            isEdit = true
        } else {
            favoriteUser = FavoriteUser()
        }

        val actionBarTitle: String
        val btnTitle: String

        observeData()
        binding.fabFavorite.setOnClickListener(this)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavUserAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavUserAddUpdateViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityUserDetailBinding = null
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

    override fun onClick(view: View?) {
        when(view) {
            binding.fabFavorite -> {
                favoriteUser?.username = "${intent.getStringExtra(USERNAME)}"
                favoriteUser?.avatarUrl = "${intent.getStringExtra(AVATAR_URL)}"

                val observeFavUser = favUserAddUpdateViewModel.getFavoritedUser("${favoriteUser?.username}")
                var isProcessing = true
                observeFavUser.observe(this) { findFavorite ->
                    if (isProcessing) {
                        when {
                            findFavorite.isEmpty() -> {
                                favUserAddUpdateViewModel.insert(favoriteUser as FavoriteUser)
                                Toast.makeText(this, "Add to Favorite", Toast.LENGTH_SHORT).show()
                                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                            } else -> {
                                favUserAddUpdateViewModel.delete(favoriteUser as FavoriteUser)
                                Toast.makeText(this, "Remove from Favorite", Toast.LENGTH_SHORT).show()
                                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                            }
                        }
                        isProcessing = false
                    }
                }
            }
        }
    }

    private fun observeData() {
        favoriteUser?.username = "${intent.getStringExtra(USERNAME)}"
        val observeFavUser = favUserAddUpdateViewModel.getFavoritedUser("${favoriteUser?.username}")
        var isProcessing = true
        observeFavUser.observe(this) { findFavorite ->
            if (isProcessing) {
                when {
                    findFavorite.isEmpty() -> {
                        binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                    } else -> {
                        binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                    }
                }
                isProcessing = false
            }
        }
    }
}