package com.example.databook.dataBase.Perfil


import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface PerfilDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInPerfisList(user: PerfilEntity)

    @Update
    suspend fun updatePerfil(fav: PerfilEntity)

    @Delete
    suspend fun removeOfPerfilList(fav: PerfilEntity)

    @Query("DELETE FROM perfiltable")
    suspend fun deletAllPerfil()

    @Query("SELECT * FROM perfiltable")
    fun getPerfilList(): LiveData<List<PerfilEntity>>


}







