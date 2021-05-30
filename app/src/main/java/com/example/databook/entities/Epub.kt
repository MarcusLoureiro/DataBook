package com.example.isbm.Entities

import java.io.Serializable

data class Epub(
    val downloadLink: String = "",
    val isAvailable: Boolean = false
): Serializable