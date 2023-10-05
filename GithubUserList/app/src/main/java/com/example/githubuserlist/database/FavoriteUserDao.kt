package com.example.githubuserlist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favUser: FavoriteUser)

    @Update
    fun update(favUser: FavoriteUser)

    @Delete
    fun delete(favUser: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser ORDER BY username ASC")
    fun getAllFavUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoritedUser(username: String): LiveData<List<FavoriteUser>>
}