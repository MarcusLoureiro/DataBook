package com.example.databook.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.databook.entities.Books
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getSearch(string: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getSearch(string)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Ocorreu um Erro!"))
        }
    }
}

