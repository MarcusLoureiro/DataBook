package com.example.databook.livro

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.databook.R
import com.example.desafiofirebase.entities.Livro
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.example.isbm.Entities.Books
import com.example.isbm.Entities.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_capa.view.*

class LivroFavAdapter(
    private val listFavLivros: List<FavoritosEntity>,
    val listener: OnLivroFavClickListener
) : RecyclerView.Adapter<LivroFavAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_capa, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var result = listFavLivros[position]
        holder.itemView.tag = position
        holder.tvTitulo.text = result.title
//        var img = result.imageLinks.smallThumbnail
//        if(img != "" && img != null){
//            Picasso.get().load(img).into(holder.ivLivro)
//        }else{
//      holder.ivLivro.setImageURI(result.imagem.toUri())
//        }

//        holder.ivLivro.setOnClickListener {
//            val intent = Intent(holder.itemView.context, LivroDetailsActivity::class.java)
//            intent.putExtra("name", listLivros[position].name)
//            intent.putExtra("lancamento", listLivros[position].data)
//            intent.putExtra("descricao", listLivros[position].description)
//            intent.putExtra("url", listLivros[position].URL)
//            intent.putExtra("key", listLivros[position].id)
//            holder.itemView.context.startActivity(intent)
//        }
        holder.ivLivro.setOnClickListener(holder)

    }

    override fun getItemCount() = listFavLivros.size

    interface OnLivroFavClickListener {
        fun livroFavClick(position: Int)

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvTitulo = view.mediaName!!
        val ivLivro = view.mediaImage!!

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?){
            val position = absoluteAdapterPosition
            if(RecyclerView.NO_POSITION != position){
                listener.livroFavClick(position)
            }
        }
    }
}