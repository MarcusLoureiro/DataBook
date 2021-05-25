package com.example.databook.livro

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dataBase.FavoritosViewModel
import com.example.databook.R
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_resgitrar_livro.*
import java.util.*


class ResgitrarLivroActivity : AppCompatActivity() {
    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var viewModelFav: FavoritosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelFav = ViewModelProvider(this).get(FavoritosViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resgitrar_livro)
        iv_livro.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(intent, pickImage)
        }
        btn_registrar.setOnClickListener {
            getDadosLivro()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            iv_livro.setImageURI(imageUri)
        }
    }
    fun getDadosLivro(){
        if(testarCampos() == true){
            var id = UUID.randomUUID().toString()
            var userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            var title = edTitle.text.toString()
            var ano = edAno.text.toString()
            var autor = edAutor.text.toString()
            var sinopse = edSinopse.text.toString()
            val fav = FavoritosEntity(id, userID, title, imageUri.toString(),autor, ano, sinopse)
            addFavList(fav)
            viewModelFav.favList.observe(this){
                Log.i("Lista Fav", it.toString())
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
        if(imageUri.toString().isNullOrBlank()==true){
            teste = false
            showMsg("Adicione uma imagem")
        }
        return teste
    }

    fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun addFavList(fav: FavoritosEntity) {
        viewModelFav.saveNewMedia(FavoritosEntity(fav.id, fav.UserID, fav.title, fav.imagem, fav.autor, fav.lancamento, fav.sinopse))
    }

    fun removeFavList(fav: FavoritosEntity) {
        viewModelFav.removeMedia(FavoritosEntity(fav.id, fav.UserID, fav.title, fav.imagem, fav.autor, fav.lancamento, fav.sinopse))
    }

}
