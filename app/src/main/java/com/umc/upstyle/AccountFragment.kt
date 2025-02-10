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
import com.umc.upstyle.databinding.FragmentAccountBinding
import com.umc.upstyle.databinding.FragmentSearchBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

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
                Log.e("Logout", "카카오 로그아웃 실패", error)
            } else {
                Log.d("Logout", "카카오 로그아웃 성공")
                clearJwt()
            }
        }
    }

    private fun clearJwt() {
        val sharedPref = requireContext().getSharedPreferences("Auth", Context.MODE_PRIVATE) // requireContext() 사용
        val editor = sharedPref.edit()
        editor.remove("jwt_token") // 저장된 JWT 삭제
        editor.apply()

        // 로그인 화면으로 이동
        val intent = Intent(requireContext(), LoginActivity::class.java) // requireContext() 사용
        startActivity(intent)
        requireActivity().finish() // Activity 종료
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지: 뷰가 파괴되면 바인딩 해제
        _binding = null
    }

}




