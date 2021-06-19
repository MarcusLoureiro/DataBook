package com.example.databook.database.perfil

import android.app.Application
import androidx.lifecycle.*
import com.example.databook.database.AppDataBase
import com.example.databook.database.PerfilRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PerfisViewModel(app: Application): AndroidViewModel(app) {

    /**ViewModel usado para ter acesso aos dados do banco, especificamente da tabela de perfis*/
    val perfilList: LiveData<List<PerfilEntity>>
    private val repository: PerfilRepository


    init {
        val perfilDAO = AppDataBase.getDataBase(app).perfisDAO()
        repository = PerfilRepository(perfilDAO)
        perfilList = repository.readAllData
    }

    //Adicionando novo perfil
    fun addPerfil(perfil: PerfilEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPerfil(perfil)
        }
    }

    //Editando novo perfil
    fun updatePerfil(perfil: PerfilEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePerfil(perfil)
        }
    }

//    fun deletePerfil(perfil: PerfilEntity){
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deletePerfil(perfil)
//        }
//    }

//    fun deleteAllFav() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteAllPerfis()
//        }
//    }

}







