package com.example.databook.livro

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.example.databook.R
import com.example.isbm.Entities.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_capa.view.*
import java.lang.Exception

class LivroAdapter(
    private val listLivros: List<Item>,
    val listener: OnLivroClickListener
) : RecyclerView.Adapter<LivroAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_capa, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var result = listLivros[position].volumeInfo
        holder.itemView.tag = position
        holder.tvTitulo.text = result.title
        holder.ivLivro.load(R.drawable.sem_imagem)
//        if(result.imageLinks.thumbnail.isNullOrBlank() == false){
//            Glide.with(holder.itemView)
//                .load(result.imageLinks.thumbnail)
//                .error(R.drawable.sem_imagem)
//                .centerCrop()
//                .placeholder(R.drawable.sem_imagem)
//                .into(holder.ivLivro)
//            Log.i("IMAGEM", result.imageLinks.thumbnail)
//        }

        holder.ivLivro.setOnClickListener(holder)

    }


    override fun getItemCount() = listLivros.size

    interface OnLivroClickListener {
        fun livroClick(position: Int)

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvTitulo = view.mediaName!!
        val ivLivro = view.mediaImage!!

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (RecyclerView.NO_POSITION != position) {
                listener.livroClick(position)
            }
        }
    }
}