package com.example.databook.entities



import java.io.Serializable
//Classe usada para criar objetos com as informações que retornam da API.
data class Books(
    val items: List<Item> = arrayListOf(),
    val kind: String = "",
    val totalItems: Int = 0
):Serializable