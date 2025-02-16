package com.umc.upstyle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.umc.upstyle.data.network.AuthApiService
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

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 자동 로그인 체크
        checkAutoLogin()

        // 비회원으로 둘러보기
        binding.tvNonMember.setOnClickListener {
            val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
            sharedPref.edit().putBoolean("is_guest", true).apply() // 비회원 상태 저장
            startActivity(Intent(this, MainActivity::class.java))
        }

        // 카카오 로그인 버튼 클릭 시 로그인 시작
        binding.btKakaoLogin.setOnClickListener {
            logoutAndStartKakaoLogin() // 기존 JWT 삭제 후 로그인 시작
        }
    }

    private fun checkAutoLogin() {
        val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
        val jwtToken = sharedPref.getString("jwt_token", null)
        val isGuest = sharedPref.getBoolean("is_guest", false)

        if (isGuest) {
            Log.d("Login", "✅ 비회원 모드: 로그인 없이 앱 사용 가능")
            startActivity(Intent(this, MainActivity::class.java))
            finish() // 로그인 화면 종료
            return
        }

        if (!jwtToken.isNullOrEmpty()) {
            Log.d("Login", "✅ 자동 로그인: 저장된 JWT 존재")
            startActivity(Intent(this, MainActivity::class.java))
            finish() // 로그인 화면 종료
        } else {
            Log.d("Login", "❌ 자동 로그인 실패: JWT 없음")
            // 로그인 화면 유지
        }
    }


    // 기존 JWT 삭제 후 카카오 로그인 시작
    private fun logoutAndStartKakaoLogin() {
        val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
        sharedPref.edit().remove("jwt_token").apply()
        Log.d("JWT", "✅ 기존 JWT 삭제 완료")

        UserApiClient.instance.logout { error ->
            startKakaoLogin()
        }
    }

    // 카카오 로그인 시작
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
            saveAccessToken(token.accessToken) // ✅ 카카오 Access Token 저장
            requestUserInfo(token.accessToken) // ✅ 사용자 정보 요청 후 백엔드로 전송
        }
    }

    // Access Token 저장
    private fun saveAccessToken(accessToken: String) {
        val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("kakao_access_token", accessToken)
        editor.apply()
    }

    // 사용자 정보 요청 (닉네임 & 이메일)
    private fun requestUserInfo(accessToken: String) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Login", "사용자 정보 요청 실패: $error")
                sendTokenToServer(accessToken) // 오류 시 Access Token만 전송
            } else if (user != null) {
                val kakaoAccount = user.kakaoAccount
                val email = kakaoAccount?.email
                val nickname = kakaoAccount?.profile?.nickname

                Log.d("Login", "카카오 사용자 정보: 닉네임=${nickname ?: "없음"}, 이메일=${email ?: "없음"}")

                sendTokenToServer(accessToken) // 사용자 정보와 함께 Access Token 전송
            }
        }
    }

    // 백엔드에 Access Token 전송 후 JWT 반환
    private fun sendTokenToServer(kakaoAccessToken: String) {
        Log.d("Login", "백엔드로 AccessToken 전송: $kakaoAccessToken")

        authApiService.loginWithKakao(kakaoAccessToken).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jwt = response.body()!!
                    Log.d("Login", "✅ JWT 수신: $jwt")
                    saveJwt(jwt)
                    navigateToBodyInfoActivity()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Login", "❌ JWT 요청 실패: $errorBody")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Login", "❌ JWT 요청 에러: ${t.message}")
            }
        })
    }

    // 기존 JWT 삭제 후 새로운 JWT 저장
    private fun saveJwt(jwt: String) {
        val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove("jwt_token") // 기존 JWT 삭제
        editor.putString("jwt_token", jwt) // 새로운 JWT 저장
        editor.remove("is_guest") // 비회원 모드 해제
        editor.apply()

        Log.d("JWT", "✅ 새로운 JWT 저장 완료")
    }

    private fun navigateToBodyInfoActivity() {
        startActivity(Intent(this, BodyinfoActivity::class.java))
        finish()
    }
}
