package com.example.databook.livro

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.dataBase.FavoritosViewModel
import com.example.databook.R
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.example.isbm.Entities.Item
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_livro_selecionado.*
import kotlinx.android.synthetic.main.login_body.*

class LivroSelecionadoActivity : AppCompatActivity() {
    lateinit var viewModelFav: FavoritosViewModel
    var bookCheckedFavorito: FavoritosEntity = FavoritosEntity()
    var FavoritoApi: FavoritosEntity = FavoritosEntity()
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
        viewModelFav.favList.observe(this){
            bookCheckedFavorito = viewModelFav.checkFavoritoInList(bookCheckedFavorito, it)
            if (bookCheckedFavorito.favoritoIndication == true){
                favorito_ic.setImageResource(R.drawable.ic_favorito_select)
            }else if(bookCheckedFavorito.favoritoIndication == false){
                favorito_ic.setImageResource(R.drawable.ic_favorito_amarelo)
            }
        }
        favorito_ic.setOnClickListener {
            if (bookCheckedFavorito.favoritoIndication == true) {
                viewModelFav.removeMedia(bookCheckedFavorito)
                favorito_ic.setImageResource(R.drawable.ic_favorito_amarelo)
                bookCheckedFavorito.favoritoIndication = false

            } else if(bookCheckedFavorito.favoritoIndication == false) {
                viewModelFav.saveNewMedia(bookCheckedFavorito)
                bookCheckedFavorito.favoritoIndication = true
                favorito_ic.setImageResource(R.drawable.ic_favorito_select)
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
        }else if(favoritos == false){
            val item = intent.getSerializableExtra("bookApi") as? Item
            val bookApi = item?.volumeInfo
            var id = item?.id
            var titulo = bookApi?.title
            var autor = bookApi?.authors?.get(0)
            var ano = bookApi?.publishedDate
            var sinopse = bookApi?.description
            var imagem = bookApi?.imageLinks?.thumbnail
            try{
                textViewTitulo.setText(titulo)
            }catch (e:Exception){
                textViewTitulo.setText("Indisponível")
            }
            try{
                textViewAno.setText(bookApi?.publishedDate)
            }catch (e:Exception){
                textViewAno.setText(ano)
            }
            try{
                textViewSinopse.setText(sinopse)
            }catch (e:Exception){
                textViewTitulo.setText("Indisponível")
            }
            try{
                textViewAutora.setText(autor)
            }catch (e:Exception){
                textViewAutora.setText("Indisponível")
            }
            try {
                imgFundoLivroSelecionado.load(imagem)
            }catch (e:Exception){
                imgFundoLivroSelecionado.load(R.drawable.sem_imagem)
            }
//            val userID = FirebaseAuth.getInstance().currentUser?.uid
//            lifecycleScope.launchWhenCreated {
//                var bitmap = getBitmap(imagem.toString())
//                FavoritoApi = FavoritosEntity(id.toString(),
//                    userID.toString(),
//                    titulo.toString(),
//                    bitmap,autor.toString(),
//                    ano.toString(),sinopse.toString(),
//                    false )
//            }
//
//            favorito_ic.setOnClickListener{
//                FavoritoApi.favoritoIndication = true
//                viewModelFav.saveNewMedia(FavoritoApi)
//                favorito_ic.setImageResource(R.drawable.ic_favorito_select)
//            }
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
                "Sinopse do livro:${textViewTitulo.text}\n")
        startActivity(Intent.createChooser(intent, "${textViewTitulo.text}"))
    }

//    private suspend fun getBitmap(data: String): Bitmap {
//        val loanding = ImageLoader(this)
//        val request = ImageRequest.Builder(this)
//            .data(data)
//            .build()
//        try {
//            val result = (loanding.execute(request) as SuccessResult).drawable
//            return (result as BitmapDrawable).bitmap
//        }catch (e:Exception){
//            return (R.drawable.sem_imagem as BitmapDrawable).bitmap
//        }
//    }
}