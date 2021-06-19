package com.example.databook.database.favoritos

import android.app.Application
import androidx.lifecycle.*
import com.example.databook.database.AppDataBase
import com.example.databook.database.FavoritosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritosViewModel(app: Application): AndroidViewModel(app) {

    /**ViewModel usado para ter acesso aos dados do banco, especificamente da tabela de favoritos*/
    val favList: LiveData<List<FavoritosEntity>>
    private val repository: FavoritosRepository

    init {
        val favsDAO = AppDataBase.getDataBase(app).favoritosDAO()
        repository = FavoritosRepository(favsDAO)
        favList = repository.readAllData
    }

    //Adicionando novo favorito
    fun addFav(fav: FavoritosEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFav(fav)
        }
    }

    //Editando favorito
    fun updateFav(fav: FavoritosEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFav(fav)
        }
    }

    //Deletando favorito
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

    //retornando lista de favoritos de acordo com pesquisa.
    fun getseacrhListFav(search:String): LiveData<List<FavoritosEntity>>{
        return repository.getListFavSearch(search)
    }

    //retornando lista de favoritos de acordo com userID.
    fun getListFavUserId(userID: String):LiveData<List<FavoritosEntity>> {
        return repository.getListFavUser(userID)
    }

}







