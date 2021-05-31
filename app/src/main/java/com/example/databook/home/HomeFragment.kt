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
import com.example.databook.database.favoritos.FavoritosViewModel
import com.example.databook.livro.LivroSelecionadoActivity
import com.example.databook.R
import com.example.databook.livro.LivroAdapter
import com.example.databook.livro.LivroFavAdapter
import com.example.databook.livro.RegistrarLivroActivity
import com.example.databook.database.favoritos.FavoritosEntity
import com.example.databook.database.perfil.PerfilEntity
import com.example.databook.database.perfil.PerfisViewModel
import com.example.databook.entities.Item
import com.example.databook.services.MainViewModel
import com.example.databook.services.service
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home_favoritos.view.*
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class HomeFragment : Fragment(), LivroAdapter.OnLivroClickListener,
    LivroFavAdapter.OnLivroFavClickListener {
    private val mAuth = FirebaseAuth.getInstance().currentUser

    private lateinit var viewModelFav: FavoritosViewModel
    private lateinit var viewModelPerfil: PerfisViewModel
    private lateinit var listLivro: List<Item>

    private var listFavs = listOf<FavoritosEntity>()
    private var favBoolean: Boolean? = null

    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(service) as T
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) favBoolean = arguments?.getBoolean(favoritos)


    }

    companion object {
        private const val favoritos = "favoritos"

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
        val view = inflater.inflate(R.layout.fragment_home_favoritos, container, false)
        lifecycleScope.launch {
            viewModelPerfil.addPerfil(
                PerfilEntity(
                    mAuth!!.uid,
                    getBitmap(mAuth.photoUrl.toString()),
                    mAuth.displayName.toString(),
                    mAuth.email.toString(),
                    0,
                    0,
                    ""
                )
            )
        }
        view.fb_addBook.setOnClickListener {
            iniciarTelaRegistro()
        }
        if (favBoolean == false) {
            view.textInputPesquisa.setEndIconOnClickListener {
                callResultsSearch(view)
            }
            view.textInputPesquisa.editText?.doOnTextChanged { _, _, _, _ ->
                callResultsSearch(view)
            }
        } else {
            callResultFavs(view)
            view.textInputPesquisa.setEndIconOnClickListener {
                callResultsSearchFav(view)
            }
            view.textInputPesquisa.editText?.doOnTextChanged { _, _, _, _ ->
                callResultsSearchFav(view)
            }
        }
        return view

    }

    private fun iniciarTelaRegistro() {
        val intent = Intent(activity, RegistrarLivroActivity::class.java)
        intent.putExtra("edit", false)
        startActivity(intent)
    }

    private fun callResultsSearch(view: View) {
        val searchText = view.textInputPesquisa.editText?.text.toString()
        if (searchText != "") {
            viewModel.getSearch(searchText)
            viewModel.returnSearchList.observe(viewLifecycleOwner) {
                listLivro = it.items
                view.rv_result.layoutManager =
                    GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
                view.rv_result.setHasFixedSize(true)
                val livroAdapter = LivroAdapter(this)
                livroAdapter.setData(listLivro)
                view.rv_result.adapter = livroAdapter
            }
        }
    }


    private fun callResultFavs(view: View) {
        val listResult = viewModelFav.getListFavUserId(mAuth!!.uid)
        listResult.observeForever {
            listFavs = it
            view.rv_result.layoutManager =
                GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
            view.rv_result.setHasFixedSize(true)
            val adapter = LivroFavAdapter(this)
            adapter.setData(listFavs)
            view.rv_result.adapter = adapter
        }
        viewModelPerfil.perfilList.observe(viewLifecycleOwner) { it ->
            it.forEach {
                if (it.userID == mAuth.uid) {
                    val perfilAtual = it
                    perfilAtual.countFavoritos = listFavs.size
                    viewModelPerfil.updatePerfil(perfilAtual)
                }
            }
        }
    }

    private fun callResultsSearchFav(view: View) {
        val searchText = view.textInputPesquisa.editText?.text.toString()
        if (searchText != "") {
            val listResult = viewModelFav.getseacrhListFav(searchText)
            listResult.observe(viewLifecycleOwner) {
                listFavs = it
                view.rv_result.layoutManager =
                    GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
                view.rv_result.setHasFixedSize(true)
                val adapter = LivroFavAdapter(this)
                adapter.setData(listFavs)
                view.rv_result.adapter = adapter
            }
        } else if (searchText == "") {
            callResultFavs(view)
        }
    }


    override fun livroClick(position: Int) {
        val book = listLivro[position]
        val intent = Intent(context, LivroSelecionadoActivity::class.java)
        val adapter = LivroAdapter(this)
        intent.putExtra("bookApi", book)
        intent.putExtra("favoritos", false)
        adapter.notifyDataSetChanged()
        startActivity(intent)
    }

    override fun livroFavClick(position: Int) {
        val intent = Intent(activity, LivroSelecionadoActivity::class.java)
        val adapter = LivroFavAdapter(this)
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




