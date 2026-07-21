package com.example.booksrepositoryapp.ui.books_page

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
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.Work
import com.example.booksrepositoryapp.databinding.ItemBooksBinding

class BooksAdapter(
    private val categoryName: String,
    private val onClick: (Work) -> Unit
): ListAdapter<Work, BooksAdapter.CategoryViewHolder>(DiffCallback()){

    inner class CategoryViewHolder(
        private val binding: ItemBooksBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind (books: Work) {
            val imageUrl = "https://covers.openlibrary.org/b/id/${books.cover_id}-M.jpg"
            binding.bookCategory.text = categoryName
            binding.bookTitle.text = books.title
            binding.bookAuthor.text = books.authors.firstOrNull()?.name?.trim()?:"Unknown"
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

class DiffCallback : DiffUtil.ItemCallback<Work>() {
    override fun areItemsTheSame(oldItem: Work, newItem: Work): Boolean {
        return oldItem.key == newItem.key
    }
    override fun areContentsTheSame(oldItem: Work, newItem: Work): Boolean {
        return oldItem == newItem
    }
}