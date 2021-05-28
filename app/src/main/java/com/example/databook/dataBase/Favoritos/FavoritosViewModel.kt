package com.example.databook.dataBase.Favoritos

import android.app.Application
import androidx.lifecycle.*
import com.example.dataBase.AppDataBase
import com.example.dataBase.FavoritosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritosViewModel(app: Application): AndroidViewModel(app) {

    val favList: LiveData<List<FavoritosEntity>>
//    val favListMaior: LiveData<List<FavoritosEntity>>
//    val favListMenor: LiveData<List<FavoritosEntity>>
    private val repository: FavoritosRepository

    init {
        val favsDAO = AppDataBase.getDataBase(app).favoritosDAO()
        repository = FavoritosRepository(favsDAO)
        favList = repository.readAllData
    }

    fun addFav(fav: FavoritosEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFav(fav)
        }
    }

    fun updateFav(fav: FavoritosEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFav(fav)
        }
    }

    fun deleteFav(fav: FavoritosEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFav(fav)
        }
    }

    fun deleteAllFav() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllFavs()
        }
    }

}







