package com.example.databook.entities


import java.io.Serializable

data class SaleInfo(
    val buyLink: String = "",
    val country: String = "",
    val isEbook: Boolean = false,
    val saleability: String = ""
): Serializable