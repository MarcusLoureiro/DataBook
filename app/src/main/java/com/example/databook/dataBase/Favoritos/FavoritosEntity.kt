package com.example.databook.dataBase.Favoritos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.hardware.Camera.open
import android.net.Uri
import android.os.ParcelFileDescriptor.open
import androidx.core.graphics.get
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.databook.R
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.IOException
import java.io.Serializable
import java.nio.channels.AsynchronousFileChannel.open
import java.nio.channels.AsynchronousServerSocketChannel.open
import java.nio.channels.Pipe.open

@Entity(tableName = "favoritostable")
data class FavoritosEntity (
    @PrimaryKey(autoGenerate = false)
    val id:String = "",
    var UserID: String = "",
    var title: String = "",
    var imagem: Bitmap? = null,
    var autor: String = "",
    var lancamento: String = "",
    var sinopse: String = "",
    var favoritoIndication: Boolean = false
    ):Serializable