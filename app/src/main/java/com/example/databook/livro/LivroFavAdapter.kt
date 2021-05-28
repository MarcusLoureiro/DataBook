package com.example.databook.livro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.databook.R
import com.example.databook.dataBase.Favoritos.FavoritosEntity
import kotlinx.android.synthetic.main.item_capa.view.*

class LivroFavAdapter(val listener: OnLivroFavClickListener) : RecyclerView.Adapter<LivroFavAdapter.ViewHolder>() {

    private var fav = emptyList<FavoritosEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_capa, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var result = fav[position]
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