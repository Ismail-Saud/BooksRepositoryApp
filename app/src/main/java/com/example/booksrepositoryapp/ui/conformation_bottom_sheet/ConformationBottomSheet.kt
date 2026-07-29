package com.example.booksrepositoryapp.ui.conformation_bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.booksrepositoryapp.databinding.BottomSheetConformationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmationBottomSheet(
    private val title: String,
    private val message: String,
    private val positiveButtonText: String,
    private val onConfirm: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetConformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetConformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.titleText.text = title
        binding.messageText.text = message
        binding.btnPositive.text = positiveButtonText

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        binding.btnPositive.setOnClickListener {
            onConfirm()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}