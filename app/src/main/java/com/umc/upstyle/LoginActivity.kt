package com.umc.upstyle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.umc.upstyle.data.model.AccountInfoDTO
import com.umc.upstyle.data.model.ApiResponse
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

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 자동 로그인 체크
        checkAutoLogin()

        // ✅ 비회원으로 둘러보기
        binding.tvNonMember.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // ✅ 카카오 로그인 버튼 클릭 시 로그인 시작
        binding.btGoogleLogin.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("Login", "로그아웃 실패: ${error.message}")
                } else {
                    Log.d("Login", "카카오 로그아웃 완료, 새로 로그인 진행")
                    startKakaoLogin()  // 다시 로그인 요청
                }
            }
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
            requestUserInfo(token.accessToken) // ✅ 사용자 정보 요청 후 accessToken 전송
        }
    }

    // ✅ 사용자 정보 요청 (닉네임 & 이메일)
    private fun requestUserInfo(accessToken: String) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Login", "사용자 정보 요청 실패: $error")
                sendTokenToServer(accessToken) // 오류 시 그냥 access_token 전송
            } else if (user != null) {
                val kakaoAccount = user.kakaoAccount
                val email = kakaoAccount?.email
                val nickname = kakaoAccount?.profile?.nickname

                Log.d("Login", "카카오 사용자 정보: 닉네임=${nickname ?: "없음"}, 이메일=${email ?: "없음"}")

                // ✅ 이메일이나 닉네임이 없으면 추가 동의 요청
                val scopes = mutableListOf<String>()
                if (email == null && kakaoAccount?.emailNeedsAgreement == true) {
                    scopes.add("account_email") // 이메일 추가 동의 요청
                }
                if (nickname == null && kakaoAccount?.profileNeedsAgreement == true) {
                    scopes.add("profile_nickname") // 닉네임 추가 동의 요청
                }

                if (scopes.isNotEmpty()) {
                    UserApiClient.instance.loginWithNewScopes(this, scopes) { newToken, scopeError ->
                        if (scopeError != null) {
                            Log.e("Login", "추가 동의 요청 실패: $scopeError")
                        } else {
                            Log.d("Login", "추가 동의 요청 완료, 새로운 AccessToken: ${newToken?.accessToken}")
                            sendTokenToServer(newToken?.accessToken ?: accessToken)
                        }
                    }
                } else {
                    sendTokenToServer(accessToken) // ✅ access_token 전송
                }
            }
        }
    }

    // ✅ 백엔드에 accessToken 전송 후 JWT 반환
    private fun sendTokenToServer(kakaoAccessToken: String) {
        Log.d("Login", "백엔드로 AccessToken 전송: $kakaoAccessToken")

        authApiService.loginWithKakao(kakaoAccessToken).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jwt = response.body()!!
                    Log.d("Login", "JWT 수신: $jwt")
                    saveJwt(jwt)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Login", "JWT 요청 실패: $errorBody")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
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
