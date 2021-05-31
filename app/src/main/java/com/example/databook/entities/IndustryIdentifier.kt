package com.example.databook.entities

import java.io.Serializable

data class IndustryIdentifier(
    val identifier: String = "",
    val type: String = ""
): Serializable