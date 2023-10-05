package com.example.githubuserlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserlist.database.FavoriteUser
import com.example.githubuserlist.databinding.UserListBinding
import com.example.githubuserlist.helper.FavUserDiffCallback

class UsersFavoritedAdapter : RecyclerView.Adapter<UsersFavoritedAdapter.UsersFavoritedViewHolder>() {

    private val listUsersFavorited = ArrayList<FavoriteUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setListUsersFavorited(listUsersFavorited: List<FavoriteUser>) {
        val diffCallback = FavUserDiffCallback(this.listUsersFavorited, listUsersFavorited)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUsersFavorited.clear()
        this.listUsersFavorited.addAll(listUsersFavorited)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersFavoritedViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersFavoritedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listUsersFavorited.size
    }

    override fun onBindViewHolder(holder: UsersFavoritedViewHolder, position: Int) {
        holder.bind(listUsersFavorited[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUsersFavorited[position])
        }
    }
    inner class UsersFavoritedViewHolder(private val binding: UserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser) {
            with(binding) {
                userNameOnList.text = favoriteUser.username
                Glide.with(itemView.context)
                    .load(favoriteUser.avatarUrl)
                    .into(userAvatarOnList)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(favoriteUser: FavoriteUser)
    }
}