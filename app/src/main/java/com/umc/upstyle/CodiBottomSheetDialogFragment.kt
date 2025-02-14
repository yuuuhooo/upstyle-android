package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.upstyle.databinding.FragmentCodiBottomSheetDialogBinding

class CodiBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCodiBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCodiBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "CodiBottomModalSheet"
    }
}
