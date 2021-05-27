package com.example.isbm.Services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.example.isbm.Entities.Books
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


val scope = CoroutineScope(Dispatchers.Main)

class MainViewModel(val service: Service) : ViewModel() {
    var returnSearchList = MutableLiveData<Books>()
    fun getSearch(text: String) {
        viewModelScope.launch {
            returnSearchList.value = service.getSearch(text, keyApi)

        }
    }
}

