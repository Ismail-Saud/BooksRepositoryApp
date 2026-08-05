package com.example.booksrepositoryapp.ui.address_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import com.example.booksrepositoryapp.databinding.ItemAddressBinding

class AddressAdapter(
    private val onLocationClick: (AddressModel) -> Unit,
    private val onCheckClick: (AddressModel, String) -> Unit,
    private val onDeleteClick: (AddressModel) -> Unit
) : ListAdapter<AddressModel, AddressAdapter.AddressViewHolder>(DiffCallback()) {
    inner class AddressViewHolder(
        private val binding: ItemAddressBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddressModel) {
            binding.etAddress.setText(item.fullAddress)
            binding.btnCurrentLocation.setOnClickListener {
                onLocationClick(item)
            }
            binding.btnSaveAddress.setOnClickListener {
                val fullAddress = binding.etAddress.text.toString().trim()
                onCheckClick(item, fullAddress)
            }
            binding.btnDeleteAddress.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressViewHolder {

        val binding = ItemAddressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return AddressViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: AddressViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


    class DiffCallback : DiffUtil.ItemCallback<AddressModel>() {
        override fun areItemsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
            return oldItem == newItem
        }
    }
}