package com.example.databook.entities

import java.io.Serializable

data class ReadingModes(
    val image: Boolean = false,
    val text: Boolean = false
): Serializable