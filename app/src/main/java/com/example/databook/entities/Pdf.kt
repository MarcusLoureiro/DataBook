package com.example.isbm.Entities

import java.io.Serializable

data class Pdf(
    val downloadLink: String = "",
    val isAvailable: Boolean = false
): Serializable