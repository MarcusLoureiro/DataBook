package com.example.databook.dataBase.Favoritos

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favoritostable")
data class FavoritosEntity (
    @PrimaryKey(autoGenerate = false)
    var id:String = "",
    var userID: String = "",
    var title: String = "",
    var imagem: Bitmap? = null,
    var autor: String = "",
    var lancamento: String = "",
    var sinopse: String = "",
    var favoritoIndication: Boolean = false
    ):Serializable