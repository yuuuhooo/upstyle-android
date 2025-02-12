package com.umc.upstyle

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.upstyle.data.model.AdditionalInfoRequestDTO
import com.umc.upstyle.data.model.ApiResponse
import com.umc.upstyle.data.model.Gender
import com.umc.upstyle.data.model.UserInfoDTO
import com.umc.upstyle.data.network.UserApiService
import com.umc.upstyle.databinding.ActivityBodyinfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BodyinfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBodyinfoBinding

    private var isHeightEntered = false
    private var isWeightEntered = false
    private var isGenderSelected = false
    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBodyinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etBodyinfoHeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isHeightEntered = !s.isNullOrEmpty()
                Log.d("UserInfo", "📝 키 입력 감지 -> 입력 상태: $isHeightEntered | 입력 값: ${s.toString().trim()}")
                updateStartButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etBodyinfoWeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isWeightEntered = !s.isNullOrEmpty()
                Log.d("UserInfo", "키 입력 감지 -> 입력 상태: $isWeightEntered | 입력 값: ${s.toString().trim()}")
                updateStartButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 성별 버튼 클릭 이벤트 추가
        binding.btnBodyinfoMale.setOnClickListener {
            isGenderSelected = true
            selectedGender = "MALE"
            Log.d("UserInfo", "성별 선택됨: 남성")
            updateGenderUI(true)
        }

        binding.btnBodyinfoFemale.setOnClickListener {
            isGenderSelected = true
            selectedGender = "FEMALE"
            Log.d("UserInfo", "성별 선택됨: 여성")
            updateGenderUI(false)
        }

        // 시작하기 버튼 이벤트 추가
        binding.btBodyinfoStart.setOnClickListener {
            val heightText = binding.etBodyinfoHeight.text?.toString()?.trim()
            val weightText = binding.etBodyinfoWeight.text?.toString()?.trim()

            val height = heightText?.toDoubleOrNull() ?: -1.0
            val weight = weightText?.toDoubleOrNull() ?: -1.0
            val gender = selectedGender ?: "NONE"

            Log.d("UserInfo", "버튼 클릭됨 - 키: $height cm, 몸무게: $weight kg, 성별: $gender")

            // 입력값이 -1.0이면 디버깅 로그 추가
            if (height == -1.0 || weight == -1.0) {
                Log.e("UserInfo", "입력된 키 또는 몸무게가 올바르지 않습니다!")
            }

            sendUserInfoToServer()
        }

    }

    // 성별 UI 업데이트
    private fun updateGenderUI(isMale: Boolean) {
        binding.btnBodyinfoMale.isSelected = isMale
        binding.btnBodyinfoFemale.isSelected = !isMale
        binding.btnBodyinfoMale.setBackgroundResource(
            if (isMale) R.drawable.button_background_gender_checked else R.drawable.button_background_gender_unchecked
        )
        binding.btnBodyinfoFemale.setBackgroundResource(
            if (!isMale) R.drawable.button_background_gender_checked else R.drawable.button_background_gender_unchecked
        )
        updateStartButtonState()
    }

    // 버튼 활성화 상태 업데이트
    private fun updateStartButtonState() {
        Log.d("UserInfo", "현재 입력 상태 -> 키: $isHeightEntered, 몸무게: $isWeightEntered, 성별: $isGenderSelected")

        val isButtonEnabled = isHeightEntered && isWeightEntered && isGenderSelected
        binding.btBodyinfoStart.isEnabled = isButtonEnabled
        binding.btBodyinfoStart.isClickable = isButtonEnabled
    }

    // 사용자 추가 정보 서버로 전송 (JWT 포함)
    private fun sendUserInfoToServer() {
        val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
        val jwtToken = sharedPref.getString("jwt_token", null)

        if (jwtToken.isNullOrEmpty()) {
            Log.e("UserInfo", "JWT 없음: 로그인이 필요합니다.")
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val height = binding.etBodyinfoHeight.text.toString().toDoubleOrNull() ?: 0.0
        val weight = binding.etBodyinfoWeight.text.toString().toDoubleOrNull() ?: 0.0
        val nickname = "user_${(1000..9999).random()}"

        val gender = try {
            Gender.valueOf(selectedGender ?: "NONE")
        } catch (e: IllegalArgumentException) {
            Gender.NONE
        }

        val request = AdditionalInfoRequestDTO(nickname, gender, height, weight)
        val userApiService = RetrofitClient.createService(UserApiService::class.java)

        Log.d("UserInfo", "추가 정보 요청: JWT=$jwtToken, Request=$request")

        userApiService.addAdditionalInfo("Bearer $jwtToken", request)
            .enqueue(object : Callback<ApiResponse<UserInfoDTO>> {
                override fun onResponse(call: Call<ApiResponse<UserInfoDTO>>, response: Response<ApiResponse<UserInfoDTO>>) {
                    if (response.isSuccessful) {
                        Log.d("UserInfo", "추가 정보 저장 성공: ${response.body()}")
                        navigateToMainActivity()
                    } else {
                        Log.e("UserInfo", "추가 정보 저장 실패: ${response.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<ApiResponse<UserInfoDTO>>, t: Throwable) {
                    Log.e("UserInfo", "API 요청 실패: ${t.message}")
                }
            })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
