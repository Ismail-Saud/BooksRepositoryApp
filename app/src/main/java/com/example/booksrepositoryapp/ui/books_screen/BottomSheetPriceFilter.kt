package com.example.booksrepositoryapp.ui.books_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.BottomSheetConformationBinding
import com.example.booksrepositoryapp.databinding.BottomSheetPriceFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetPriceFilter(
    private val onApply: (Int, Int) -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetPriceFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetPriceFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.priceSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            val min = values[0].toInt()
            val max = values[1].toInt()
            binding.tvPriceRange.text = "Price: $ $min - $ $max"
        }
        binding.applyFilter.setOnClickListener {
            val values = binding.priceSlider.values
            if(values.size >= 2){
                onApply(
                    values[0].toInt(),
                    values[1].toInt()
                )
            }
            dismiss()
        }
        binding.resetFilter.setOnClickListener {
            binding.priceSlider.values = listOf(15f, 36f)
            binding.tvPriceRange.text = "Price: $ 15 - $ 36"
        }
        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}