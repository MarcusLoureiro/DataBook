package com.example.dataBase

import androidx.lifecycle.LiveData
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.example.filmapp.Media.dataBase.FavoritosDAO


class FavoritosRepository(private val favoritosDAO: FavoritosDAO){

    val readAllData: LiveData<List<FavoritosEntity>> = favoritosDAO.getFavoritosList()
//    val readAllDataMenorIdade: LiveData<List<FavoritosEntity>> = favoritosDAO.getUsersMenorIdade()
//    val readAllDataMaiorIdade: LiveData<List<FavoritosEntity>> = favoritosDAO.getUsersMaiorIdade()


    suspend fun addFav(fav: FavoritosEntity){
        favoritosDAO.saveInUsersList(fav)
    }

    suspend fun deleteFav(fav: FavoritosEntity){
        favoritosDAO.removeOfFavsList(fav)
    }

    suspend fun deleteAllFavs(){
        favoritosDAO.deletAllFavs()
    }

    suspend fun updateFav(fav: FavoritosEntity){
        favoritosDAO.updateFavs(fav)
    }



}