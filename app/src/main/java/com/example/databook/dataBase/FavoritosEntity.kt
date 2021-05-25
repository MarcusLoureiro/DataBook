package com.example.filmapp.Media.dataBase

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseAuth

@Entity(tableName = "favoritostable")
data class FavoritosEntity (
    @PrimaryKey(autoGenerate = false)
    val id:String,
    var UserID: String,
    var title: String,
    var imagem: String,
    var autor: String,
    var lancamento: String,
    var sinopse: String
    )