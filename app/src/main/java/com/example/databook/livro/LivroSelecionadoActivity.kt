package com.example.databook.livro

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.dataBase.FavoritosViewModel
import com.example.databook.R
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_livro_selecionado.*
import kotlinx.android.synthetic.main.login_body.*

class LivroSelecionadoActivity : AppCompatActivity() {
    lateinit var viewModelFav: FavoritosViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelFav = ViewModelProvider(this).get(FavoritosViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livro_selecionado)
        setinfos()
        var isClick = false

        imgFundoLivroSelecionado.setOnClickListener {
            if(isClick == false){
                includeInfos.isGone = true
                isClick = true
            }else if (isClick == true){
                includeInfos.isGone = false
                isClick = false
            }

        }
    }

    private fun setinfos() {
        val favoritos = intent.getSerializableExtra("favoritos") as? Boolean
        if(favoritos == true){
            val position = intent.getSerializableExtra("position") as? Int
            viewModelFav.favList.observe(this){
                var fav = it[position!!]
                textViewTitulo.setText(fav.title)
                textViewAno.setText(fav.lancamento)
                textViewSinopse.setText(fav.sinopse)
                textViewAutora.setText(fav.autor)
                imgFundoLivroSelecionado.load(fav.imagem)
            }

        }


        imgFundoLivroSelecionado.setImageResource(R.drawable.sem_imagem)
//        if(favoritos == true){
//            imgFundoLivroSelecionado.setImageURI(imagem.toString().toUri())
//        }else {
//            Picasso.get().load(imagem).into(imgFundoLivroSelecionado)
//        }

    }
}