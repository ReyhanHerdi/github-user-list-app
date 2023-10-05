package com.example.githubuserlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserlist.database.FavoriteUser
import com.example.githubuserlist.repository.FavoriteUserRepository

class DetailUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavUser()
}