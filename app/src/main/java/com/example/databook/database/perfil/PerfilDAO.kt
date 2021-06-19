package com.example.databook.database.perfil


import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface PerfilDAO {

    //Inserir um perfil na tabela
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInPerfisList(user: PerfilEntity)

    //Atualizar um perfil na tabela
    @Update
    suspend fun updatePerfil(fav: PerfilEntity)

    //Deletar um perfil da tabela
    @Delete
    suspend fun removeOfPerfilList(fav: PerfilEntity)

    //Deletar todos os perfis da tabela
    @Query("DELETE FROM perfiltable")
    suspend fun deletAllPerfil()

    //Pegar a lista de perfis da tabela
    @Query("SELECT * FROM perfiltable")
    fun getPerfilList(): LiveData<List<PerfilEntity>>


}







