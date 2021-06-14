package com.example.databook.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.databook.database.favoritos.FavoritosViewModel
import com.example.databook.R
import com.example.databook.database.favoritos.FavoritosEntity
import com.example.databook.database.perfil.PerfilEntity
import com.example.databook.database.perfil.PerfisViewModel
import com.example.databook.entities.Books
import com.example.databook.entities.Item
import com.example.databook.livro.*
import com.example.databook.services.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.custom_alert.view.*
import kotlinx.android.synthetic.main.custom_alert_delete.view.*
import kotlinx.android.synthetic.main.fragment_home_favoritos.view.*
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class HomeFragment : Fragment(), MainAdapter.OnLivroClickListener,
    LivroFavAdapter.OnLivroFavClickListener {
    private val mAuth = FirebaseAuth.getInstance().currentUser

    private lateinit var viewModelFav: FavoritosViewModel
    private lateinit var viewModelPerfil: PerfisViewModel
    private var listLivro: List<Item> = listOf()

    private var listFavs = listOf<FavoritosEntity>()
    private var favBoolean: Boolean? = null
    private var favDeleteBoolean: Boolean? = null
    private var favPosition: Int? = null


    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter


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
        if (favDeleteBoolean == true) {
            viewModelFav.favList.observe(viewLifecycleOwner) {
                viewModelFav.deleteFav(it[favPosition!!])
                val adapter = LivroFavAdapter(this)
                adapter.notifyDataSetChanged()
                favDeleteBoolean = false
            }
        }


        val view = inflater.inflate(R.layout.fragment_home_favoritos, container, false)
        view.progressBar.visibility = View.GONE
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

        setupViewModel()
        setupUI(view)


        view.fb_addBook.setOnClickListener {
            iniciarTelaRegistro()
        }
        if (favBoolean == false) {
            view.textInputPesquisa.setEndIconOnClickListener {
                Log.i("termo pesquisa", view.textInputPesquisa.editText?.text.toString())
                setupObservers(view.textInputPesquisa.editText?.text.toString(), view)
            }
            view.textInputPesquisa.editText?.doOnTextChanged { _, _, _, _ ->
                Log.i("termo pesquisa", view.textInputPesquisa.editText?.text.toString())
                setupObservers(view.textInputPesquisa.editText?.text.toString(), view)
            }
        } else {
            view.progressBar.visibility = View.GONE
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
        listLivro.forEach {
            Log.i("title test", book.volumeInfo.title.toString())
        }
        val intent = Intent(context, LivroSelecionadoActivity::class.java)
        val adapter = MainAdapter(this)
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

    override fun livroFavLongClick(position: Int) {
        Log.i("LONG", "entrou no onLongClick")
        val adapter = LivroFavAdapter(this)
        createAlert(listFavs[position])
        adapter.notifyDataSetChanged()
    }

    private suspend fun getBitmap(data: String): Bitmap {
        val loanding = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(data)
            .build()

        val result = (loanding.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.service))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI(view: View) {
        adapter = MainAdapter(this)
        view.rv_result.layoutManager =
            GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        view.rv_result.setHasFixedSize(true)
        view.rv_result.adapter = adapter
    }

    private fun setupObservers(string: String, view: View) {
        if (string != "") {
            viewModel.getSearch(string).observe(viewLifecycleOwner) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            view.rv_result.visibility = View.VISIBLE
                            view.progressBar.visibility = View.GONE
                            resource.data?.let { Books -> retrieveList(listOf(Books)) }
                        }
                        Status.ERROR -> {
                            view.rv_result.visibility = View.VISIBLE
                            view.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            view.progressBar.visibility = View.VISIBLE
                            view.rv_result.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun retrieveList(Books: List<Books>) {
        listLivro = Books[0].items
        adapter.apply {
            addBooks(Books[0].items)
            notifyDataSetChanged()
        }
    }

    private fun createAlert(fav:FavoritosEntity) {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded).create()
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.custom_alert_delete, null)
        builder.setView(view)
        builder.show()
        view.btAlert_sim.setOnClickListener {
            viewModelFav.deleteFav(fav)
            builder.dismiss()
        }
        view.btAlert_nao.setOnClickListener {
            builder.dismiss()
        }
    }
}




