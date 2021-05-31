package com.example.dataBase

import androidx.lifecycle.LiveData
import com.example.databook.dataBase.Favoritos.FavoritosEntity
import com.example.databook.dataBase.Favoritos.FavoritosDAO
import com.example.databook.dataBase.Perfil.PerfilDAO
import com.example.databook.dataBase.Perfil.PerfilEntity
import java.util.concurrent.Flow


class FavoritosRepository(private val favoritosDAO: FavoritosDAO){

    val readAllData: LiveData<List<FavoritosEntity>> = favoritosDAO.getFavoritosList()

    suspend fun addFav(fav: FavoritosEntity){
        favoritosDAO.saveInFavsList(fav)
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

    fun getListFavUser(userID: String): LiveData<List<FavoritosEntity>>{
        return favoritosDAO.getListFavUserId(userID)
    }

    fun getListFavSearch(search: String): LiveData<List<FavoritosEntity>> {
        return favoritosDAO.getListFavSearch(search)
    }
}

class PerfilRepository(private val perfisDAO: PerfilDAO){

    val readAllData: LiveData<List<PerfilEntity>> = perfisDAO.getPerfilList()


    suspend fun addPerfil(fav: PerfilEntity){
        perfisDAO.saveInPerfisList(fav)
    }

    suspend fun deletePerfil(fav: PerfilEntity){
        perfisDAO.removeOfPerfilList(fav)
    }

    suspend fun deleteAllPerfis(){
        perfisDAO.deletAllPerfil()
    }

    suspend fun updatePerfil(fav: PerfilEntity){
        perfisDAO.updatePerfil(fav)
    }
}