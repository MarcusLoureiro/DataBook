package com.example.databook.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dataBase.FavoritosViewModel
import com.example.databook.livro.LivroSelecionadoActivity
import com.example.databook.R
import com.example.databook.livro.LivroAdapter
import com.example.databook.livro.LivroFavAdapter
import com.example.databook.livro.ResgitrarLivroActivity
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.example.isbm.Entities.Item
import com.example.isbm.Entities.VolumeInfo
import com.example.isbm.Services.MainViewModel
import com.example.isbm.Services.service
import kotlinx.android.synthetic.main.fragment_home_favoritos.view.*

class HomeFragment : Fragment(), LivroAdapter.OnLivroClickListener,
    LivroFavAdapter.OnLivroFavClickListener {
    var Favoritos: Boolean? = null
    private lateinit var listLivro: List<Item>
    private lateinit var listFavs: List<FavoritosEntity>
    private lateinit var viewModelFav: FavoritosViewModel


    val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(service) as T
            }
        }
    }


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFav = ViewModelProvider(this).get(FavoritosViewModel::class.java)
        var view = inflater.inflate(R.layout.fragment_home_favoritos, container, false)

        view.fb_addBook.setOnClickListener {
            IniciarTelaRegistro()
        }
        if (Favoritos == false) {

            view.textInputPesquisa.setEndIconOnClickListener {
                callResultsSearch(view)
            }

            view.textInputPesquisa.editText?.doOnTextChanged { inputText, _, _, _ ->
                callResultsSearch(view)
            }
        } else {
            callResultFavs(view)
        }
        return view

    }

    fun IniciarTelaRegistro() {
        val intent = Intent(activity, ResgitrarLivroActivity::class.java)
        startActivity(intent)
    }


    override fun livroClick(position: Int) {
//        val media = listLivro.get(position)
//        val intent = Intent(context, LivroSelecionadoActivity::class.java)
//        intent.putExtra("imagem", media.URL)
//        intent.putExtra("favoritos", true)
//        intent.putExtra("sinopse", media.sinopse)
//        intent.putExtra("id", media.id)
//        intent.putExtra("titulo", media.titulo)
//        intent.putExtra("autora", media.autora)
//        intent.putExtra("ano", media.ano)
//        startActivity(intent)
//        livroAdapter.notifyDataSetChanged()
    }

    fun callResultsSearch(view: View) {
        var searchText = view.textInputPesquisa.editText?.text.toString()
        if (searchText != "") {
            viewModel.getSearch(searchText)
            viewModel.returnSearchList.observe(viewLifecycleOwner) {
                listLivro = it.items
                view.rv_result.layoutManager =
                    GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
                view.rv_result.setHasFixedSize(true)
                var livroAdapter = LivroAdapter(this, requireActivity())
                livroAdapter.setData(listLivro)
                view.rv_result.adapter = livroAdapter
            }
        }
    }

    fun callResultFavs(view: View) {
        viewModelFav.favList.observe(viewLifecycleOwner) {
            listFavs = it
            view.rv_result.layoutManager =
                GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
            view.rv_result.setHasFixedSize(true)
            var adapter = LivroFavAdapter(this)
            adapter.setData(listFavs)
            view.rv_result.adapter = adapter
        }
    }

    override fun livroFavClick(position: Int) {
        var item = listFavs[position]
        val intent = Intent(activity, LivroSelecionadoActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("favoritos", true)
        startActivity(intent)
        Toast.makeText(activity, item.title, Toast.LENGTH_SHORT).show()
    }

}




