package com.example.isbm.Entities

import java.io.Serializable

data class Books(
    val items: List<Item> = arrayListOf(),
    val kind: String = "",
    val totalItems: Int = 0
):Serializable