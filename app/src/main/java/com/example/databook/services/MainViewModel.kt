package com.example.databook.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.databook.entities.Books
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

