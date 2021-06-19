package com.example.databook.entities

import java.io.Serializable
//Classe usada para criar objetos com as informações que retornam da API.
data class IndustryIdentifier(
    val identifier: String = "",
    val type: String = ""
): Serializable