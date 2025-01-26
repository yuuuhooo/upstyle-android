package com.umc.upstyle

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.umc.upstyle.databinding.ActivityBodyinfoBinding

class BodyinfoActivity : AppCompatActivity() {

    // ViewBinding 객체 선언
    private lateinit var binding: ActivityBodyinfoBinding

    // 입력 상태를 저장하는 변수들
    private var isHeightEntered = false
    private var isWeightEntered = false
    private var isGenderSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityBodyinfoBinding.inflate(layoutInflater)

        // setContentView()로 ViewBinding의 root 레이아웃을 설정
        setContentView(binding.root)

        // EditText 상태를 감지
        binding.tilBodyinfoHeight.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isHeightEntered = !s.isNullOrEmpty()
                updateStartButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.tilBodyinfoWeight.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isWeightEntered = !s.isNullOrEmpty()
                updateStartButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 성별 버튼 클릭 시 상태 변경
        binding.btnBodyinfoMale.setOnClickListener {
            isGenderSelected = true
            binding.btnBodyinfoMale.isSelected = true
            binding.btnBodyinfoFemale.isSelected = false
            binding.btnBodyinfoMale.setBackgroundResource(R.drawable.button_background_gender_checked)
            binding.btnBodyinfoFemale.setBackgroundResource(R.drawable.button_background_gender_unchecked)
            updateStartButtonState()
        }

        binding.btnBodyinfoFemale.setOnClickListener {
            isGenderSelected = true
            binding.btnBodyinfoFemale.isSelected = true
            binding.btnBodyinfoMale.isSelected = false
            binding.btnBodyinfoFemale.setBackgroundResource(R.drawable.button_background_gender_checked)
            binding.btnBodyinfoMale.setBackgroundResource(R.drawable.button_background_gender_unchecked)
            updateStartButtonState()
        }

        // 시작하기 버튼 이벤트 처리
        binding.btBodyinfoStart.setOnClickListener {
            // Intent 생성
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // MainActivity로 이동
        }
    }

    // 시작하기 버튼 상태 업데이트 함수
    private fun updateStartButtonState() {
        binding.btBodyinfoStart.isEnabled = isHeightEntered && isWeightEntered && isGenderSelected
        binding.btBodyinfoStart.isClickable = isHeightEntered && isWeightEntered && isGenderSelected
    }
}
