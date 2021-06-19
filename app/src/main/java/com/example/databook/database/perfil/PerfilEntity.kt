package com.example.databook.database.perfil

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "perfiltable")
//Criando a classe PerfilEntity  com os atributos necessários.
data class PerfilEntity (
    @PrimaryKey(autoGenerate = false)
    var userID: String = "",
    var imagemPerfil: Bitmap? = null,
    var nome: String = "",
    var email: String = "",
    var countFavoritos: Int = 0,
    var countCompartilhamentos: Int = 0,
    var ultimoTermo: String = ""
    ):Serializable