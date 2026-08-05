package com.example.booksrepositoryapp.ui.address_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booksrepositoryapp.databinding.ItemAddressShimmerBinding

class AddressShimmerAdapter(private val count: Int = 5) : RecyclerView.Adapter<AddressShimmerAdapter.ShimmerViewHolder>() {
    inner class ShimmerViewHolder(
        private val binding: ItemAddressShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.shimmerLayout.startShimmer()
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShimmerViewHolder {
        val binding = ItemAddressShimmerBinding.inflate(
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