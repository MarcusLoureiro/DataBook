package com.example.databook.livro

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Images.Media.insertImage
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.databook.R
import com.example.databook.R.drawable
import com.example.databook.database.favoritos.FavoritosEntity
import com.example.databook.database.favoritos.FavoritosViewModel
import com.example.databook.database.perfil.PerfisViewModel
import com.example.databook.entities.Item
import com.google.firebase.auth.FirebaseAuth
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import kotlinx.android.synthetic.main.activity_livro_selecionado.*
import kotlinx.android.synthetic.main.fab_menu.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*
import kotlin.jvm.Throws


@Suppress("DEPRECATION", "UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
class LivroSelecionadoActivity : AppCompatActivity() {
    private lateinit var viewModelFav: FavoritosViewModel
    private lateinit var viewModelPerfil: PerfisViewModel
    var pdfPath = ""
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var second = 0
    var savedDay = 0
    var savedMonth = ""
    var savedYear = 0
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
            fabEditar.isClickable = false
        }

        fabMenu.setOnClickListener {
            onFabMenuClicked()
        }
        fabEditar.setOnClickListener {
            if (favoritos == true) {
                updateFav()
            } else {
                fabEditar.setImageResource(R.drawable.ic_edit)
                Toast.makeText(
                    this,
                    "Somente livros favoritos podem ser editados!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        fabFavoritar.setOnClickListener {
            if (favoritos == true) {
                Toast.makeText(
                    this,
                    "Livro já foi favoritado. Caso queira deletar. Utilize um toque longo no livro na lista da tela anterior!",
                    Toast.LENGTH_SHORT
                ).show()
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

        fabBuy.setOnClickListener {
            getDateTimeCalendar()
            createPdf()
        }



      //  pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        textViewSinopse.movementMethod = ScrollingMovementMethod()
    }

    private fun setActivity() {
        val favoritos = intent.getSerializableExtra("favoritos") as? Boolean
        Log.i("FAVORITOS", favoritos.toString())
        if (favoritos == true) {
            getFavInListData()
        } else if (favoritos == false) {
            checkBookApi(getBookApiData())
        }
    }

    private fun setBookFavInView(fav: FavoritosEntity) {
        textViewTitulo.text = fav.title
        textViewAno.text = anoFormating(fav.lancamento)
        textViewSinopse.text = fav.sinopse
        textViewAutora.text = fav.autor
        imgFundoLivroSelecionado.load(fav.imagem)
    }

    private fun checkBookApi(bookApi: Item) {
        try {
            val titulo = bookApi.volumeInfo.title
            if (titulo != "") {
                textViewTitulo.text = titulo
            } else {
                textViewTitulo.text = "Título indisponível"
            }
        } catch (e: Exception) {
            val titulo = "Título indisponível"
            textViewTitulo.text = titulo
        }
        try {
            val ano = bookApi.volumeInfo.publishedDate
            textViewAno.text = anoFormating(ano)
        } catch (e: Exception) {
            val ano = "Ano indisponível"
            textViewAno.text = ano
        }
        try {
            val sinopse = bookApi.volumeInfo.description
            textViewSinopse.text = sinopse
        } catch (e: Exception) {
            val sinopse = "Sinopse indisponível"
            textViewSinopse.text = sinopse
        }
        try {
            val autor = bookApi.volumeInfo.authors[0]
            textViewAutora.text = autor
        } catch (e: Exception) {
            val autor = "Autor(a) indisponível"
            textViewAutora.text = autor
        }
        try {
            val imagem = bookApi.volumeInfo.imageLinks.thumbnail
            imgFundoLivroSelecionado.load(imagem)
        } catch (e: Exception) {
            val imagem = R.drawable.sem_imagem
            imgFundoLivroSelecionado.load(imagem)
        }

    }

    private fun addFav(fav: FavoritosEntity) {
        viewModelFav.addFav(fav)
        Toast.makeText(
            this,
            "Favoritado com sucesso:${fav.title}",
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
                Log.i("teste position", it[position!!].toString())
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
        intent.type = "${textViewTitulo.text}/png"
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
                    perfilAtual.countCompartilhamentos = +1
                    Log.i("share", perfilAtual.countCompartilhamentos.toString())
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

    private fun anoFormating(string: String): String {
        var anoFormatado = ""
        for (i in 0..3) {
            anoFormatado += string[i]
        }
        return anoFormatado
    }

    fun View.setMargins(
        left: Int = this.marginLeft,
        top: Int = this.marginTop,
        right: Int = this.marginRight,
        bottom: Int = this.marginBottom
    ) {
        layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(left, top, right, bottom)
        }
    }
    @Throws(FileNotFoundException::class)
    private fun createPdf(){
        pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "DataBookComprovante$day$hour$minute$second.pdf")
        val outPutStream = FileOutputStream(file)
        
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)

        pdfDocument.defaultPageSize = PageSize.A6
        document.setMargins(0.0f,0.0f,0.0f,0.0f)
        val d = getDrawable(drawable.logo_pdf)
        val bitmap = d!!.toBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()

        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)

        val visitorTicket = Paragraph("Comprovante DataBook").setBold().setFontSize(24.0f).setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph("Departamento de Compra\n" +
        "Aplicativo DataBook, Brasil").setTextAlignment(TextAlignment.CENTER).setFontSize(12.0f)
        val varansi = Paragraph(textViewTitulo.text.toString()).setBold().setFontSize(20.0f).setTextAlignment(TextAlignment.CENTER)

        val width = floatArrayOf(100f,100f)
        val table = Table(width)

        table.setHorizontalAlignment(HorizontalAlignment.CENTER)

        table.addCell(Cell().add(Paragraph("Usuário")))
        table.addCell(Cell().add(Paragraph(mAuth!!.displayName.toString())))

        table.addCell(Cell().add(Paragraph("E-mail")))
        table.addCell(Cell().add(Paragraph(mAuth!!.email.toString())))

        table.addCell(Cell().add(Paragraph("Produto")))
        table.addCell(Cell().add(Paragraph(textViewTitulo.text.toString())))

        if(month in 10..12){
            month = month
        }else{
            month = ("0$month").toInt()
        }

        table.addCell(Cell().add(Paragraph("Date:")))
        table.addCell(Cell().add(Paragraph("$day/$month/$year")))

        table.addCell(Cell().add(Paragraph("Hora:")))
        table.addCell(Cell().add(Paragraph("$hour:$minute")))

        val code = BarcodeQRCode("Comprovante validado:$hour/$minute")
        val qrCodeObject = code.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

     // document.add(image)
        document.add(visitorTicket)
        document.add(group)
        document.add(varansi)
        document.add(table)
        document.add(qrCodeImage)
        document.close()

        Toast.makeText(this, "Compra Efetuada", Toast.LENGTH_SHORT).show()
    }

    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        second = cal.get(Calendar.SECOND)

    }



}
