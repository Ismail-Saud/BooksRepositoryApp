package com.example.booksrepositoryapp.ui.addToCart

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
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.databinding.ItemCartBinding

class CartAdapter(
    private val onIncreaseClick: (Cart) -> Unit,
    private val onDecreaseClick: (Cart) -> Unit,
    private val onRemoveClick: (Cart) -> Unit
) : ListAdapter<Cart, CartAdapter.CartViewHolder>(DiffCallback()) {
    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Cart) {
            val imageUrl = "https://covers.openlibrary.org/b/id/${item.coverId}-M.jpg"
            binding.tvBookTitle.text = item.title
            binding.tvAuthor.text = item.author
            binding.tvCategory.text = item.category
            binding.tvQuantity.text = item.quantity.toString()
            binding.tvPrice.text = "$${String.format("%.2f", item.price * item.quantity)}"
            binding.btnIncrease.setOnClickListener {
                onIncreaseClick(item)
            }
            binding.btnDecrease.setOnClickListener {
                onDecreaseClick(item)
            }
            binding.btnRemove.setOnClickListener {
                onRemoveClick(item)
            }
            Log.d("Cart Adapter", "Binding ${item.bookId}")
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

    class DiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.bookId == newItem.bookId
        }
        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }
}
