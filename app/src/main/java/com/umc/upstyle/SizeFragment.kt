package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexboxLayout
import com.umc.upstyle.databinding.ActivitySizeBinding

class SizeFragment : Fragment() {

    private var _binding: ActivitySizeBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory: String? = null
    private var selectedSubCategory: String? = null
    private var selectedFit: String? = null
    private var selectedSize: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = ActivitySizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이전 Fragment에서 전달된 데이터 수신
        arguments?.let {
            selectedCategory = it.getString("CATEGORY")
            selectedSubCategory = it.getString("SUB_CATEGORY")
            selectedFit = it.getString("FIT")
        }

        // 옷 종류에 대한 설명
        binding.mainTitleTextView.text = selectedCategory

        binding.titleTextView.text = "사이즈를 선택해주세요!"

        setupSizeOptions()

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.nextButton.setOnClickListener {
            navigateToNextStep()
        }
    }

    private fun setupSizeOptions() {
        val options = listOf("스몰", "미디엄", "라지")
        createButtons(binding.optionsLayout, options) { selectedOption ->
            selectedSize = selectedOption
            binding.nextButton.setBackgroundResource(R.drawable.next_on)
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
                    layout.children.forEach { (it as Button).background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background)
                    }
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
        if (selectedSize.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "사이즈를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 다음 Fragment로 데이터 전달 및 전환
        val nextFragment = ColorFragment().apply {
            arguments = Bundle().apply {
                putString("CATEGORY", selectedCategory)
                putString("SUB_CATEGORY", selectedSubCategory)
                putString("FIT", selectedFit)
                putString("SIZE", selectedSize)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nextFragment) // R.id.fragment_container는 FrameLayout ID
            .addToBackStack(null) // 뒤로 가기 지원
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
