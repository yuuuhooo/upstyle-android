package com.umc.upstyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.adapters.ViewBindingAdapter.setPadding
import com.google.android.flexbox.FlexboxLayout
import com.umc.upstyle.databinding.ActivityCategoryBinding
import com.umc.upstyle.databinding.FragmentMyProfileBinding


class MyProfileFragment : Fragment(R.layout.fragment_my_profile) {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMyProfileBinding.bind(view)  // 바인딩 초기화

//        binding.myProfileBtn.setOnClickListener {
//            requireActivity().supportFragmentManager.popBackStack()  // 이전 화면으로 돌아가기
//        }

//        val selectedCategory = arguments?.getString("CATEGORY")
//        if (selectedCategory.isNullOrEmpty()) {
//            Toast.makeText(requireContext(), "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show()
//            requireActivity().supportFragmentManager.popBackStack()
//            return
//        }
//
//        binding.mainTitleTextView.text = "$selectedCategory"
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 메모리 누수 방지
    }
}