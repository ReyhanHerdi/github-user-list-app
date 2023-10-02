package com.example.githubuserlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserlist.data.response.DetailFollowersResponseItem
import com.example.githubuserlist.data.response.DetailFollowingResponseItem
import com.example.githubuserlist.data.retrofit.ApiConfig
import com.example.githubuserlist.databinding.FragmentFollowersBinding
import com.example.githubuserlist.ui.FollowersViewAdapter
import com.example.githubuserlist.ui.FollowingViewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        val layoutManager = LinearLayoutManager(context)
        binding.followersList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.followersList.addItemDecoration(itemDecoration)

        if (index == 1) {
            findFollowers()
        } else {
            findFollowing()
        }
    }

    private fun findFollowers() {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowersUsers("${arguments?.getString(ARG_NAME, " Tes ")}")
        client.enqueue(object : Callback<List<DetailFollowersResponseItem>> {

            override fun onResponse(
                call: Call<List<DetailFollowersResponseItem>>,
                response: Response<List<DetailFollowersResponseItem>>
            ) {
                if (response.isSuccessful) {
                    showLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setFollowersData(responseBody)
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<DetailFollowersResponseItem>>, t: Throwable) {
                Log.e(TAG, "onFailure = ${t.message}")
            }
        })
    }

    private fun findFollowing() {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowingUsers("${arguments?.getString(ARG_NAME, " Tes ")}")
        client.enqueue(object : Callback<List<DetailFollowingResponseItem>> {

            override fun onResponse(
                call: Call<List<DetailFollowingResponseItem>>,
                response: Response<List<DetailFollowingResponseItem>>
            ) {
                if (response.isSuccessful) {
                    showLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setFollowingData(responseBody)
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<DetailFollowingResponseItem>>, t: Throwable) {
                Log.e(TAG, "onFailure = ${t.message}")
            }
        })
    }

    private fun setFollowersData(followersList: List<DetailFollowersResponseItem>) {
        val adapter = FollowersViewAdapter()
        adapter.submitList(followersList)
        binding.followersList.adapter = adapter
    }

    private fun setFollowingData(followingList: List<DetailFollowingResponseItem>) {
        val adapter = FollowingViewAdapter()
        adapter.submitList(followingList)
        binding.followersList.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_NAME = "arg_name"
        const val TAG = "FollowersFragment"
    }
}