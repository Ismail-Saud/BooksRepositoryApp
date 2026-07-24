package com.example.booksrepositoryapp.ui.books_screen

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.Work
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.databinding.ItemBooksBinding
import kotlin.random.Random

class BooksAdapter(
    private val categoryName: String,
    private val onClick: (BookDetailsModel) -> Unit
): ListAdapter<BookDetailsModel, BooksAdapter.CategoryViewHolder>(DiffCallback()){

    inner class CategoryViewHolder(
        private val binding: ItemBooksBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind (books: BookDetailsModel) {
            val imageUrl = "https://covers.openlibrary.org/b/id/${books.coverId}-M.jpg"
            binding.bookCategory.text = categoryName
            binding.bookTitle.text = books.title
            binding.bookAuthor.text = books.author
            binding.bookPrice.text = "$${books.price}"
            Glide.with(binding.root.context)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: Target<Drawable?>,
                        p3: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }
                    override fun onResourceReady(
                        p0: Drawable,
                        p1: Any,
                        p2: Target<Drawable?>?,
                        p3: DataSource,
                        p4: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(binding.bookImage)
            binding.root.setOnClickListener {
                onClick(books)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CategoryViewHolder {
        val binding = ItemBooksBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(parent: CategoryViewHolder, position: Int) {
        parent.bind(getItem(position))
    }
}

class DiffCallback : DiffUtil.ItemCallback<BookDetailsModel>() {
    override fun areItemsTheSame(oldItem: BookDetailsModel, newItem: BookDetailsModel): Boolean {
        return oldItem.workId == newItem.workId
    }
    override fun areContentsTheSame(oldItem: BookDetailsModel, newItem: BookDetailsModel): Boolean {
        return oldItem == newItem
    }
}