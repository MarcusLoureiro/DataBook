package com.example.isbm.Entities

import java.io.Serializable

data class Books(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
):Serializable