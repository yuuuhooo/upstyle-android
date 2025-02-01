package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.umc.upstyle.databinding.ActivityClosetBinding

class ClosetFragment : Fragment() {
    private var _binding: ActivityClosetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityClosetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.mainFragment) // 이전 Fragment로 이동
        }

        binding.btnGoOuter.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "OUTER")
            findNavController().navigate(action)
        }

        binding.btnGoTop.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "TOP")
            findNavController().navigate(action)
        }

        binding.btnGoBottom.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "BOTTOM")
            findNavController().navigate(action)
        }

        binding.btnGoShoes.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "SHOES")
            findNavController().navigate(action)
        }

        binding.btnGoBag.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "BAG")
            findNavController().navigate(action)
        }

        binding.btnGoOther.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "OTHER")
            findNavController().navigate(action)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ViewBinding 해제
    }
}
