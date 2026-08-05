package com.example.booksrepositoryapp.ui.cart_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booksrepositoryapp.databinding.ItemAddressShimmerBinding
import com.example.booksrepositoryapp.databinding.ItemCartShimmerBinding

class AddToCartShimmerAdapter(private val count: Int = 5) : RecyclerView.Adapter<AddToCartShimmerAdapter.ShimmerViewHolder>() {
    inner class ShimmerViewHolder(
        private val binding: ItemCartShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.shimmerLayout.startShimmer()
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShimmerViewHolder {
        val binding = ItemCartShimmerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShimmerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = count
}