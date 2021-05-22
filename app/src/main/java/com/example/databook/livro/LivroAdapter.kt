package com.example.databook.livro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.databook.R
import com.example.desafiofirebase.entities.Livro
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_capa.view.*

class LivroAdapter(
    private val listLivros: ArrayList<Livro>,
    val listener: OnLivroClickListener
) : RecyclerView.Adapter<LivroAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_capa, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.tvTitulo.text = listLivros[position].titulo
        Picasso.get().load(listLivros[position].URL).into(holder.ivLivro)
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

        override fun onClick(v: View?){
            val position = absoluteAdapterPosition
            if(RecyclerView.NO_POSITION != position){
                listener.livroClick(position)
            }
        }
    }
}