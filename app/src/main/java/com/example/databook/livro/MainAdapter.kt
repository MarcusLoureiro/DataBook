package com.example.databook.livro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.databook.R
import com.example.databook.entities.Books
import com.example.databook.entities.Item
import com.example.databook.entities.VolumeInfo
import kotlinx.android.synthetic.main.item_capa.view.*

class MainAdapter(val listener: OnLivroClickListener) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {
    private var books = emptyList<Item>()

   inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(book: VolumeInfo) {
            itemView.apply {
                mediaName.text = book.title
                Glide.with(mediaImage.context)
                    .load(book.imageLinks.thumbnail)
                    .error(R.drawable.sem_imagem)
                    .into(mediaImage)
            }
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (RecyclerView.NO_POSITION != position) {
                listener.livroClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_capa, parent, false)
        )

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(books[position].volumeInfo)
    }


    fun addBooks(books: List<Books>) {
        this.books = books[0].items
        notifyDataSetChanged()

    }

    interface OnLivroClickListener {
        fun livroClick(position: Int)

    }


}