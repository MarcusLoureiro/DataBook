package com.example.dataBase

import androidx.lifecycle.LiveData
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.example.filmapp.Media.dataBase.FavoritosDAO


class UsersRepository(private val favoritosDAO: FavoritosDAO){

    val readAllData: LiveData<List<FavoritosEntity>> = favoritosDAO.getFavoritosList()
//    val readAllDataMenorIdade: LiveData<List<FavoritosEntity>> = favoritosDAO.getUsersMenorIdade()
//    val readAllDataMaiorIdade: LiveData<List<FavoritosEntity>> = favoritosDAO.getUsersMaiorIdade()


    suspend fun saveInUsersListTask(fav: FavoritosEntity){
        favoritosDAO.saveInUsersList(fav)
    }

    suspend fun removeOfUsersListTask(fav: FavoritosEntity){
        favoritosDAO.removeOfUsersList(fav)
    }

}