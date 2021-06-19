package com.example.databook.entities


import java.io.Serializable
//Classe usada para criar objetos com as informações que retornam da API.
data class VolumeInfo(
    val allowAnonLogging: Boolean = false,
    val authors: List<String> = arrayListOf(),
    val averageRating: Double = 0.0,
    val canonicalVolumeLink: String = "",
    val categories: List<String> = arrayListOf(),
    val contentVersion: String = "",
    val description: String = "",
    val imageLinks: ImageLinks = ImageLinks(),
    val industryIdentifiers: List<IndustryIdentifier> = arrayListOf(),
    val infoLink: String = "",
    val language: String = "",
    val maturityRating: String = "",
    val pageCount: Int = 0,
    val panelizationSummary: PanelizationSummary = PanelizationSummary(),
    val previewLink: String = "",
    val printType: String = "",
    val publishedDate: String = "",
    val publisher: String = "",
    val ratingsCount: Int = 0,
    val readingModes: ReadingModes = ReadingModes(),
    var title: String = ""
): Serializable