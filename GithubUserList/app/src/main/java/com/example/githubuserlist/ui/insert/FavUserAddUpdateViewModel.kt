package com.example.githubuserlist.ui.insert

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserlist.database.FavoriteUser
import com.example.githubuserlist.repository.FavoriteUserRepository

class FavUserAddUpdateViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun getFavoritedUser(username: String): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getFavoritedUser(username)
}