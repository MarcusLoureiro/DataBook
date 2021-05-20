package com.example.databook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databook.adapter.LivroAdapter
import com.example.desafiofirebase.entities.Livro
import com.example.desafiofirebase.entities.Repository
import kotlinx.android.synthetic.main.fragment_home_favoritos.*
import kotlinx.android.synthetic.main.fragment_home_favoritos.view.*

class HomeFragment:Fragment(), LivroAdapter.OnLivroClickListener{
    var Favoritos: Boolean? = null
    private lateinit var livroAdapter: LivroAdapter
    var listLivro = addLivro()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            Favoritos = arguments?.getBoolean(favoritos)
        }


    }

    companion object {
        private val favoritos = "favoritos"

        fun newInstance(Favoritos: Boolean): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putBoolean(favoritos, Favoritos)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home_favoritos, container, false)

        view.fb_addBook.setOnClickListener {
                IniciarTelaRegistro()
        }

        view.rv_result.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        view.rv_result.setHasFixedSize(true)
        livroAdapter = LivroAdapter(listLivro, this)
        view.rv_result.adapter = livroAdapter
        return view

    }

    fun IniciarTelaRegistro(){
        val intent = Intent(activity, ResgitrarLivroActivity::class.java)
        startActivity(intent)
    }

    fun IniciarTelaLivroSelecionado(){
        val intent = Intent(activity, LivroSelecionadoActivity::class.java)
        startActivity(intent)
    }

    fun addLivro():ArrayList<Livro>{
        return arrayListOf<Livro>(
            Livro("123", "Harry Potter e a Pedra Filosofal", "J.K Rowling",  "2015", "", R.drawable.img3),
            Livro("123", "Harry Potter e a Pedra Filosofal", "J.K Rowling",  "2015", "", R.drawable.img3),
            Livro("123", "Harry Potter e a Pedra Filosofal", "J.K Rowling",  "2015", "", R.drawable.img3),
            Livro("123", "Harry Potter e a Pedra Filosofal", "J.K Rowling",  "2015", "", R.drawable.img3),
            Livro("123", "Harry Potter e a Pedra Filosofal", "J.K Rowling",  "2015", "", R.drawable.img3),
            Livro("123", "Harry Potter e a Pedra Filosofal", "J.K Rowling",  "2015", "", R.drawable.img3),
            Livro("123", "Harry Potter e a Pedra Filosofal", "J.K Rowling",  "2015", "", R.drawable.img3)
        )

    }


    override fun livroClick(position: Int) {
        val media = listLivro.get(position)
        val intent = Intent(context, LivroSelecionadoActivity::class.java)
        intent.putExtra("imagem", media.URL)
        intent.putExtra("favoritos", true)
        intent.putExtra("sinopse", media.sinopse)
        intent.putExtra("id", media.id)
        intent.putExtra("titulo", media.titulo)
        intent.putExtra("autora", media.autora)
        intent.putExtra("ano", media.ano)
        startActivity(intent)
        livroAdapter.notifyDataSetChanged()
    }
}


