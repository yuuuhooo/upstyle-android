package com.umc.upstyle

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.umc.upstyle.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWebViewBinding.bind(view)

        val url = arguments?.getString("URL") ?: "https://default-url.com" // 기본 URL 설정

        binding.webView.apply {
            settings.javaScriptEnabled = true  // JavaScript 활성화 (필요한 경우)
            webViewClient = WebViewClient()    // WebView에서 내부에서 페이지 로딩
            loadUrl(url)  // URL 로드
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
