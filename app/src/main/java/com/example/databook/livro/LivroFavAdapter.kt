package com.example.databook.livro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.databook.R
import com.example.databook.database.favoritos.FavoritosEntity
import kotlinx.android.synthetic.main.item_capa.view.*

class LivroFavAdapter(val listener: OnLivroFavClickListener) : RecyclerView.Adapter<LivroFavAdapter.ViewHolder>() {

    private var fav = emptyList<FavoritosEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_capa, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = fav[position]
        holder.tvTitulo.text = result.title
        holder.ivLivro.load(result.imagem)
    }

    fun setData(fav: List<FavoritosEntity>){
        this.fav = fav
        notifyDataSetChanged()
    }

    override fun getItemCount() = fav.size

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
