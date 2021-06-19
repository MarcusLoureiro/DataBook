package com.example.databook.livro

import android.annotation.SuppressLint
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

    /*Inicialização de variavéis dos ViewModels, FirebaseAuth e Data e Tempo.*/
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


    //================================================Animações dos floating button==================================//
    /*Rotação para abrir menu de botões*/
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }

    /*Rotação para fechar menu de botões*/
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }

    /*Rotação para descer menu de botões*/
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }

    /*Rotação para subir menu de botões*/
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }


    private var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {


        /** Declarando viewModel do Banco de dados:
         * Tabela de Perfil
         * Tabela de Favoritos
         */
        viewModelPerfil = ViewModelProvider(this).get(PerfisViewModel::class.java)
        viewModelFav = ViewModelProvider(this).get(FavoritosViewModel::class.java)

        //Recebendo via intent se o Livro selecionado é um favorito.
        val favoritos = intent.getSerializableExtra("favoritos") as? Boolean

        //Variável isClick para esconder informações do livro e mostrar o fundo da imagem do livro.
        var isClick = false

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livro_selecionado)
        setActivity()
        setVisibility(true)

        //Aqui será feito a alternância entre mostrar somente o fundo ou não.
        imgFundoLivroSelecionado.setOnClickListener {
            if (!isClick) {
                includeInfos.isGone = true
                isClick = true
            } else if (isClick) {
                includeInfos.isGone = false
                isClick = false
            }
        }

        //Definir automaticamente se o livro já está na lista de favoritos e setar o ícone ideal.
        if (favoritos == true) {
            fabFavoritar.setImageResource(drawable.ic_favorito_select)
        } else {
            fabFavoritar.setImageResource(drawable.ic_favorito_amarelo)
            fabEditar.isClickable = false
        }

        //Floating Button que abre o menu com as outras opções.
        fabMenu.setOnClickListener {
            onFabMenuClicked()
        }

        //Ativar a função de editar informações do livro.
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

        //Ativar a função de favoritar um livro.
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

        //Ativar a intent para que se compartilhe o livro com as redes sociais.
        fabCompatilhar.setOnClickListener {
            setShareIntent()
        }

        //Emite o comprovante em .pdf do livro. Simulação simples de compra com 1-click
        fabBuy.setOnClickListener {
            getDateTimeCalendar()
            createPdf()
        }

        //Ativando método automático de scroll no textView que exibe a sinopse do livro.
        textViewSinopse.movementMethod = ScrollingMovementMethod()
    }

    /**Função para setar a Activity:
     * Pega o livro da API
     * Pega o livro do Banco de Dados
     * De acordo com a variável "favoritos"*/
    private fun setActivity() {
        val favoritos = intent.getSerializableExtra("favoritos") as? Boolean
        Log.i("FAVORITOS", favoritos.toString())
        if (favoritos == true) {
            getFavInListData()
        } else if (favoritos == false) {
            checkBookApi(getBookApiData())
        }
    }


    /**Função para setar informações de um livro da tabela de favoritos do Banco de dados:
     * Título
     * Ano
     * Sinopse
     * Autor(a)
     * Imagem*/
    private fun setBookFavInView(fav: FavoritosEntity) {
        textViewTitulo.text = fav.title
        textViewAno.text = anoFormating(fav.lancamento)
        textViewSinopse.text = fav.sinopse
        textViewAutora.text = fav.autor
        imgFundoLivroSelecionado.load(fav.imagem)
    }

    /**Função para checar informações de um livro da API Google Books antes de exibir:
     * Título
     * Ano
     * Sinopse
     * Autor(a)
     * Imagem*/
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

    /**Função para pegar o item da API Google Books via intent e no fim será retornado o item.*/
    private fun getBookApiData(): Item {
        val item = intent.getSerializableExtra("bookApi") as? Item
        return item as Item
    }

    /**Função para adicionar um livro na tabela de favoritos do Banco de dados:
     * Um toast é exibido para avisar que a função foi finalizada*/
    private fun addFav(fav: FavoritosEntity) {
        viewModelFav.addFav(fav)
        Toast.makeText(
            this,
            "Favoritado com sucesso:${fav.title}",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**Função para editar um livro na tabela de favoritos do Banco de dados:
     * Pegasse a position do livro na lista do Banco
     * Passasse via intent para a Activity RegistrarLivro a position e se é para editar
     * Start da activity (intent)*/
    private fun updateFav() {
        val position = intent.getSerializableExtra("position") as? Int
        val intent = Intent(this, RegistrarLivroActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("edit", true)
        startActivity(intent)

    }


    /**Função para pegar um livro na tabela de favoritos do Banco de dados:*/
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


    /**Função para compartilhar um livro pelas redes sociais:
     * Seja Favorito
     * Seja da API
     * No fim se atualiza a perfil do usuário utilizando a função updatePerfilInfos()*/
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
        updatePerfilInfos()
    }

    /**Função atualiza a perfil do usuário:
     * Usando ViewModelPerfil
     * Pegasse o usuário logado pelo FirebaseAuth e comparasse com o id salvo no Banco
     * No fim se atualiza por meio da função updatePerfil() o número de livros compartilhados*/
    private fun updatePerfilInfos() {
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

    /**Função usada pelo Floating Button menu:
     * Chamasse a função de Visibilidade
     * Chamasse a função de animação
     * alterasse o valor boolean de click*/
    private fun onFabMenuClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    /**Função usada para ativar os status de visibilidade dos Floating Buttons do Menu:
     * Dependendo do click*/

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

    /**Função usada para ativar as animações dos Floating Buttons do Menu:
     * Dependendo do click*/
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

    /**Função usada para pegar um Bitmap:
     * transforma url em bitmap
     * usada para imagens do banco e da API*/
    private suspend fun getBitmap(data: String): Bitmap {
        val loanding = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(data)
            .build()
        val result = (loanding.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    /**Função para mudar o ícone do favorito:
     * Dependendo se for favorito ou não*/
    private fun changeIconFav(fav: Boolean) {
        if (fav) {
            fabFavoritar.setImageResource(drawable.ic_favorito_amarelo)
        } else {
            fabFavoritar.setImageResource(drawable.ic_favorito_select)
        }
    }

    /**Função usada formatar os anos dos livros do(a):
     * API
     * Banco*/
    private fun anoFormating(string: String): String {
        var anoFormatado = ""
        for (i in 0..3) {
            anoFormatado += string[i]
        }
        return anoFormatado
    }


    /**Cria um arquivo pdf e salva na pasta de Downloads do dispositivo*/
    @SuppressLint("UseCompatLoadingForDrawables")
    @Throws(FileNotFoundException::class)
    private fun createPdf() {
        pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()
        val file = File(pdfPath, "DataBookComprovante$day$hour$minute$second.pdf")
        val outPutStream = FileOutputStream(file)

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)

        pdfDocument.defaultPageSize = PageSize.A6
        document.setMargins(0.0f, 0.0f, 0.0f, 0.0f)
        val d = getDrawable(drawable.logo_pdf)
        val bitmap = d!!.toBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()

        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)

        val visitorTicket = Paragraph("Comprovante DataBook").setBold().setFontSize(24.0f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            "Departamento de Compra\n" +
                    "Aplicativo DataBook, Brasil"
        ).setTextAlignment(TextAlignment.CENTER).setFontSize(12.0f)
        val varansi = Paragraph(textViewTitulo.text.toString()).setBold().setFontSize(20.0f)
            .setTextAlignment(TextAlignment.CENTER)

        val width = floatArrayOf(100f, 100f)
        val table = Table(width)

        table.setHorizontalAlignment(HorizontalAlignment.CENTER)

        table.addCell(Cell().add(Paragraph("Usuário")))
        table.addCell(Cell().add(Paragraph(mAuth!!.displayName.toString())))

        table.addCell(Cell().add(Paragraph("E-mail")))
        table.addCell(Cell().add(Paragraph(mAuth!!.email.toString())))

        table.addCell(Cell().add(Paragraph("Produto")))
        table.addCell(Cell().add(Paragraph(textViewTitulo.text.toString())))

        if (month in 10..12) {
            month = month
        } else {
            month = ("0$month").toInt()
        }

        table.addCell(Cell().add(Paragraph("Date:")))
        table.addCell(Cell().add(Paragraph("$day/$month/$year")))

        table.addCell(Cell().add(Paragraph("Hora:")))
        table.addCell(Cell().add(Paragraph("$hour:$minute")))

        val code = BarcodeQRCode("Comprovante validado:$hour/$minute\nID Usuário:${mAuth.uid}")
        val qrCodeObject = code.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage =
            Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

        // document.add(image)
        document.add(visitorTicket)
        document.add(group)
        document.add(varansi)
        document.add(table)
        document.add(qrCodeImage)
        document.close()

        Toast.makeText(this, "Compra Efetuada", Toast.LENGTH_SHORT).show()
    }

    /**Pega data e hora atual do dispositivo*/
    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        second = cal.get(Calendar.SECOND)

    }


}
