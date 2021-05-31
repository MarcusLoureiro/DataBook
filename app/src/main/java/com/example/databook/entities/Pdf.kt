package com.example.databook.entities

import java.io.Serializable

data class Pdf(
    val downloadLink: String = "",
    val isAvailable: Boolean = false
): Serializable