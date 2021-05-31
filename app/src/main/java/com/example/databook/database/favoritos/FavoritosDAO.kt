package com.example.databook.database.favoritos

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface FavoritosDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInFavsList(fav: FavoritosEntity)

    @Update
    suspend fun updateFavs(fav: FavoritosEntity)

    @Delete
    suspend fun removeOfFavsList(fav: FavoritosEntity)

    @Query("DELETE FROM favoritostable")
    suspend fun deletAllFavs()

    @Query("SELECT * FROM favoritostable")
    fun getFavoritosList(): LiveData<List<FavoritosEntity>>

    @Query("SELECT * FROM favoritostable WHERE userID =:userID")
    fun getListFavUserId(userID: String): LiveData<List<FavoritosEntity>>

    @Query("SELECT * FROM favoritostable WHERE title LIKE :search")
    fun getListFavSearch(search: String): LiveData<List<FavoritosEntity>>

}







