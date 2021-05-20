package com.example.databook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_livro_selecionado.*
import kotlinx.android.synthetic.main.login_body.*

class LivroSelecionadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livro_selecionado)
        setinfos()

        imgFundoLivroSelecionado.setOnClickListener {
            includeInfos.isGone = true
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