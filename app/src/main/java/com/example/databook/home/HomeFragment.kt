package com.example.databook.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.databook.dataBase.Favoritos.FavoritosViewModel
import com.example.databook.livro.LivroSelecionadoActivity
import com.example.databook.R
import com.example.databook.livro.LivroAdapter
import com.example.databook.livro.LivroFavAdapter
import com.example.databook.livro.ResgitrarLivroActivity
import com.example.databook.dataBase.Favoritos.FavoritosEntity
import com.example.databook.dataBase.Perfil.PerfilEntity
import com.example.databook.dataBase.Perfil.PerfisViewModel
import com.example.isbm.Entities.Item
import com.example.isbm.Services.MainViewModel
import com.example.isbm.Services.service
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home_favoritos.view.*
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), LivroAdapter.OnLivroClickListener,
    LivroFavAdapter.OnLivroFavClickListener {
    var Favoritos: Boolean? = null

    private lateinit var viewModelPerfil: PerfisViewModel
    private lateinit var listLivro: List<Item>
    private lateinit var listFavs: List<FavoritosEntity>
    private lateinit var viewModelFav: FavoritosViewModel

    val mAuth = FirebaseAuth.getInstance().currentUser
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
        viewModelPerfil = ViewModelProvider(this).get(PerfisViewModel::class.java)
        viewModelFav = ViewModelProvider(this).get(FavoritosViewModel::class.java)
        var view = inflater.inflate(R.layout.fragment_home_favoritos, container, false)
        lifecycleScope.launch {
            viewModelPerfil.addPerfil(
                PerfilEntity(
                    mAuth!!.uid,
                    getBitmap(mAuth!!.photoUrl.toString()),
                    mAuth!!.displayName.toString(),
                    mAuth!!.email.toString(),
                    0,
                    0,
                    "")
            )
        }
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

    fun callResultsSearch(view: View) {
        var searchText = view.textInputPesquisa.editText?.text.toString()
        if (searchText != "") {
            viewModel.getSearch(searchText)
            viewModel.returnSearchList.observe(viewLifecycleOwner) {
                listLivro = it.items
                view.rv_result.layoutManager =
                    GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
                view.rv_result.setHasFixedSize(true)
                var livroAdapter = LivroAdapter(this)
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
            viewModelPerfil.perfilList.observe(viewLifecycleOwner) {
                it.forEach {
                    if (it.userID == mAuth!!.uid) {
                        var PerfilAtual = it
                        PerfilAtual.countFavoritos = listFavs.size
                        viewModelPerfil.updatePerfil(PerfilAtual)
                    }
                }
            }
        }
    }


    override fun livroClick(position: Int) {
        val book = listLivro.get(position)
        val intent = Intent(context, LivroSelecionadoActivity::class.java)
        var adapter = LivroAdapter(this)
        intent.putExtra("bookApi", book)
        intent.putExtra("favoritos", false)
        adapter.notifyDataSetChanged()
        startActivity(intent)
    }

    override fun livroFavClick(position: Int) {
        var item = listFavs[position]
        val intent = Intent(activity, LivroSelecionadoActivity::class.java)
        var adapter = LivroFavAdapter(this)
        intent.putExtra("position", position)
        intent.putExtra("favoritos", true)
        adapter.notifyDataSetChanged()
        startActivity(intent)

    }

    private suspend fun getBitmap(data: String): Bitmap {
        val loanding = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(data)
            .build()

        val result = (loanding.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }


}




