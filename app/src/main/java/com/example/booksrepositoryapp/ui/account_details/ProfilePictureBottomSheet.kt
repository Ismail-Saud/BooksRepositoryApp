package com.example.booksrepositoryapp.ui.account_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.booksrepositoryapp.databinding.BottomSheetProfilePictureBinding
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfilePictureBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetProfilePictureBinding? = null
    private val binding get() = _binding!!

    var listener: OnPictureOptionSelected? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetProfilePictureBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCloseBtn()
        setupButtons()
    }

    private fun setupCloseBtn() {
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun setupButtons() {
        binding.btnTakeImage.setOnClickListener {
            listener?.onCameraClicked()
            dismiss()
        }

        binding.btnSelectImage.setOnClickListener {
            listener?.onGalleryClicked()
            dismiss()
        }

        binding.btnRemoveImage.setOnClickListener {
            dismiss()
            ConfirmationBottomSheet(
                title = "Remove Profile Picture?",
                message = "Are you sure you want to remove your profile picture? \n This action cannot be undone",
                positiveButtonText = "Remove"
            ) {
                listener?.onRemovePictureClicked()
            }.show(parentFragmentManager, "Confirmation")
        }

        val showRemove = arguments?.getBoolean("showRemove", false) ?: false
        binding.btnRemoveImage.isVisible = showRemove
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}