package com.example.databook.entities


import java.io.Serializable
//Classe usada para criar objetos com as informações que retornam da API.
data class Item(
    val accessInfo: AccessInfo = AccessInfo(),
    val etag: String = "",
    val id: String = "",
    val kind: String = "",
    val saleInfo: SaleInfo = SaleInfo(),
    val searchInfo: SearchInfo = SearchInfo(),
    val selfLink: String = "",
    val volumeInfo: VolumeInfo = VolumeInfo()
): Serializable