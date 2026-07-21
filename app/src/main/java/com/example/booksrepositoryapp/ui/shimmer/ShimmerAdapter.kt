package com.example.booksrepositoryapp.ui.shimmer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booksrepositoryapp.R

class ShimmerAdapter: RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {
    inner class ShimmerViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_books_shimmer, p0, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(p0: ShimmerAdapter.ShimmerViewHolder, p1: Int) {

    }

    override fun getItemCount(): Int = 12
}