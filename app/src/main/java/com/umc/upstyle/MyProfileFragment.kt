package com.umc.upstyle

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.upstyle.databinding.FragmentMyProfileBinding

class MyProfileFragment : Fragment(R.layout.fragment_my_profile) {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    private val existingNicknames = listOf("apple", "test") // 중복 검사 리스트

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyProfileBinding.bind(view)  // 바인딩 초기화

        // 닉네임 불러오기
        loadNickname()

        binding.backButton.setOnClickListener {
            saveNickname()  // 뒤로 가기 전에 닉네임 저장
            findNavController().navigateUp()
        }

        // EditText에 입력하면 ImageView가 사라지도록 설정
        binding.nicknameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateNickname(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        setupNicknameValidation()
    }

    private fun setupNicknameValidation() {
        binding.nicknameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateNickname(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
// namechkimg
    private fun validateNickname(nickname: String) {
        val nicknamePattern = "^[가-힣a-zA-Z0-9]+$".toRegex()

        when {
            nickname.isEmpty() -> {
                binding.errorTextView.text = ""
                binding.nicknameEditText.setTextColor(Color.WHITE)
                binding.namechkimg.visibility = View.VISIBLE  // 입력 없으면 다시 보이게
                binding.namechkimg.setImageResource(R.drawable.ic_namechk_white)
            }
            nickname.length > 8 -> {
                binding.errorTextView.text = "최대 8자까지 입력 가능합니다."
                binding.errorTextView.setTextColor(Color.RED)
                binding.nicknameEditText.setTextColor(Color.RED)  // 입력 텍스트 빨간색 변경
                binding.namechkimg.setImageResource(R.drawable.ic_namechk_red)
                binding.namechkimg.visibility = View.VISIBLE
            }
            !nicknamePattern.matches(nickname) -> {
                binding.errorTextView.text = "한글, 영문, 숫자만 입력 가능합니다."
                binding.errorTextView.setTextColor(Color.RED)
                binding.nicknameEditText.setTextColor(Color.RED)  // 입력 텍스트 빨간색 변경
                binding.namechkimg.setImageResource(R.drawable.ic_namechk_red)
                binding.namechkimg.visibility = View.VISIBLE
            }
            existingNicknames.contains(nickname) -> {
                binding.errorTextView.text = "이미 사용 중인 닉네임입니다."
                binding.errorTextView.setTextColor(Color.RED)
                binding.nicknameEditText.setTextColor(Color.RED)  // 입력 텍스트 빨간색 변경
                binding.namechkimg.setImageResource(R.drawable.ic_namechk_red)
                binding.namechkimg.visibility = View.VISIBLE
            }
            else -> {
                binding.errorTextView.text = ""
                binding.nicknameEditText.setTextColor(Color.WHITE)  // 입력 텍스트 흰색 유지
                binding.namechkimg.setImageResource(R.drawable.ic_namechk_white)
                binding.namechkimg.visibility = View.VISIBLE
            }
        }
    }

    // 닉네임 저장 함수
    private fun saveNickname() {
        val nickname = binding.nicknameEditText.text.toString()
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("nickname", nickname) // 닉네임 저장
            apply()
        }
    }

    // 닉네임 불러오기 함수
    private fun loadNickname() {
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val savedNickname = sharedPref.getString("nickname", "") // 저장된 값 불러오기
        binding.nicknameEditText.setText(savedNickname) // EditText에 설정
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 메모리 누수 방지
    }
}
