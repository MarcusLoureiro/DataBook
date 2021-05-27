package com.example.databook.livro

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.dataBase.FavoritosViewModel
import com.example.databook.R
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.example.isbm.Entities.Item
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
        share_ic.setOnClickListener {
            setShareIntent()
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
        }else if(favoritos == false){
            val item = intent.getSerializableExtra("bookApi") as? Item
            val bookApi = item?.volumeInfo
            try{
                textViewTitulo.setText(bookApi?.title)
            }catch (e:Exception){
                textViewTitulo.setText("Indisponível")
            }
            try{
                textViewAno.setText(bookApi?.publishedDate)
            }catch (e:Exception){
                textViewAno.setText("Indisponível")
            }
            try{
                textViewSinopse.setText(bookApi?.description)
            }catch (e:Exception){
                textViewSinopse.setText("Indisponível")
            }
            try{
                textViewAutora.setText(bookApi?.authors!![0])
            }catch (e:Exception){
                textViewAutora.setText("Indisponível")
            }
            try {
                imgFundoLivroSelecionado.load(bookApi?.imageLinks?.thumbnail)
            }catch (e:Exception){
                imgFundoLivroSelecionado.load(R.drawable.sem_imagem)
            }
        }
    }

    fun setShareIntent(){
        val drawable = imgFundoLivroSelecionado.drawable
        val bitmap = drawable.toBitmap()
        val bitmapPath = MediaStore.Images
            .Media
            .insertImage(contentResolver,bitmap,"title",null)
        val uri = Uri.parse(bitmapPath)
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("image/png")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, "Nome do App: DataBook\n" +
                "Título do livro:${textViewTitulo.text}\n" +
                "Sinopse do livro:${textViewSinopse.text}\n")
        startActivity(Intent.createChooser(intent, "${textViewTitulo.text}"))
    }
}