package com.example.githubuserlist.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserlist.database.FavoriteUser
import com.example.githubuserlist.database.FavoriteUserDao
import com.example.githubuserlist.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavUserDao = db.fvUserDao()
    }

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavUserDao.getAllFavUser()

    fun getFavoritedUser(username: String): LiveData<List<FavoriteUser>> = mFavUserDao.getFavoritedUser(username)

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavUserDao.delete(favoriteUser) }
    }

}