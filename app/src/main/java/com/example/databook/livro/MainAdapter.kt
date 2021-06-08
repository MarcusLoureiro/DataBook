package com.example.databook.livro

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.databook.R
import com.example.databook.entities.Item
import com.example.databook.entities.VolumeInfo
import com.example.databook.services.MainViewModel
import kotlinx.android.synthetic.main.item_capa.view.*

class MainAdapter(val listener: OnLivroClickListener) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {
    private var books = emptyList<Item>()
   inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(book: VolumeInfo) {
            itemView.apply {
                mediaName.text = formattingItems(book)
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


    fun addBooks(books: List<Item>) {
        this.books = books
        notifyDataSetChanged()

    }

    interface OnLivroClickListener {
        fun livroClick(position: Int)

    }

    fun formattingItems(item: VolumeInfo): String {
        var newTitle = ""
        while (newTitle.length < 14) {
            Log.i("teste adapter funcition", "entrou")
            if (item.title.length > 15) {
                for (i in 0..14) {
                    if (("${item.title[14]}" == " ") && (i == 14)) {
                        break
                    }
                    newTitle += "${item.title[i]}"
                    Log.i("teste adapter funcition", "$newTitle")
                }
                return "${newTitle}..."
            } else {
                item.title = item.title
                return item.title
            }
        }
        return ""
    }


    fun formattingTitle(list: List<Item>): String {
        list.forEach {
            return if (it.volumeInfo.title.length > 15) {
                var newTitle = ""
                for (i in 0..14) {
                    if (("${it.volumeInfo.title[14]}" == " ") && (i == 14)) {
                        break
                    }
                    newTitle += "${it.volumeInfo.title[i]}"
                }
                it.volumeInfo.title = "$newTitle..."
                newTitle
            } else {
                it.volumeInfo.title = it.volumeInfo.title
                it.volumeInfo.title
            }
        }
        return ""
    }
}





