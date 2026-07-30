package com.example.booksrepositoryapp.ui.loading_screen

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentLoadingBinding

class LoadingFragment : DialogFragment() {
    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentLoadingBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        isCancelable = false
        return dialog
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}