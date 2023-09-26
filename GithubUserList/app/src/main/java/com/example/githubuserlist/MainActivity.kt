package com.example.githubuserlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserlist.data.response.GithubUserResponse
import com.example.githubuserlist.data.response.ItemsItem
import com.example.githubuserlist.data.retrofit.ApiConfig
import com.example.githubuserlist.databinding.ActivityMainBinding
import com.example.githubuserlist.ui.GithubUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
        private const val USER_ID = "ReyhanHerdi"
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

    }

    private fun findUser() {
        val client = ApiConfig.getApiService().getGithubUser(USER_ID)
        Log.d("GG", "Ada di dini")
        client.enqueue(object : Callback<GithubUserResponse> {
            override fun onResponse(
                call: Call<GithubUserResponse>,
                response: Response<GithubUserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("TR", "Di sini")
                    if (responseBody != null) {
                        setUserData(responseBody.items)
                        Log.d("IF", "Sukses")
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                } else {
                    Log.e("LL", "Gagal")
                }
            }

            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure = ${t.message}")
            }

        })
    }

    private fun setUserData(userList: List<ItemsItem>) {
        val adapter = GithubUserAdapter()
        adapter.submitList(userList)
        Log.d("ADAPTER", "Di sini")
        binding.userList.adapter = adapter
    }
}