package com.example.databook.livro

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.databook.dataBase.Favoritos.FavoritosViewModel
import com.example.databook.R
import com.example.databook.dataBase.Favoritos.FavoritosEntity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_resgitrar_livro.*
import kotlinx.android.synthetic.main.custom_alert.view.*
import kotlinx.coroutines.launch
import java.util.*


class ResgitrarLivroActivity : AppCompatActivity() {
    private var pickImage = ""
    private var imageUri: Uri? = null
    private var imagemBitmap: Bitmap? = null
    private lateinit var viewModelFav: FavoritosViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelFav = ViewModelProvider(this).get(FavoritosViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resgitrar_livro)

        iv_livro.setOnClickListener {
            creatAlert()
        }

        btn_registrar.setOnClickListener {
            getDadosLivro()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            123->{
                var bmp = data?.extras?.get("data") as Bitmap
                iv_livro.setImageBitmap(bmp)
                imagemBitmap = bmp
                pickImage = "bitmap"
            }
            456->{
                iv_livro.setImageURI(data?.data)
                imageUri = data?.data
                pickImage = "uri"
                var bmp = data?.extras?.get("data")
            }
        }
    }

    fun getDadosLivro() {
        if (testarCampos() == true) {
            var id = UUID.randomUUID().toString()
            var userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            var title = edTitle.text.toString()
            var ano = edAno.text.toString()
            var autor = edAutor.text.toString()
            var sinopse = edSinopse.text.toString()
            lifecycleScope.launch {
                if (pickImage == "bitmap" ) {
                    var bitmap = imagemBitmap as Bitmap
                    val fav = FavoritosEntity(id, userID, title, bitmap, autor, ano, sinopse, true)
                    addFavList(fav)
                } else if (pickImage == "uri"){
                    var uri = getBitmap(imageUri.toString())
                    val fav = FavoritosEntity(id, userID, title, uri, autor, ano, sinopse, true)
                    addFavList(fav)
                }
            }
        }
    }
    fun testarCampos(): Boolean {
        var teste = true
        if (edTitle.text.isNullOrBlank() == true) {
            teste = false
            showMsg("Email inválido")
        }
        if (edAutor.text.isNullOrBlank() == true) {
            teste = false
            showMsg("Senha inválida")
        }
        if (edSinopse.text.isNullOrBlank() == true) {
            teste = false
            showMsg("Email não confere")
        }
        if (edAno.text.isNullOrBlank() == true) {
            teste = false
            showMsg("Senha não confere")
        }
        if (imageUri.toString().isNullOrBlank() == true) {
            teste = false
            showMsg("Adicione uma imagem")
        }
        return teste
    }

    fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun addFavList(fav: FavoritosEntity) {
        viewModelFav.addFav(
            FavoritosEntity(
                fav.id,
                fav.UserID,
                fav.title,
                fav.imagem,
                fav.autor,
                fav.lancamento,
                fav.sinopse,
                true
            )
        )
    }


    fun creatAlert() {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded).create()
        val view: View = LayoutInflater.from(this).inflate(R.layout.custom_alert, null)
        builder.setView(view)
        builder.show()
        view.btAlert_galeria.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 456)
            builder.dismiss()
        }
        view.btAlert_camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 123)
            builder.dismiss()
        }
    }

    private suspend fun getBitmap(data: String): Bitmap {
        val loanding = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(data)
            .build()

        val result = (loanding.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

}
