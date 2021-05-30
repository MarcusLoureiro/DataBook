package com.example.databook.dataBase.Favoritos

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


}







//    @Query("SELECT * FROM favoritostable WHERE idade < 18")
//    fun getUsersMenorIdade(): LiveData<List<FavoritosEntity>>
//
//    @Query("SELECT * FROM favoritostable WHERE idade >= 18")
//    fun getUsersMaiorIdade(): LiveData<List<FavoritosEntity>>
