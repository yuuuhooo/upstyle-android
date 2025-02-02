package com.umc.upstyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.upstyle.databinding.ActivityCategoryBinding
import com.umc.upstyle.databinding.FragmentCreateVoteBinding


class CreateVoteFragment : Fragment() {
    private var _binding: FragmentCreateVoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateVoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goBackButton = binding.backButton
        goBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()  // 이전 화면으로 돌아가기
        }
    }
}



