package com.example.githubuserlist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserlist.data.response.DetailFollowersResponseItem
import com.example.githubuserlist.databinding.UserListBinding

class FollowersViewAdapter : ListAdapter<DetailFollowersResponseItem, FollowersViewAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailFollowersResponseItem>() {
            override fun areItemsTheSame(oldItem: DetailFollowersResponseItem, newItem: DetailFollowersResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DetailFollowersResponseItem, newItem: DetailFollowersResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class MyViewHolder(val binding: UserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(followersList: DetailFollowersResponseItem) {
            Glide.with(itemView.context)
                .load(followersList.avatarUrl)
                .into(binding.userAvatarOnList)
            binding.userNameOnList.text = followersList.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userList = getItem(position)
        holder.bind(userList)
    }
}