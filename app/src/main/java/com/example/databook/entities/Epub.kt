package com.example.databook.entities

import java.io.Serializable

data class Epub(
    val downloadLink: String = "",
    val isAvailable: Boolean = false
): Serializable