package com.example.githubuserlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserlist.data.response.GithubUserResponse
import com.example.githubuserlist.data.response.ItemsItem
import com.example.githubuserlist.data.retrofit.ApiConfig
import com.example.githubuserlist.databinding.ActivityMainBinding
import com.example.githubuserlist.helper.SettingPreferences
import com.example.githubuserlist.helper.ViewModelFactory
import com.example.githubuserlist.helper.dataStore
import com.example.githubuserlist.model.MainViewModel
import com.example.githubuserlist.ui.adapter.GithubUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var darkMode: Boolean = true

    companion object {
        private const val TAG = "MainActivity"
        private var USER_ID = "Github"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.userList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.userList.addItemDecoration(itemDecoration)

        findUser()
        searchUserData()

        binding.fabFavList.setOnClickListener(this)
        theme()
    }

    private fun findUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getGithubUser(USER_ID)
        client.enqueue(object : Callback<GithubUserResponse> {
            override fun onResponse(
                call: Call<GithubUserResponse>,
                response: Response<GithubUserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        showLoading(false)
                        setUserData(responseBody.items)
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

            }

            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure = ${t.message}")
            }
        })
    }

    private fun setUserData(userList: List<ItemsItem>) {
        val adapter = GithubUserAdapter()
        adapter.submitList(userList)
        binding.userList.adapter = adapter

        adapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showClickedUser(data)
            }

        })
    }

    private fun searchUserData() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    USER_ID = "${searchView.text}"
                    findUser()
                    false
                }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showClickedUser(userList: ItemsItem) {
        val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.USERNAME, userList.login)
        intent.putExtra(UserDetailActivity.AVATAR_URL, userList.avatarUrl)
        startActivity(intent)
    }

    private fun theme() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(application, pref))[MainViewModel::class.java]

        mainViewModel.getThemeSetings().observe(this) {isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        binding.fabDarkMode.setOnClickListener {
            darkMode = !darkMode
            mainViewModel.saveThemeSetting(darkMode)
        }
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.fabFavList -> {
                val intent = Intent(this@MainActivity, UsersFavoritedActivity::class.java)
                startActivity(intent)
            }
        }
    }
}