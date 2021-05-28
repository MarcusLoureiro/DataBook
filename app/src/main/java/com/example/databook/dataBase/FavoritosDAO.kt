package com.example.filmapp.Media.dataBase

import android.app.usage.UsageEvents
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface FavoritosDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInUsersList(user: FavoritosEntity)

    @Update
    suspend fun updateFavs(fav: FavoritosEntity)

    @Delete
    suspend fun removeOfFavsList(fav: FavoritosEntity)

    @Query("DELETE FROM favoritostable")
    suspend fun deletAllFavs()

    @Query("SELECT * FROM favoritostable")
    fun getFavoritosList(): LiveData<List<FavoritosEntity>>


}







//    @Query("SELECT * FROM favoritostable WHERE idade < 18")
//    fun getUsersMenorIdade(): LiveData<List<FavoritosEntity>>
//
//    @Query("SELECT * FROM favoritostable WHERE idade >= 18")
//    fun getUsersMaiorIdade(): LiveData<List<FavoritosEntity>>
