package com.example.dataBase

import android.app.Application
import androidx.lifecycle.*
import com.example.filmapp.Media.dataBase.FavoritosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritosViewModel(app: Application): AndroidViewModel(app) {

    val favList: LiveData<List<FavoritosEntity>>
//    val favListMaior: LiveData<List<FavoritosEntity>>
//    val favListMenor: LiveData<List<FavoritosEntity>>
    private val repository: UsersRepository

    init {
        val favsDAO = AppDataBase.getDataBase(app).favoritosDAO()
        repository = UsersRepository(favsDAO)
        favList = repository.readAllData
//        favListMaior = repository.readAllDataMaiorIdade
//        favListMenor = repository.readAllDataMenorIdade
    }

    fun saveNewMedia(fav: FavoritosEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveInUsersListTask(fav)
        }
    }


    fun removeMedia(fav: FavoritosEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeOfUsersListTask(fav)
        }
    }

    fun checkFavoritoInList(
        book: FavoritosEntity,
        listDataBase: List<FavoritosEntity>
    ): FavoritosEntity {
        listDataBase?.forEach {
            if (book.id == it.id)
                book.favoritoIndication = true
        }
        return book
    }

}







