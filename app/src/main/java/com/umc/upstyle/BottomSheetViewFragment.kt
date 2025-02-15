package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.upstyle.databinding.FragmentBottomSheetViewBinding

class BottomSheetViewFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "CodiBottomModalSheet"

        // newInstance 메서드로 commentId를 넘겨받을 수 있도록 수정
        fun newInstance(commentId: Int): BottomSheetViewFragment {
            val fragment = BottomSheetViewFragment()
            val args = Bundle().apply {
                putInt("commentId", commentId) // commentId를 Bundle에 저장
            }
            fragment.arguments = args
            return fragment
        }
    }
}
