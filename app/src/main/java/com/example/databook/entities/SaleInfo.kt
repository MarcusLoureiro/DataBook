package com.example.databook.entities


import java.io.Serializable
//Classe usada para criar objetos com as informações que retornam da API.
data class SaleInfo(
    val buyLink: String = "",
    val country: String = "",
    val isEbook: Boolean = false,
    val saleability: String = ""
): Serializable