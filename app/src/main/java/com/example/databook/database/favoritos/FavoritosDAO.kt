package com.example.databook.database.favoritos

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface FavoritosDAO {

    //Inserir um livro na tabela
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInFavsList(fav: FavoritosEntity)

    //Atualizar um livro na tabela
    @Update
    suspend fun updateFavs(fav: FavoritosEntity)

    //Deletar um livro na tabela
    @Delete
    suspend fun removeOfFavsList(fav: FavoritosEntity)

    //Deletar todos os livros da tabela
    @Query("DELETE FROM favoritostable")
    suspend fun deletAllFavs()

    //Pegar todos os livros da Tabela
    @Query("SELECT * FROM favoritostable")
    fun getFavoritosList(): LiveData<List<FavoritosEntity>>

    //Pegar todos os livros da tabela de acordo com um userID
    @Query("SELECT * FROM favoritostable WHERE userID =:userID")
    fun getListFavUserId(userID: String): LiveData<List<FavoritosEntity>>

    //Pegar todos os livros da tabela de acordo com uma pesquisa especificamente igual ao título
    @Query("SELECT * FROM favoritostable WHERE title LIKE :search")
    fun getListFavSearch(search: String): LiveData<List<FavoritosEntity>>

}







