package com.umc.upstyle

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.umc.upstyle.databinding.ActivityClosetBinding

class LoadCategoryFragment : Fragment() {
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

        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)

        // 전달받은 데이터 확인
        val selectedItem = arguments?.getString("SELECTED_ITEM")
        val category = arguments?.getString("CATEGORY")

        // 만약 데이터가 전달되었다면
        if (!selectedItem.isNullOrEmpty() || !category.isNullOrEmpty()) {
            // 직전 프래그먼트로 데이터 전달하면서 navigateUp
            findNavController().previousBackStackEntry?.savedStateHandle?.set("SELECTED_ITEM", selectedItem)
            findNavController().previousBackStackEntry?.savedStateHandle?.set("CATEGORY", category)
            findNavController().navigateUp()
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp() // 이전 Fragment로 이동
        }

        binding.btnGoOuter.setOnClickListener {
            val action = LoadCategoryFragmentDirections.actionLoadCategoryFragmentToLoadItemFragment(category = "OUTER")
            findNavController().navigate(action)
        }

        binding.btnGoTop.setOnClickListener {
            val action = LoadCategoryFragmentDirections.actionLoadCategoryFragmentToLoadItemFragment(category = "TOP")
            findNavController().navigate(action)
        }

        binding.btnGoBottom.setOnClickListener {
            val action = LoadCategoryFragmentDirections.actionLoadCategoryFragmentToLoadItemFragment(category = "BOTTOM")
            findNavController().navigate(action)
        }

        binding.btnGoShoes.setOnClickListener {
            val action = LoadCategoryFragmentDirections.actionLoadCategoryFragmentToLoadItemFragment(category = "SHOES")
            findNavController().navigate(action)
        }

        binding.btnGoBag.setOnClickListener {
            val action = LoadCategoryFragmentDirections.actionLoadCategoryFragmentToLoadItemFragment(category = "BAG")
            findNavController().navigate(action)
        }

        binding.btnGoOther.setOnClickListener {
            val action = LoadCategoryFragmentDirections.actionLoadCategoryFragmentToLoadItemFragment(category = "OTHER")
            findNavController().navigate(action)
        }


    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ViewBinding 해제
    }
}
