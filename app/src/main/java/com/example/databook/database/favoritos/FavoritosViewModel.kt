package com.example.databook.database.favoritos

import android.app.Application
import androidx.lifecycle.*
import com.example.databook.database.AppDataBase
import com.example.databook.database.FavoritosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritosViewModel(app: Application): AndroidViewModel(app) {

    val favList: LiveData<List<FavoritosEntity>>
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

//    fun deleteAllFav() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteAllFavs()
//        }
//    }

    fun getseacrhListFav(search:String): LiveData<List<FavoritosEntity>>{
        return repository.getListFavSearch(search)
    }

    fun getListFavUserId(userID: String):LiveData<List<FavoritosEntity>> {
        return repository.getListFavUser(userID)
    }

}







