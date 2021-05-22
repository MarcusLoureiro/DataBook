package com.example.databook.livro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import com.example.databook.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_livro_selecionado.*
import kotlinx.android.synthetic.main.login_body.*

class LivroSelecionadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
        val id = intent.getSerializableExtra("id") as? Boolean
        val titulo = intent.getSerializableExtra("titulo") as? String
        val imagem = intent.getSerializableExtra("imagem") as? Int
        val autora = intent.getSerializableExtra("autora") as? String
        val ano = intent.getSerializableExtra("ano") as? String
        val sinopse = intent.getSerializableExtra("sinopse") as? String
        val favoritos = intent.getSerializableExtra("favoritos") as? Boolean
        Picasso.get().load(imagem!!).into(imgFundoLivroSelecionado)
        textViewTitulo.setText(titulo)
        textViewAno.setText(ano)
        textViewSinopse.setText(sinopse)
        textViewAutora.setText(autora)
    }
}