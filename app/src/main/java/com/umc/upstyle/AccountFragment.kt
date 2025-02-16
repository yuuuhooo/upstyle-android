package com.umc.upstyle

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.user.UserApiClient
import com.umc.upstyle.data.model.AccountInfoDTO
import com.umc.upstyle.data.model.ApiResponse
import com.umc.upstyle.data.network.RetrofitClient
import com.umc.upstyle.data.network.UserApiService
import com.umc.upstyle.databinding.FragmentAccountBinding
import com.umc.upstyle.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment : Fragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val userApiService = RetrofitClient.createService(UserApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myProfileBtn.setOnClickListener { findNavController().navigate(R.id.myProfileFragment) }
        binding.privacyPolicy.setOnClickListener {
            val bundle = Bundle().apply { putString("URL", "https://judicious-quiver-042.notion.site/1841ce3fbf8380acb266cd73c4ab72ed")  } // URL을 전달
            findNavController().navigate(R.id.webViewFragment, bundle)
        }
        binding.termsOfService.setOnClickListener {
            val bundle = Bundle().apply { putString("URL", "https://judicious-quiver-042.notion.site/1841ce3fbf8380d68eeedc8911eb1af0")  } // URL을 전달
            findNavController().navigate(R.id.webViewFragment, bundle)
        }

        binding.logoutBtn.setOnClickListener {
            showLoadItemPopupDialog()
        }

        fetchUserNickname()
    }

    // ✅ JWT로 닉네임, 이메일 가져오기
    private fun fetchUserNickname() {
        val sharedPref = requireContext().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val jwtToken = sharedPref.getString("jwt_token", null)
        val isGuest = sharedPref.getBoolean("is_guest", false)  // 비회원 여부 확인

//        if (jwtToken.isNullOrEmpty()) {
//            Log.e("Account", "JWT 없음 → 로그인 화면 이동")
//            Log.e("Account", "$jwtToken")
//            Log.d("Account", "현재 is_guest 값: $isGuest")
//            return
//        }

        Log.d("Account", "JWT 있음 → 사용자 정보 요청 시작, JWT: $jwtToken")

        if (isGuest) { // 비회원 모드
            Log.d("Account", "비회원 모드 활성화됨")
            binding.userNickname.text = "비회원"
            binding.userEmail.text = "이메일을 불러올 수 없음"
        } else { // 회원 모드
            userApiService.getUserInfo("Bearer $jwtToken")
                .enqueue(object : Callback<ApiResponse<AccountInfoDTO>> {
                    override fun onResponse(
                        call: Call<ApiResponse<AccountInfoDTO>>,
                        response: Response<ApiResponse<AccountInfoDTO>>
                    ) {
                        Log.d("Account", "서버 응답 코드: ${response.code()}")
                        binding.userNickname.text = response.body()?.result?.nickname ?: "닉네임을 불러올 수 없음"
                        binding.userEmail.text = response.body()?.result?.email ?: "이메일을 불러올 수 없음"
                    }

                    override fun onFailure(call: Call<ApiResponse<AccountInfoDTO>>, t: Throwable) {
                        Log.e("Account", "사용자 정보 요청 실패: ${t.message}")
                    }
                })
        }
    }


    private fun showLoadItemPopupDialog() {
        val dialog = LogoutPopupDialog(
            requireContext(),
            onNoClicked = {

            },
            onYesClicked = {
                logout()
            }
        )
        dialog.show()
    }

    private fun logout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("Logout", "❌ 카카오 로그아웃 실패", error)
            } else {
                Log.d("Logout", "✅ 카카오 로그아웃 성공")
            }
            // ✅ 로그아웃 실패해도 JWT 삭제 및 로그인 화면으로 이동
            clearJwt()
        }
    }

    private fun clearJwt() {
        val sharedPref = requireContext().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        sharedPref.edit().remove("jwt_token").apply()

        Log.d("Logout", "✅ JWT 삭제 완료, 로그인 화면으로 이동")

        // 로그인 화면으로 이동
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
//        requireActivity().finish() // 현재 액티비티 종료
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지: 뷰가 파괴되면 바인딩 해제
        _binding = null
    }

}





