package com.example.githubuserlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserlist.database.FavoriteUser
import com.example.githubuserlist.databinding.ActivityUsersFavoritedBinding
import com.example.githubuserlist.helper.SettingPreferences
import com.example.githubuserlist.helper.ViewModelFactory
import com.example.githubuserlist.helper.dataStore
import com.example.githubuserlist.model.DetailUserViewModel
import com.example.githubuserlist.ui.adapter.UsersFavoritedAdapter

class UsersFavoritedActivity : AppCompatActivity() {

    private var _activityUsersFavoritedBinding: ActivityUsersFavoritedBinding? = null
    private val binding get() = _activityUsersFavoritedBinding

    private lateinit var adapter: UsersFavoritedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityUsersFavoritedBinding = ActivityUsersFavoritedBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val detailUserViewModel = obtainViewModel(this@UsersFavoritedActivity)
        detailUserViewModel.getAllFavUser().observe(this) {usersFavoriteList ->
            if (usersFavoriteList != null) {
                adapter.setListUsersFavorited(usersFavoriteList)
            }
        }

        showFavoritedUser()
        val layoutManager = LinearLayoutManager(this)
        binding?.usersFavorited?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.usersFavorited?.addItemDecoration(itemDecoration)

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    private fun showClickedUser(favoriteUser: FavoriteUser) {
        val intent = Intent(this@UsersFavoritedActivity, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.USERNAME, favoriteUser.username)
        intent.putExtra(UserDetailActivity.AVATAR_URL, favoriteUser.avatarUrl)
        startActivity(intent)
    }

    private fun showFavoritedUser() {
        adapter = UsersFavoritedAdapter()
        binding?.usersFavorited?.adapter = adapter

        adapter.setOnItemCallback(object : UsersFavoritedAdapter.OnItemClickCallback {
            override fun onItemClicked(favoriteUser: FavoriteUser) {
                showClickedUser(favoriteUser)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityUsersFavoritedBinding = null
    }
}