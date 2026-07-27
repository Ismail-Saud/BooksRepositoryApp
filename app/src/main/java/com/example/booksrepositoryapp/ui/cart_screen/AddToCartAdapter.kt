package com.example.booksrepositoryapp.ui.cart_screen

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
import com.example.booksrepositoryapp.data.local.uiModels.CartItem
import com.example.booksrepositoryapp.databinding.ItemCartBinding

class CartAdapter(
    private val onIncreaseClick: (CartItem) -> Unit,
    private val onDecreaseClick: (CartItem) -> Unit,
    private val onRemoveClick: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(DiffCallback()) {
    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: CartItem) {
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
            Glide.with(binding.root.context)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: com.bumptech.glide.request.target.Target<Drawable?>,
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

    class DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.cartId == newItem.cartId
        }
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}