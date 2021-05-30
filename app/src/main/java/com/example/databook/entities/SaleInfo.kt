package com.example.isbm.Entities

import java.io.Serializable

data class SaleInfo(
    val buyLink: String = "",
    val country: String = "",
    val isEbook: Boolean = false,
    val saleability: String = ""
): Serializable