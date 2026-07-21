package com.example.booksrepositoryapp.ui.book_category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booksrepositoryapp.data.api.models.Category
import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.SubjectApiResponseModel
import com.example.booksrepositoryapp.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val onClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {
    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind (category: Category) {
            binding.tvCategoryName.text = category.title
            binding.ivCategory.setImageResource(category.imgSrc)
            binding.root.setOnClickListener {
                onClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
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


class DiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.apiValue == newItem.apiValue
    }
    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}