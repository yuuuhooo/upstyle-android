package com.umc.upstyle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.umc.upstyle.data.model.ApiResponse
import com.umc.upstyle.data.model.KakaoJwtDTO
import com.umc.upstyle.data.network.AuthApiService
import com.umc.upstyle.data.network.OotdApiService
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

        // setContentView()로 ViewBinding의 root 레이아웃을 설정
        setContentView(binding.root)

        // 앱 실행 시 자동 로그인 체크
        checkAutoLogin()

        // 비회원으로 둘러보기
        binding.tvNonMember.setOnClickListener {
            // 버튼 클릭 이벤트 처리
            // Intent 생성
            val intent = Intent(this, BodyinfoActivity::class.java)
            startActivity(intent) // 현재는 TestActivity로 이동. 실제로는 마이홈으로 가야 됨.
        }

        // 카카오로그인
        binding.btGoogleLogin.setOnClickListener {
            startKakaoLogin()
        }
    }

    // 1. 카카오 로그인 완료 후 accessToken 획득
    private fun startKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            // 카카오톡 앱 로그인
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("Login", "카카오톡 로그인 실패: $error")
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    } else {
                        // 카카오 계정 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
                    }
                } else if (token != null) {
                    Log.d("Login", "카카오 로그인 성공: ${token.accessToken}")
                    sendTokenToServer(token.accessToken) // ✅ 백엔드에 accessToken 전송
                }
            }
        } else {
            // 카카오 계정 로그인 (웹 브라우저)
            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
        }
    }

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("Login", "카카오 계정 로그인 실패: $error")
        } else if (token != null) {
            Log.d("Login", "카카오 계정 로그인 성공: ${token.accessToken}")
            sendTokenToServer(token.accessToken) // ✅ 백엔드에 accessToken 전송
        }
    }

    // 2. 백엔드에 accessToken 보내고 JWT 반환
    private fun sendTokenToServer(kakaoAccessToken: String) {
        Log.d("Login", "백엔드로 AccessToken 전송: $kakaoAccessToken")

        authApiService.loginWithKakao("Bearer $kakaoAccessToken").enqueue(object : Callback<ApiResponse<KakaoJwtDTO>> {
            override fun onResponse(
                call: Call<ApiResponse<KakaoJwtDTO>>,
                response: Response<ApiResponse<KakaoJwtDTO>>
            ) {
                if (response.isSuccessful) {
                    val jwt = response.body()?.result?.jwt
                    Log.d("Login", "JWT: $jwt")
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

    // 3. JWT를 SharedPreferences에 저장
    private fun saveJwt(jwt: String?) {
        if (jwt.isNullOrEmpty()) {
            Log.e("Login", "❌ 저장할 JWT가 없음!")
            return
        }

        val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("jwt_token", jwt)
        editor.apply()
    }

    // 자동 로그인 체크 함수 (JWT 있으면 자동 로그인 처리)
    private fun checkAutoLogin() {
        val sharedPref = getSharedPreferences("Auth", MODE_PRIVATE)
        val savedJwt = sharedPref.getString("jwt_token", null)

        if (!savedJwt.isNullOrEmpty()) {
            Log.d("Login", "자동 로그인 - JWT: $savedJwt")
            navigateToBodyInfoActivity()
        }
    }

    private fun navigateToBodyInfoActivity() {
        val intent = Intent(this, BodyinfoActivity::class.java)
        startActivity(intent)
        finish()
    }
}