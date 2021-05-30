package com.example.isbm.Entities

import java.io.Serializable

data class ReadingModes(
    val image: Boolean = false,
    val text: Boolean = false
): Serializable