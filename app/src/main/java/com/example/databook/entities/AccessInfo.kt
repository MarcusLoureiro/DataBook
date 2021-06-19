package com.example.databook.entities


import java.io.Serializable
//Classe usada para criar objetos com as informações que retornam da API.
data class AccessInfo(
    val accessViewStatus: String = "",
    val country: String = "",
    val embeddable: Boolean = false,
    val epub: Epub = Epub(),
    val pdf: Pdf = Pdf(),
    val publicDomain: Boolean = false,
    val quoteSharingAllowed: Boolean = false,
    val textToSpeechPermission: String = "",
    val viewability: String = "",
    val webReaderLink: String = ""
): Serializable