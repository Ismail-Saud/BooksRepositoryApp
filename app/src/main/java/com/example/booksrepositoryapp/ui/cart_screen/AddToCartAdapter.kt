package com.example.booksrepositoryapp.ui.cart_screen

import android.graphics.drawable.Drawable
import android.util.Log
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
import com.example.booksrepositoryapp.data.firebase.firestore.CartModelFB
import com.example.booksrepositoryapp.databinding.ItemCartBinding

class CartAdapter(
    private val onIncreaseClick: (CartModelFB) -> Unit,
    private val onDecreaseClick: (CartModelFB) -> Unit,
    private val onRemoveClick: (CartModelFB) -> Unit
) : ListAdapter<CartModelFB, CartAdapter.CartViewHolder>(DiffCallback()) {
    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: CartModelFB) {
            val imageUrl = "https://covers.openlibrary.org/b/id/${book.coverId}-M.jpg"
            binding.tvBookTitle.text = book.title
            binding.tvAuthor.text = book.author
            binding.tvCategory.text = book.category
            binding.tvQuantity.text = book.quantity.toString()
            binding.tvPrice.text = "$${String.format("%.2f", book.price * book.quantity)}"
            binding.btnIncrease.setOnClickListener {
                onIncreaseClick(book)
            }
            binding.btnDecrease.setOnClickListener {
                onDecreaseClick(book)
            }
            binding.btnRemove.setOnClickListener {
                onRemoveClick(book)
            }
            Log.d("Cart Adapter", "Binding ${book.workId}")
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
                .into(binding.ivBookCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<CartModelFB>() {
        override fun areItemsTheSame(oldItem: CartModelFB, newItem: CartModelFB): Boolean {
            return oldItem.workId == newItem.workId
        }
        override fun areContentsTheSame(oldItem: CartModelFB, newItem: CartModelFB): Boolean {
            return oldItem == newItem
        }
    }
}