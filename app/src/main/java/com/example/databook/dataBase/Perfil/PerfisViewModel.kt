package com.example.databook.dataBase.Perfil

import android.app.Application
import androidx.lifecycle.*
import com.example.dataBase.AppDataBase
import com.example.dataBase.FavoritosRepository
import com.example.dataBase.PerfilRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PerfisViewModel(app: Application): AndroidViewModel(app) {

    val perfilList: LiveData<List<PerfilEntity>>
    private val repository: PerfilRepository

    init {
        val perfilDAO = AppDataBase.getDataBase(app).perfisDAO()
        repository = PerfilRepository(perfilDAO)
        perfilList = repository.readAllData
    }

    fun addFav(perfil: PerfilEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPerfil(perfil)
        }
    }

    fun updateFav(perfil: PerfilEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePerfil(perfil)
        }
    }

    fun deleteFav(perfil: PerfilEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePerfil(perfil)
        }
    }

    fun deleteAllFav() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPerfis()
        }
    }

}







