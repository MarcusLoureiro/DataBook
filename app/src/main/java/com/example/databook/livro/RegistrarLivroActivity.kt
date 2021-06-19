package com.example.databook.livro

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.databook.R
import com.example.databook.database.favoritos.FavoritosEntity
import com.example.databook.database.favoritos.FavoritosViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registrar_livro.*
import kotlinx.android.synthetic.main.custom_alert.view.*
import kotlinx.coroutines.launch
import java.util.*


@Suppress("DEPRECATION", "NAME_SHADOWING")
class RegistrarLivroActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private var pickImage = ""
    private var imageUri: Uri? = null
    private var imagemBitmap: Bitmap? = null
    private lateinit var viewModelFav: FavoritosViewModel
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var savedDay = 0
    var savedMonth = ""
    var savedYear = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelFav = ViewModelProvider(this).get(FavoritosViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_livro)

        edSinopse.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

        val edit = intent.getSerializableExtra("edit") as? Boolean
        if (edit!!) {
            val position = intent.getSerializableExtra("position") as? Int
            btn_registrar.text = "EDITAR"
            viewModelFav.favList.observe(this) {
                setInfosForEdit(it[position!!])
            }
        }

        iv_livro.setOnClickListener {
            createAlert()
        }

        edAno.setOnClickListener {
            pickDate()
        }

        btn_registrar.setOnClickListener {
            val edit = intent.getSerializableExtra("edit") as? Boolean

            if (edit!!) {
                updateInfosForEdit()
                Toast.makeText(this, "Editou", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                getDadosLivro()
                finish()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            123 -> {
                val bmp = data?.extras?.get("data") as Bitmap
                iv_livro.setImageBitmap(bmp)
                imagemBitmap = bmp
                pickImage = "bitmap"
            }
            456 -> {
                iv_livro.setImageURI(data?.data)
                imageUri = data?.data
                pickImage = "uri"
            }
        }
    }

    private fun getDadosLivro() {
        if (testarCampos()) {
            val id = UUID.randomUUID().toString()
            val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val title = edTitle.text.toString()
            val ano = savedYear.toString()
            val autor = edAutor.text.toString()
            val sinopse = edSinopse.text.toString()
            lifecycleScope.launch {
                if (pickImage == "bitmap") {
                    val bitmap = imagemBitmap as Bitmap
                    val fav = FavoritosEntity(id, userID, title, bitmap, autor, ano, sinopse, true)
                    addFavList(fav)
                } else if (pickImage == "uri") {
                    val uri = getBitmap(imageUri.toString())
                    val fav = FavoritosEntity(id, userID, title, uri, autor, ano, sinopse, true)
                    addFavList(fav)
                }
            }
        }
    }

    private fun testarCampos(): Boolean {
        var teste = true
        if (edTitle.text.isNullOrBlank()) {
            teste = false
            showMsg("Email inválido")
        }
        if (edAutor.text.isNullOrBlank()) {
            teste = false
            showMsg("Senha inválida")
        }
        if (edSinopse.text.isNullOrBlank()) {
            teste = false
            showMsg("Email não confere")
        }
        if (edAno.text.isNullOrBlank()) {
            teste = false
            showMsg("Senha não confere")
        }
        if (imageUri.toString().isBlank()) {
            teste = false
            showMsg("Adicione uma imagem")
        }
        return teste
    }

    private fun showMsg(msg: String) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun addFavList(fav: FavoritosEntity) {
        viewModelFav.addFav(
            FavoritosEntity(
                fav.id,
                fav.userID,
                fav.title,
                fav.imagem,
                fav.autor,
                fav.lancamento,
                fav.sinopse,
                true
            )
        )
    }


    private fun createAlert() {
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

    private fun setInfosForEdit(fav: FavoritosEntity) {
        edTitle.setText(fav.title)
        edAno.setText(fav.lancamento)
        edAutor.setText(fav.autor)
        edSinopse.setText(fav.sinopse)
        iv_livro.setImageBitmap(fav.imagem)
    }

    private fun updateInfosForEdit() {
        val position = intent.getSerializableExtra("position") as? Int
        viewModelFav.favList.observe(this) {
                val fav = it[position!!]
                fav.sinopse = edSinopse.text.toString()
                fav.autor = edAutor.text.toString()
                fav.title = edTitle.text.toString()
                fav.lancamento = edAno.text.toString()
                viewModelFav.updateFav(fav)
        }
    }



    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)

    }

    private fun pickDate(){
        getDateTimeCalendar()

        DatePickerDialog(this, this, year, month, day).show()
    }


    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        if(month in 10..12){
            savedMonth = month.toString()
        }else{
            savedMonth = ("0$month").toString()
        }
        savedYear = year
        getDateTimeCalendar()
        edAno.setText("${savedDay}/${savedMonth}/${savedYear}")
    }
}

