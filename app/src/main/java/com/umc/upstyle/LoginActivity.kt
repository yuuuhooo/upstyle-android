package com.umc.upstyle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.umc.upstyle.data.model.AccountInfoDTO
import com.umc.upstyle.data.model.ApiResponse
import com.umc.upstyle.data.model.KakaoJwtDTO
import com.umc.upstyle.data.model.UserInfoDTO
import com.umc.upstyle.data.network.AuthApiService
import com.umc.upstyle.data.network.UserApiService
import com.umc.upstyle.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authApiService = RetrofitClient.createService(AuthApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewBinding 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 자동 로그인 체크
        checkAutoLogin()

        // ✅ 비회원으로 둘러보기
        binding.tvNonMember.setOnClickListener {
            startActivity(Intent(this, BodyinfoActivity::class.java))
        }

        // ✅ 카카오로그인 버튼 클릭 시 로그인 시작
        binding.btGoogleLogin.setOnClickListener {
            startKakaoLogin()
        }
    }

    // ✅ 카카오 로그인 시작
    private fun startKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                handleKakaoLoginResponse(token, error)
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = ::handleKakaoLoginResponse)
        }
    }

    private fun handleKakaoLoginResponse(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Log.e("Login", "카카오 로그인 실패: $error")
        } else if (token != null) {
            Log.d("Login", "카카오 로그인 성공: ${token.accessToken}")
            sendTokenToServer(token.accessToken) // ✅ 백엔드에 accessToken 전송
        }
    }

    // ✅ 백엔드에 accessToken 전송 후 JWT 반환
    private fun sendTokenToServer(kakaoAccessToken: String) {
        Log.d("Login", "백엔드로 AccessToken 전송: $kakaoAccessToken")

        authApiService.loginWithKakao("Bearer $kakaoAccessToken").enqueue(object : Callback<ApiResponse<KakaoJwtDTO>> {
            override fun onResponse(call: Call<ApiResponse<KakaoJwtDTO>>, response: Response<ApiResponse<KakaoJwtDTO>>) {
                if (response.isSuccessful) {
                    val jwt = response.body()?.result?.jwt
                    Log.d("Login", "JWT 수신: $jwt")
                    saveJwt(jwt)
                } else {
                    Log.e("Login", "JWT 요청 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<KakaoJwtDTO>>, t: Throwable) {
                Log.e("Login", "JWT 요청 에러: ${t.message}")
            }
        })
    }

    // ✅ JWT 저장
    private fun saveJwt(jwt: String?) {
        if (jwt.isNullOrEmpty()) {
            Log.e("Login", "❌ 저장할 JWT가 없음!")
            return
        }

        Log.d("Login", "✅ 저장할 JWT: $jwt")
        val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("jwt_token", jwt)
        editor.apply()

        // ✅ 자동 로그인 확인
        checkAutoLogin()
    }

    // ✅ 자동 로그인 체크
    private fun checkAutoLogin() {
        val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
        val savedJwt = sharedPref.getString("jwt_token", null)

        if (!savedJwt.isNullOrEmpty()) {
            Log.d("Login", "✅ 자동 로그인 - JWT: $savedJwt")

            val userApiService = RetrofitClient.createService(UserApiService::class.java)
            userApiService.getUserInfo("Bearer $savedJwt").enqueue(object : Callback<ApiResponse<AccountInfoDTO>> {
                override fun onResponse(
                    call: Call<ApiResponse<AccountInfoDTO>>,
                    response: Response<ApiResponse<AccountInfoDTO>>
                ) {
                    if (response.isSuccessful && response.body()?.result != null) {
                        Log.d("Login", "사용자 정보 확인 완료, 메인 화면 이동")
                        navigateToMainActivity()
                    } else {
                        Log.e("Login", "사용자 정보 없음, 추가 정보 입력 필요")
                        navigateToBodyInfoActivity()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<AccountInfoDTO>>, t: Throwable) {
                    Log.e("Login", "❌ 자동 로그인 API 요청 실패: ${t.message}")
                    navigateToBodyInfoActivity()
                }
            })
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToBodyInfoActivity() {
        startActivity(Intent(this, BodyinfoActivity::class.java))
        finish()
    }
}
