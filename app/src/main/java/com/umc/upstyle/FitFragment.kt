package com.umc.upstyle

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexboxLayout
import com.umc.upstyle.databinding.ActivityFitBinding

class FitFragment : Fragment(R.layout.activity_fit) {

    private var _binding: ActivityFitBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory: String? = null
    private var selectedSubCategory: String? = null
    private var selectedFit: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = ActivityFitBinding.bind(view)  // 바인딩 초기화

        val category = arguments?.getString("CATEGORY")
        val subCategory = arguments?.getString("SUB_CATEGORY")

        println("DEBUG: Received CATEGORY=$category, SUB_CATEGORY=$subCategory")

        // 뒤로가기 버튼 클릭 이벤트 설정
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        // 이전 Fragment에서 전달된 데이터 수신
        selectedCategory = arguments?.getString("CATEGORY")
        selectedSubCategory = arguments?.getString("SUB_CATEGORY")

        // 옷 종류에 대한 설명
        binding.mainTitleTextView.text = "$selectedCategory"

        // Title 설정
        binding.titleTextView.text = "핏/사이즈를 선택해주세요!"

        // 핏 선택 옵션 설정
        setupFitOptions()

        // 다음 버튼 클릭 시 처리
        binding.nextButton.setOnClickListener {
            navigateToNextStep()
        }
    }

    private fun setupFitOptions() {
        val options = listOf("슬림핏", "레귤러핏", "오버핏")
        createButtons(binding.optionsLayout, options) { selectedOption ->
            selectedFit = selectedOption
            binding.nextButton.setBackgroundResource(R.drawable.next_on) // 버튼 활성화 상태로 변경
        }
    }

    private fun createButtons(layout: FlexboxLayout, options: List<String>, onClick: (String) -> Unit) {
        layout.removeAllViews()

        options.forEach { option ->
            val button = Button(requireContext()).apply {
                text = option
                setTypeface(null, android.graphics.Typeface.BOLD)
                background = ContextCompat.getDrawable(requireContext(), R.drawable.button_background)
                textSize = 14f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                setPadding(40, 10, 40, 10)

                setOnClickListener {
                    // 다른 버튼의 배경을 초기화
                    layout.children.forEach { (it as Button).background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background)
                    }
                    // 선택된 버튼 배경 변경
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.button_background_pressed)
                    onClick(option)
                }
            }

            val layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                100
            ).apply {
                setMargins(16, 16, 16, 16)
            }

            layout.addView(button, layoutParams)
        }
    }

    private fun navigateToNextStep() {
        if (selectedCategory.isNullOrEmpty() || selectedSubCategory.isNullOrEmpty() || selectedFit.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "필수 값을 모두 선택해주세요.", Toast.LENGTH_SHORT).show()
            println("DEBUG: category=$selectedCategory, subCategory=$selectedSubCategory, fit=$selectedFit")
            return
        }

        // 데이터를 Bundle에 추가
        val bundle = Bundle().apply {
            putString("CATEGORY", selectedCategory)
            putString("SUB_CATEGORY", selectedSubCategory)
            putString("FIT", selectedFit)
        }

        val action = R.id.action_fitFragment_to_colorFragment

        // 네비게이션 액션 수행
        findNavController().navigate(action, bundle)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 메모리 누수 방지
    }
}
