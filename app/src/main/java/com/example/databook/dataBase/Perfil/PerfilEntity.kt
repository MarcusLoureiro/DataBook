package com.example.databook.dataBase.Perfil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.hardware.Camera.open
import android.net.Uri
import android.os.ParcelFileDescriptor.open
import androidx.core.graphics.get
import androidx.room.Entity
import androidx.room.Ignore
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

@Entity(tableName = "perfiltable")
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