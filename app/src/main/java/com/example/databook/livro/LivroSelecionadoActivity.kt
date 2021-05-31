package com.example.databook.livro

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media.insertImage
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.databook.database.favoritos.FavoritosViewModel
import com.example.databook.R
import com.example.databook.R.drawable
import com.example.databook.database.favoritos.FavoritosEntity
import com.example.databook.database.perfil.PerfisViewModel
import com.example.databook.entities.Item
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_livro_selecionado.*
import kotlinx.android.synthetic.main.fab_menu.*
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class LivroSelecionadoActivity : AppCompatActivity() {
    private lateinit var viewModelFav: FavoritosViewModel
    private lateinit var viewModelPerfil: PerfisViewModel
    private val mAuth = FirebaseAuth.getInstance().currentUser

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    private var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelPerfil = ViewModelProvider(this).get(PerfisViewModel::class.java)
        viewModelFav = ViewModelProvider(this).get(FavoritosViewModel::class.java)

        val favoritos = intent.getSerializableExtra("favoritos") as? Boolean
        var isClick = false

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livro_selecionado)
        setActivity()
        setVisibility(true)
        imgFundoLivroSelecionado.setOnClickListener {
            if (!isClick) {
                includeInfos.isGone = true
                isClick = true
            } else if (isClick) {
                includeInfos.isGone = false
                isClick = false
            }
        }

        if (favoritos == true) {
            fabFavoritar.setImageResource(drawable.ic_favorito_select)
        } else {
            fabFavoritar.setImageResource(drawable.ic_favorito_amarelo)
        }

        fabMenu.setOnClickListener {
            onFabMenuClicked()
        }
        fabEditar.setOnClickListener {
            updateFav()
        }
        fabFavoritar.setOnClickListener {
            if (favoritos == true) {
                changeIconFav(true)
                val position = intent.getSerializableExtra("position") as? Int
                viewModelFav.favList.observe(this) {
                    val favCopy = FavoritosEntity()
                    if (it.isNotEmpty()) {
                        setBookFavInView(it[position!!])
                        deleteFav(it[position])
                        finish()
                    } else {
                        setBookFavInView(favCopy)
                    }
                }
            } else {
                changeIconFav(false)
                lifecycleScope.launch {
                    val bookApi = getBookApiData()
                    addFav(
                        FavoritosEntity(
                            bookApi.id,
                            FirebaseAuth.getInstance().currentUser!!.uid,
                            bookApi.volumeInfo.title,
                            getBitmap(bookApi.volumeInfo.imageLinks.thumbnail),
                            bookApi.volumeInfo.authors[0],
                            bookApi.volumeInfo.publishedDate,
                            bookApi.volumeInfo.description,
                            true
                        )
                    )
                    Log.i("BOOKAPI", bookApi.toString())
                }
            }
        }
        fabCompatilhar.setOnClickListener {
            setShareIntent()
        }

    }


    private fun setActivity() {
        val favoritos = intent.getSerializableExtra("favoritos") as? Boolean
        Log.i("FAVORITOS", favoritos.toString())
        if (favoritos == true) {
            getFavInListData()
        } else if (favoritos == false) {
            setBookApiInView(getBookApiData())
        }
    }

    private fun setBookFavInView(fav: FavoritosEntity) {
        textViewTitulo.text = fav.title
        textViewAno.text = fav.lancamento
        textViewSinopse.text = fav.sinopse
        textViewAutora.text = fav.autor
        imgFundoLivroSelecionado.load(fav.imagem)
    }

    private fun setBookApiInView(bookApi: Item) {
        val titulo = bookApi.volumeInfo.title
        val autor = bookApi.volumeInfo.authors[0]
        val ano = bookApi.volumeInfo.publishedDate
        val sinopse = bookApi.volumeInfo.description
        val imagem = bookApi.volumeInfo.imageLinks.thumbnail
        textViewTitulo.text = titulo
        textViewAno.text = ano
        textViewSinopse.text = sinopse
        textViewAutora.text = autor
        imgFundoLivroSelecionado.load(imagem)
    }


    private fun addFav(fav: FavoritosEntity) {
        viewModelFav.addFav(fav)
        Toast.makeText(
            this,
            "Favoritado com sucesso:${fav.title}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun deleteFav(fav: FavoritosEntity) {
        viewModelFav.deleteFav(fav)
        Toast.makeText(
            this,
            "Deletado com sucesso:${fav.title}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateFav() {
        val position = intent.getSerializableExtra("position") as? Int
        val intent = Intent(this, RegistrarLivroActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("edit", true)
        startActivity(intent)

    }


    private fun getBookApiData(): Item {
        val item = intent.getSerializableExtra("bookApi") as? Item
        return item as Item
    }

    private fun getFavInListData() {
        val position = intent.getSerializableExtra("position") as? Int
        viewModelFav.favList.observe(this) {
            val favCopy = FavoritosEntity()
            if (it.isNotEmpty()) {
                setBookFavInView(it[position!!])
            } else {
                setBookFavInView(favCopy)
            }
        }
    }


    private fun setShareIntent() {
        val drawable = imgFundoLivroSelecionado.drawable
        val bitmap = drawable.toBitmap()
        val bitmapPath = insertImage(contentResolver, bitmap, "title", null)
        val uri = Uri.parse(bitmapPath)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(
            Intent.EXTRA_TEXT, "Nome do App: DataBook\n" +
                    "Título do livro:${textViewTitulo.text}\n" +
                    "Sinopse do livro:${textViewSinopse.text}\n"
        )
        startActivity(Intent.createChooser(intent, "${textViewTitulo.text}"))

        viewModelPerfil.perfilList.observe(this) { it ->
            it.forEach {
                if (it.userID == mAuth!!.uid) {
                    val perfilAtual = it
                    perfilAtual.countCompartilhamentos = perfilAtual.countCompartilhamentos + 1
                    viewModelPerfil.updatePerfil(perfilAtual)
                }
            }
        }
    }

    private fun onFabMenuClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            fabFavoritar.isGone = false
            fabEditar.isGone = false
            fabCompatilhar.isGone = false
        } else {
            fabFavoritar.isGone = true
            fabEditar.isGone = true
            fabCompatilhar.isGone = true
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            fabFavoritar.startAnimation(fromBottom)
            fabEditar.startAnimation(fromBottom)
            fabCompatilhar.startAnimation(fromBottom)
            fabMenu.startAnimation(rotateOpen)
        } else {
            fabFavoritar.startAnimation(toBottom)
            fabEditar.startAnimation(toBottom)
            fabCompatilhar.startAnimation(toBottom)
            fabMenu.startAnimation(rotateClose)
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

    private fun changeIconFav(fav: Boolean) {
        if (fav) {
            fabFavoritar.setImageResource(drawable.ic_favorito_amarelo)
        } else {
            fabFavoritar.setImageResource(drawable.ic_favorito_select)
        }
    }


}

