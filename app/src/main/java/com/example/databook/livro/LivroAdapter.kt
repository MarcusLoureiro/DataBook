package com.example.databook.livro

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.databook.R
import com.example.isbm.Entities.ImageLinks
import com.example.isbm.Entities.Item
import com.example.isbm.Entities.VolumeInfo
import kotlinx.android.synthetic.main.item_capa.view.*
import org.json.JSONObject


class LivroAdapter(val listener: OnLivroClickListener, val context: Context) : RecyclerView.Adapter<LivroAdapter.ViewHolder>() {
    private var listLivros = emptyList<Item>()
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
        if(result.imageLinks.thumbnail != null ){
            Glide.with(holder.itemView)
                .load(result.imageLinks.thumbnail)
                .error(R.drawable.sem_imagem)
                .centerCrop()
                .into(holder.ivLivro)
            Log.i("IMAGEM", result.imageLinks.thumbnail)
        }

        holder.ivLivro.setOnClickListener(holder)

    }


    fun setData(listLivros: List<Item>){
        this.listLivros = listLivros
        notifyDataSetChanged()
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