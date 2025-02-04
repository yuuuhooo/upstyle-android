package com.umc.upstyle

import Item_load
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.umc.upstyle.databinding.ActivityClosetBinding
import com.umc.upstyle.databinding.FragmentLoadCategoryBinding

class LoadCategoryFragment : Fragment() {
    private var _binding: FragmentLoadCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoadCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LoadCategoryFragment에서 데이터 받기
        findNavController().currentBackStackEntry?.savedStateHandle?.get<String>("SELECTED_ITEM")?.let { description ->
            findNavController().currentBackStackEntry?.savedStateHandle?.get<String>("SELECTED_ITEM_IMAGE_URL")?.let { imageUrl ->
                findNavController().currentBackStackEntry?.savedStateHandle?.get<String>("CATEGORY")?.let { category ->
                // 이제 description과 imageUrl을 사용해서 필요한 작업을 처리
                val item = Item_load(description, imageUrl) // 아이템 객체 생성
                // 이미지 로드 처리
                Glide.with(requireContext())
                    .load(item.imageUrl)
                    .into(binding.ivTest)

                // CATEGORY 데이터 처리
                binding.tvDescription.text = category
                Toast.makeText(requireContext(), "선택된 카테고리: $category", Toast.LENGTH_SHORT).show()

                binding.tvDescription.text = item.description

                Toast.makeText(requireContext(), "선택된 사진: $item.description", Toast.LENGTH_SHORT).show()

                // 직전 프래그먼트로 데이터 전달하면서 navigateUp
                findNavController().previousBackStackEntry?.savedStateHandle?.set("SELECTED_ITEM", description)
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("SELECTED_ITEM_IMAGE_URL", imageUrl)
                findNavController().previousBackStackEntry?.savedStateHandle?.set("CATEGORY", category)


                findNavController().navigateUp()
                }
            }
        }

        // 날려버리기
        findNavController().previousBackStackEntry?.savedStateHandle?.keys()?.forEach {
            findNavController().previousBackStackEntry?.savedStateHandle?.remove<String>(it)
        }

//        // 전달받은 데이터 확인
//        val selectedItem = arguments?.getString("SELECTED_ITEM")
//        val category = arguments?.getString("CATEGORY")
//
//
//        if (arguments != null) {
//
//        }

//        // 만약 데이터가 전달되었다면
//        if (!selectedItem.isNullOrEmpty() || !category.isNullOrEmpty()) {
//
//
//        }

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

        // 날려버리기
        findNavController().previousBackStackEntry?.savedStateHandle?.keys()?.forEach {
            findNavController().previousBackStackEntry?.savedStateHandle?.remove<String>(it)
        }
        super.onDestroyView()
        _binding = null // ViewBinding 해제
    }
}
