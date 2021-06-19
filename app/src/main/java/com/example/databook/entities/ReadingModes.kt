package com.example.databook.entities

import java.io.Serializable
//Classe usada para criar objetos com as informações que retornam da API.
data class ReadingModes(
    val image: Boolean = false,
    val text: Boolean = false
): Serializable