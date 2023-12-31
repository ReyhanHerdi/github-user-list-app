package com.example.githubuserlist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserlist.data.response.ItemsItem
import com.example.githubuserlist.databinding.UserListBinding

class GithubUserAdapter : ListAdapter<ItemsItem, GithubUserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    class MyViewHolder(private val binding: UserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userList: ItemsItem) {
            Glide.with(itemView.context)
                .load(userList.avatarUrl)
                .into(binding.userAvatarOnList)
            binding.userNameOnList.text = userList.login
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userList = getItem(position)
        holder.bind(userList)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(userList)
        }
    }


}