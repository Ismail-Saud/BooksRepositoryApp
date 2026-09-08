package com.example.booksrepositoryapp.ui.addressScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booksrepositoryapp.databinding.ItemAddressBinding

class AddressAdapter(
    private val onLocationClick: (AddressUiModel) -> Unit,
    private val onCheckClick: (AddressUiModel, String) -> Unit,
    private val onDeleteClick: (AddressUiModel) -> Unit
) : ListAdapter<AddressUiModel, AddressAdapter.AddressViewHolder>(DiffCallback()) {

    inner class AddressViewHolder(
        private val binding: ItemAddressBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddressUiModel) {
            binding.etAddress.setText(item.address.fullAddress)
            val isLoading = item.isFetchingLocation || item.isSaving

            binding.btnCurrentLocation.isEnabled = !isLoading
            binding.btnCurrentLocation.alpha = if (isLoading) 0.5f else 1f
            binding.btnCurrentLocation.visibility = if (item.isFetchingLocation) View.INVISIBLE else View.VISIBLE
            binding.progressLocation.visibility = if (item.isFetchingLocation) View.VISIBLE else View.GONE

            binding.btnSaveAddress.isEnabled = !isLoading
            binding.btnSaveAddress.alpha = if (isLoading) 0.5f else 1f
            binding.btnSaveAddress.visibility = if (item.isSaving) View.INVISIBLE else View.VISIBLE
            binding.progressSave.visibility = if (item.isSaving) View.VISIBLE else View.GONE

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<AddressUiModel>() {
        override fun areItemsTheSame(oldItem: AddressUiModel, newItem: AddressUiModel): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: AddressUiModel, newItem: AddressUiModel): Boolean {
            return oldItem == newItem
        }
    }
}
